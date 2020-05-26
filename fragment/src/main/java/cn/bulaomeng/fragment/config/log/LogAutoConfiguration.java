package cn.bulaomeng.fragment.config.log;

import cn.bulaomeng.fragment.config.log.logaspect.LogFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 启动类  weds.log.switch是日志开关。默认开启状态。true是开启状态。false是关闭状态  在application.yml配置即可
 * Created by zhangchao on 2019/1/25.
 */
@Configuration
@ComponentScan("cn.bulaomeng.fragment.config.log")
@ConditionalOnProperty(name = "weds.log.switch", matchIfMissing = true, havingValue = "true")
public class LogAutoConfiguration {

    @Bean
    public LogFilter initLogFilter() {
        return new LogFilter();
    }
}
