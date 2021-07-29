package top.chaser.framework.starter.gray.ribbon;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.base.util.SpringBeanHelper;
import lombok.extern.slf4j.Slf4j;
import top.chaser.framework.starter.gray.support.WeightMeta;
import top.chaser.framework.starter.gray.support.WeightRandomUtils;
import top.chaser.framework.starter.gray.autoconfigure.GrayProperties;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定路由规则
 *
 * @description:
 * @author: chaser8
 * @date 2020/9/9 8:55 下午
 **/
@Slf4j
public class GrayRule extends ZoneAvoidanceRule {

    public Server choose(List<Server> servers) {
        Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(servers);
        if (server.isPresent()) {
            return server.get();
        } else {
            return null;
        }
    }

    @Override
    public Server choose(Object key) {
        log.debug("211222111");
        log.error("211");
        GrayProperties grayProperties = SpringBeanHelper.getBean(GrayProperties.class);
        if (!grayProperties.isEnable()) {
            return super.choose(key);
        }
        Map<String, GrayProperties.Strategy> strategys = grayProperties.getStrategys();
        String clientName = null;

        ILoadBalancer loadBalancer = getLoadBalancer();
        if (loadBalancer instanceof ZoneAwareLoadBalancer) {
            clientName = ((ZoneAwareLoadBalancer) loadBalancer).getClientConfig().getClientName();
            if (!strategys.containsKey(clientName)) {
                return super.choose(key);
            }
        } else {
            return super.choose(key);
        }

        GrayProperties.Strategy strategy = strategys.get(clientName);

        List<Server> allServers = loadBalancer.getAllServers();
        List<Server> reachableServers = loadBalancer.getReachableServers();

        if (allServers.size() == 0 || reachableServers.size() == 0) {
            return super.choose(key);
        }

        List<Server> grayReachableServers = new ArrayList<>();
        List<Server> generalReachableServers = new ArrayList<>();


        for (Server instance : reachableServers) {
            if (instance.getClass().getName().indexOf("NacosServer") != -1) {
                Map<String, String> metadata = ReflectUtil.invoke(instance, "getMetadata");
                String version = metadata.get("version");

                if (strategy.getVersion().trim().equals(version.trim())) {
                    grayReachableServers.add(instance);
                } else {
                    generalReachableServers.add(instance);
                }
            } else {
                return super.choose(key);
            }
        }

        List<Server> callServers = null;
        if (grayReachableServers.size() > 0 && match(strategy, key)) {
            Map<List<Server>, Integer> weightMap = new HashMap<>();
            weightMap.put(grayReachableServers, strategy.getPercentage());
            weightMap.put(generalReachableServers, 100 - strategy.getPercentage());
            WeightMeta<List<Server>> nodes = WeightRandomUtils.buildWeightMeta(weightMap);
            callServers = nodes.random();
            if (log.isDebugEnabled()) {
                log.debug("gray to:{}", JSONUtil.toJSONString(callServers.stream().map(server -> server.getMetaInfo().getInstanceId() + "@" + JSONUtil.getByPath(JSONUtil.toJSONString(server), "metadata.version")).collect(Collectors.toList())));
            }
        } else {
            callServers = generalReachableServers;
            if (log.isDebugEnabled()) {
                log.debug("general to:{}", JSONUtil.toJSONString(callServers.stream().map(server -> server.getMetaInfo().getInstanceId() + "@" + JSONUtil.getByPath(JSONUtil.toJSONString(server), "metadata.version")).collect(Collectors.toList())));
            }
        }

        return choose(callServers);
    }

    /**
     * 规则匹配
     *
     * @param strategy 策略
     * @param key      参数
     * @return boolean
     * @author
     * @date 2020/9/9 8:56 下午
     */
    private boolean match(GrayProperties.Strategy strategy, Object key) {
        boolean flag = true;
        Set<GrayProperties.Condition> conditions = strategy.getConditions();
        if (key != null) {
            condition:
            for (GrayProperties.Condition condition : conditions) {
                type:
                switch (condition.getType()) {
                    case PARAM:
                        boolean bodyFlag = matchParams(condition, key);
                        if (bodyFlag) {
                            break type;
                        } else {
                            flag = false;
                            break condition;
                        }
                    case HEADER:
                        boolean headerFlag = matchHeader(condition, key);
                        if (headerFlag) {
                            break type;
                        } else {
                            flag = false;
                            break condition;
                        }
                    default:
                        flag = false;
                        break condition;
                }
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 参数匹配
     *
     * @param condition
     * @param key
     * @return boolean
     * @author
     * @date 2020/9/9 8:57 下午
     */
    protected boolean matchParams(GrayProperties.Condition condition, Object key) {
        boolean flag = false;
        if (key instanceof GrayParams) {
            Map<String, Object> params = ((GrayParams) key).getParams();
            String paramsValue = JSONUtil.getByPath(params, condition.getName(), String.class);
            if (log.isDebugEnabled()) {
                log.debug("condition:{},params:{}", JSONUtil.toJSONString(condition), paramsValue);
            }
            if (!StrUtil.isEmpty(paramsValue)) {
                if (paramsValue.equals(condition.getValue())) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * matchHeader
     *
     * @param condition
     * @param key
     * @return boolean
     * @author
     * @date 2020/9/9 8:57 下午
     */
    protected boolean matchHeader(GrayProperties.Condition condition, Object key) {
        boolean flag = false;
        if (key instanceof GrayParams) {
            Map<String, String> headers = ((GrayParams) key).getHeaders();
            String headerValue = headers.get(condition.getName());
            if (log.isDebugEnabled()) {
                log.debug("condition:{},headers:{}", JSONUtil.toJSONString(condition), headerValue);
            }
            if (!StrUtil.isEmpty(headerValue)) {
                if (headerValue.equals(condition.getValue())) {
                    flag = true;
                }
            }
        }
        return flag;
    }
}
