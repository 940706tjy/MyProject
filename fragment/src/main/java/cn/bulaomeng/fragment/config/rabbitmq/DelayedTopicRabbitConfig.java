package cn.bulaomeng.fragment.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

// TODO  x-delay-message 实现延时队列
/**
 * RabbitMQ 延时队列配置
 *
 * @author tjy
 * @date 2020/5/29
 **/
@Configuration
public class DelayedTopicRabbitConfig {

    // 支付超时延时交换机
    public static final String DELAY_EXCHANGE_NAME = "delayCustom.exchange";

    // 超时订单关闭队列
    public static final String TIMEOUT_TRADE_QUEUE_NAME = "closeTradeCustom";

    public final static String SKYPYB_DELAY_KEY = "skypyb.key.delay";
    @Bean
    public Queue delayPayQueue() {
        return new Queue(DelayedTopicRabbitConfig.TIMEOUT_TRADE_QUEUE_NAME, true);
    }


    // 定义广播模式的延时交换机 无需绑定路由
    @Bean
    CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        // todo fanout 设置延时队列交换机
        /*FanoutExchange topicExchange = new FanoutExchange(DelayedTopicRabbitConfig.DELAY_EXCHANGE_NAME,
                true, false, args);
        topicExchange.setDelayed(true);
        return topicExchange;*/
        return new CustomExchange(DELAY_EXCHANGE_NAME, "x-delayed-message" ,
                true, false ,args);
    }

    // 绑定延时队列与交换机
    @Bean
    public Binding delayPayBind() {
        return BindingBuilder.bind(delayPayQueue()).to(delayExchange()).with(SKYPYB_DELAY_KEY).noargs();
    }

  /*  // 定义消息转换器
    @Bean
    Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 定义消息模板用于发布消息，并且设置其消息转换器
    @Bean
    RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }*/

   /* @Bean
    RabbitAdmin rabbitAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }*/

}