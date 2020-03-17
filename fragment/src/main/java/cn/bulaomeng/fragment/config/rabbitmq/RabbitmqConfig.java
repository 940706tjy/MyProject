package cn.bulaomeng.fragment.config.rabbitmq;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RabbitmqParam.class})
public class RabbitmqConfig {
}
