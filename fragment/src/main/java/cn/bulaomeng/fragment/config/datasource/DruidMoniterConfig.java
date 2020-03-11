package cn.bulaomeng.fragment.config.datasource;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
* @Description: Druid 监控配置
* @Author: tjy
* @Date: 2020/3/11
*/

@Configuration
public class DruidMoniterConfig {
    /**
     *    1、配置一个管理后台的Servlet
     */
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String,String> initParams = new HashMap<>();
        initParams.put("loginUsername","admin");
        initParams.put("loginPassword","admin");
        //默认就是允许所有访问
        initParams.put("allow","");
        //黑名单的IP
        initParams.put("deny","192.168.15.21");
        //禁用HTML页面上的“REST ALL”功能
        initParams.put("resetEnable","false");
        bean.setInitParameters(initParams);
        return bean;
    }

    /**
     * 2、配置一个web监控的filter
     */
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
 
        Map<String,String> initParams = new HashMap<>();
        initParams.put("exclusions","*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
 
        bean.setInitParameters(initParams);
 
        bean.setUrlPatterns(Arrays.asList("/*"));
 
        return  bean;
    }
}