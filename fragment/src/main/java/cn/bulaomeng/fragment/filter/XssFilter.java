
package cn.bulaomeng.fragment.filter;


import cn.bulaomeng.fragment.util.XssHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@WebFilter(filterName="xssFilter",urlPatterns="/*")
public class XssFilter implements Filter {
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        filterChain.doFilter(new XssHttpServletRequestWrapper(request),servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}