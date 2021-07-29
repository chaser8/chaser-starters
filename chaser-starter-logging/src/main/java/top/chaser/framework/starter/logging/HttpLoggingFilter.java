package top.chaser.framework.starter.logging;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.Header;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.web.http.request.MultiReadHttpServletRequest;
import top.chaser.framework.common.web.http.response.ResponseWrapper;
import top.chaser.framework.starter.logging.processor.LoggingProcessor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class HttpLoggingFilter extends OncePerRequestFilter {
    public HttpLoggingFilter(List<LoggingProperties.ProcessorProperties> processorProperties, ApplicationContext applicationContext) {
        this.processorProperties = processorProperties;
        this.applicationContext = applicationContext;
    }

    protected List<LoggingProperties.ProcessorProperties> processorProperties;
    protected ApplicationContext applicationContext;
    protected AntPathMatcher pathMatcher = new AntPathMatcher();
    protected Map<String, Set<LoggingProcessor>> includeUrlsProcessorMap = new HashMap();
    protected Map<String, Set<LoggingProcessor>> excludeUrlsProcessorMap = new HashMap();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Date requestTime = Calendar.getInstance().getTime();
        LogInfo logInfo = new LogInfo();
        Set<LoggingProcessor> allLoggingProcessors = new HashSet<>();
        allLoggingProcessors.addAll(getExcludeUrlsLoggingProcessors(request));
        allLoggingProcessors.addAll(getIncludeUrlsLoggingProcessors(request));
        if (allLoggingProcessors.size() < 1) {
            filterChain.doFilter(request, response);
        } else {
            ResponseWrapper wrapper = new ResponseWrapper(response);
            try {
                filterChain.doFilter(request, wrapper);
            } finally {
                try {
                    logInfo.setRequestTime(requestTime);
                    process(logInfo, request, wrapper, response, allLoggingProcessors);
                } catch (Exception e) {
                    log.error("RequestLoggingFilter error", e);
                }
            }
        }
    }

    @SneakyThrows
    protected void process(LogInfo logInfo, HttpServletRequest request, ResponseWrapper responseWrapper, HttpServletResponse response, Set<LoggingProcessor> allLoggingProcessors) {
        logInfo.setClientIp(ServletUtil.getClientIP(request));
//        User user = SessionUtil.getCurrentUser(request);
//
//        logInfo.setUser(user);
        logInfo.setUserAgent(ServletUtil.getHeader(request, Header.USER_AGENT.getValue(), request.getCharacterEncoding()));
        logInfo.setResponseTime(Calendar.getInstance().getTime());
        logInfo.setUri(request.getRequestURI());
        logInfo.setServerIp(getServerIp());
        logInfo.setMethod(HttpMethod.resolve(request.getMethod()));
        logInfo.setQueryString(JSONUtil.toJSONString(ServletUtil.getParamMap(request)));
        logInfo.setUsedMilliseconds(DateUtil.betweenMs(logInfo.getRequestTime(), logInfo.getResponseTime()));

        String requestContent = null;
        if (ServletUtil.isMultipart(request)) {
            List<Map<String, Object>> files = request.getParts().stream().map(part -> {
                Map<String, Object> partMap = new HashMap<>();
                partMap.put("fileName", part.getSubmittedFileName());
                partMap.put("contentType", part.getContentType());
                return partMap;
            }).collect(Collectors.toList());
            requestContent = JSONUtil.toJSONString(files);
        } else {
            MultiReadHttpServletRequest multiReadHttpServletRequest = WebUtils.getNativeRequest(request, MultiReadHttpServletRequest.class);
            requestContent = multiReadHttpServletRequest.getBody();
        }
        logInfo.setRequestContent(requestContent);

        String responseContent = responseWrapper.getContent();
        logInfo.setResponseContent(responseContent);
        try {
            if (JSONUtil.isJson(responseContent)) {
                Map responseContentMap = JSONUtil.parseObject(responseContent, HashMap.class);
                boolean isSuccess = Convert.toBool(responseContentMap.get("success"));
                logInfo.setSuccess(isSuccess);
            }
        } catch (Exception e) {
            log.trace("解析出参出错", e);
        }


        for (LoggingProcessor loggingProcessor : allLoggingProcessors) {
            try {
                loggingProcessor.process(logInfo, request, responseWrapper);
            } catch (Exception e) {
                log.error("loggingProcessor error", e);
            }
        }
    }


    /**
     * getIncludeUrlsLoggingProcessors
     *
     * @param request
     * @author yangzb
     * @date 2021/6/15 4:38 下午
     */
    protected Set<LoggingProcessor> getExcludeUrlsLoggingProcessors(HttpServletRequest request) {
        Set<LoggingProcessor> loggingProcessors = new HashSet<>();
        Set<String> urls = excludeUrlsProcessorMap.keySet();
        for (String url : urls) {
            if (!pathMatcher.match(url, request.getRequestURI())) {
                loggingProcessors.addAll(excludeUrlsProcessorMap.get(url));
            }
        }
        return loggingProcessors;
    }

    /**
     * getIncludeUrlsLoggingProcessors
     *
     * @param request
     * @author yangzb
     * @date 2021/6/15 4:38 下午
     */
    protected Set<LoggingProcessor> getIncludeUrlsLoggingProcessors(HttpServletRequest request) {
        Set<String> urls = includeUrlsProcessorMap.keySet();
        for (String url : urls) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                return includeUrlsProcessorMap.get(url);
            }
        }
        return Sets.newHashSet();
    }

    protected String getServerIp() {
        List<String> hostAddress = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = null;
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresss = ni.getInetAddresses();
                while (addresss.hasMoreElements()) {
                    InetAddress nextElement = addresss.nextElement();
                    hostAddress.add(nextElement.getHostAddress());
                }
            }
        } catch (Exception e) {
            log.error("获取本机ip出错", e);
        }
        return CollUtil.join(hostAddress, ",");
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        for (LoggingProperties.ProcessorProperties processorProperty : processorProperties) {
            Set<String> excludeUrls = processorProperty.getExcludeUrls();
            Set<String> includeUrls = processorProperty.getIncludeUrls();
            if (CollUtil.isNotEmpty(excludeUrls) && CollUtil.isNotEmpty(includeUrls)) {
                throw new InvalidConfigurationPropertyValueException("processors", processorProperty, "请检查配置excludeUrls 和 includeUrls 不能同时存在");
            }

            if (CollUtil.isNotEmpty(excludeUrls)) {
                for (String excludeUrl : excludeUrls) {
                    Set<LoggingProcessor> loggingProcessors = Optional.ofNullable(excludeUrlsProcessorMap.get(excludeUrl)).orElse(new HashSet<LoggingProcessor>());
                    LoggingProcessor loggingProcessor = Optional.of(applicationContext.getBean(processorProperty.getProcessorName(), LoggingProcessor.class))
                            .orElseThrow(() -> new NoSuchBeanDefinitionException(StrUtil.format("No bean:{} definition", processorProperty.getProcessorName())));
                    loggingProcessors.add(loggingProcessor);
                    excludeUrlsProcessorMap.put(excludeUrl, loggingProcessors);
                }
            } else if (CollUtil.isNotEmpty(includeUrls)) {
                for (String includeUrl : includeUrls) {
                    Set<LoggingProcessor> loggingProcessors = Optional.ofNullable(includeUrlsProcessorMap.get(includeUrl)).orElse(new HashSet<LoggingProcessor>());
                    LoggingProcessor loggingProcessor = Optional.of(applicationContext.getBean(processorProperty.getProcessorName(), LoggingProcessor.class))
                            .orElseThrow(() -> new NoSuchBeanDefinitionException(StrUtil.format("No bean:{} definition", processorProperty.getProcessorName())));
                    loggingProcessors.add(loggingProcessor);
                    includeUrlsProcessorMap.put(includeUrl, loggingProcessors);
                }
            } else if (CollUtil.isEmpty(includeUrls) && CollUtil.isEmpty(excludeUrls)) {
                Set<LoggingProcessor> loggingProcessors = Optional.ofNullable(includeUrlsProcessorMap.get("/**")).orElse(new HashSet<LoggingProcessor>());
                LoggingProcessor loggingProcessor = Optional.of(applicationContext.getBean(processorProperty.getProcessorName(), LoggingProcessor.class))
                        .orElseThrow(() -> new NoSuchBeanDefinitionException(StrUtil.format("No bean:{} definition", processorProperty.getProcessorName())));
                loggingProcessors.add(loggingProcessor);
                includeUrlsProcessorMap.put("/**", loggingProcessors);
            }
        }
    }
}
