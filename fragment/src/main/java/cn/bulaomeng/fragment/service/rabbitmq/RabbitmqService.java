package cn.bulaomeng.fragment.service.rabbitmq;


import cn.bulaomeng.fragment.config.rabbitmq.RabbitmqParam;
import cn.bulaomeng.fragment.config.rabbitmq.Receiver;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

public interface RabbitmqService {


    /**
     * 声明开启一个 Queue 和 Exchange
     *
     * @param param
     */
    boolean publishTopic(RabbitmqParam param);

    /**
     * 订阅
     * @param param
     */
    SimpleMessageListenerContainer subscribeTopic(RabbitmqParam param, Receiver receiver);

    /**
     * 发布并订阅
     * @param param
     * @param receiver
     */
    void pubAndSub(RabbitmqParam param, Receiver receiver);


    /**
     * 发送消息
     * @param param
     * @param message
     */
    void sendMessage(RabbitmqParam param, String message);

    /**
     * 获取消息发送者
     * @param param
     * @return
     */
    RabbitTemplate getSender(RabbitmqParam param);

}
