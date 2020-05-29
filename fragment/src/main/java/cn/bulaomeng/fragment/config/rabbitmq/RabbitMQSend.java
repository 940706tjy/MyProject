package cn.bulaomeng.fragment.config.rabbitmq;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

/**
* @Description: 生产者接口
*/
@Component
public interface RabbitMQSend {


    /**
     * fanout类型 发送消息
     */

    void fanoutSendMsg(String exchangeName, Object message);


    /**
     * direct类型 发送消息
     */

    void directSendMsg(String exchangeName, String routingKey, Object message);


    /**
     * topic类型 发送消息
     */

    void topicSendMsg(String exchangeName, String routingKey, Object message);


    /**
     * 延时队列
     *
     */
    void delaySendMsg(String exchange, String routingKey, Object message,
                       MessagePostProcessor messagePostProcessor);
}
