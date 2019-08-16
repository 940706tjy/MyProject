package cn.bulaomeng.fragment.filter;

import cn.bulaomeng.fragment.util.XssHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName="xssFilter",urlPatterns="/*")
public class XssFilter implements Filter {
    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String path = request.getServletPath();
        //由于@WebFilter注解配置的是urlPatterns="/*"(过滤所有请求),所以这里对不需要过滤的静态资源url,作忽略处理
        String[] exclusionsUrls = {".js",".gif",".jpg",".png",".css",".ico"};
        for (String str : exclusionsUrls) {
            if (path.contains(str)) {
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }
        }
        filterChain.doFilter(new XssHttpServletRequestWrapper(request),servletResponse);
    }

    @Override
    public void destroy() {

    }
}
