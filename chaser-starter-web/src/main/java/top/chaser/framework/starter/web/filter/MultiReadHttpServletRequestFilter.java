package top.chaser.framework.starter.web.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import top.chaser.framework.common.web.http.request.MultiReadHttpServletRequest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @description:
 * @create: 2019-08-26 15:04
 **/
@Component
@WebFilter(filterName = "chaserRequestWrapperFilter", urlPatterns = "/")
@Order(1)
public class MultiReadHttpServletRequestFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ServletRequest requestWrapper = new MultiReadHttpServletRequest((HttpServletRequest) request);
        chain.doFilter(requestWrapper, response);
    }
}
