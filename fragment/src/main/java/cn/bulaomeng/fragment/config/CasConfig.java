package cn.bulaomeng.fragment.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Configuration
public class CasConfig {
    @Value("${sso.cas.path-url}")
    private String pathUrl;
    /**
    * @Description:  用于单点退出，该监听器和过滤器用于实现单点登出功能，可选配置
    * @Param: []
    * @return: org.springframework.boot.web.servlet.FilterRegistrationBean
    * @Author: tjy
    * @Date: 2019/6/3
    */
    @Bean
    public FilterRegistrationBean SingleSignOutHttpSessionListenerBean(){
        FilterRegistrationBean SingleSignOutHttpSession = new FilterRegistrationBean();
        SingleSignOutHttpSession.setFilter(new SingleSignOutFilter());
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add(pathUrl);// 设置匹配的url
        SingleSignOutHttpSession.setUrlPatterns(urlPatterns);
        return SingleSignOutHttpSession;
    }

    /** 
    * @Description: 该过滤器负责用户的认证工作，必须启用它 
    * @Param: [] 
    * @return: org.springframework.boot.web.servlet.FilterRegistrationBean 
    * @Author: tjy
    * @Date: 2019/6/3 
    */ 
    @Bean
    public  FilterRegistrationBean casBean(){
        FilterRegistrationBean Authentication=new FilterRegistrationBean();
        Authentication.setFilter(new AuthenticationFilter());
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerLoginUrl", "http://auth.hcnu.edu.cn/authserver/login");
        initParameters.put("serverName", "http://127.0.0.1:8081");
        Authentication.setInitParameters(initParameters);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add(pathUrl);// 设置匹配的url
        Authentication.setUrlPatterns(urlPatterns);
        return Authentication;
    }
    /*** 
    * @Description: 该过滤器负责对Ticket的校验工作，必须启用它 
    * @Param: [] 
    * @return: org.springframework.boot.web.servlet.FilterRegistrationBean 
    * @Author: tjy
    * @Date: 2019/6/3 
    */ 
    @Bean
    public FilterRegistrationBean TicketValidationBean(){
        FilterRegistrationBean TicketValidation=new FilterRegistrationBean();
        TicketValidation.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", "http://auth.hcnu.edu.cn/authserver");
        initParameters.put("serverName", "http://127.0.0.1:8081");
        initParameters.put("encoding", "UTF-8");
        TicketValidation.setInitParameters(initParameters);
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add(pathUrl);// 设置匹配的url
        TicketValidation.setUrlPatterns(urlPatterns);
        return TicketValidation;

    }
    /** 
    * @Description: 该过滤器负责实现HttpServletRequest请求的包裹，比如允许开发者通过HttpServletRequest的getRemoteUser()方法获得SSO登录用户的登录名。
    * @Param: [] 
    * @return: org.springframework.boot.web.servlet.FilterRegistrationBean 
    * @Author: tjy
    * @Date: 2019/6/3 
    */ 
    @Bean
    public  FilterRegistrationBean CasHttpServletRequestWrapperBean(){
        FilterRegistrationBean CasHttpServletRequestWrapper=new FilterRegistrationBean();
        CasHttpServletRequestWrapper.setFilter(new HttpServletRequestWrapperFilter());
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add(pathUrl);// 设置匹配的url
        CasHttpServletRequestWrapper.setUrlPatterns(urlPatterns);
        return CasHttpServletRequestWrapper;
    }
/***
* @Description: 设置白名单
* @Param: [] 
* @return: org.springframework.boot.web.servlet.FilterRegistrationBean 
* @Author: tjy
* @Date: 2019/6/3 
*/ 
   /* @Bean
    public  FilterRegistrationBean casAuthenticationBean(){
        FilterRegistrationBean casAuthentication=new FilterRegistrationBean();
       casAuthentication.setFilter(new AuthenticationFilter());
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("casServerLoginUrl", "http://authserver.xxx.edu.cn/authserver/login");
        initParameters.put("casWhiteUrl", "/demp.*index.jsp");//按照业务设置
        initParameters.put("service", "http://127.0.0.1:8080");
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("/*");// 设置匹配的url
        return casAuthentication;
    }*/
}
