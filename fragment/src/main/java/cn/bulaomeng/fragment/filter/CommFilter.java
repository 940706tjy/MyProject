package cn.bulaomeng.fragment.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("CommFilter")
public class CommFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		req.getHeader("Authorization");
		HttpServletResponse res = (HttpServletResponse) response;
		 res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Headers","*");
//		 res.setHeader("Access-Control-Allow-Headers",
//		 "User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,Authorization");
		 res.setHeader("Access-Control-Allow-Credentials", "true");
		 res.setHeader("Access-Control-Allow-Methods",
		 "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		 res.setHeader("Access-Control-Max-Age", "1209600");
		 res.setHeader("Access-Control-Expose-Headers", "accesstoken");
		 res.setHeader("Access-Control-Request-Headers", "accesstoken");
		 res.setHeader("Expires", "-1");
		 res.setHeader("Cache-Control", "no-cache");
		 res.setHeader("pragma", "no-cache");
		 req.setCharacterEncoding("UTF-8");
		chain.doFilter(request, res);
	}

	@Override
	public void destroy() {

	}

}
