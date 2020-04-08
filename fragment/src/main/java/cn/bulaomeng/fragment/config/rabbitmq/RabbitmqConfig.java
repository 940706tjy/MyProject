package cn.bulaomeng.fragment.config.rabbitmq;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RabbitmqParam.class})
public class RabbitmqConfig {
    @Bean
    public MessageConverter configConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
