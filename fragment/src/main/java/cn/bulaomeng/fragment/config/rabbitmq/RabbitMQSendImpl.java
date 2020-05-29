package cn.bulaomeng.fragment.config.rabbitmq;

import cn.bulaomeng.fragment.constant.CommConstant;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 创建发送队列实现
 * @Author: tjy
 */

@Service
public class RabbitMQSendImpl implements RabbitMQSend {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void fanoutSendMsg(String exchangeName, Object message) {
        rabbitTemplate.convertAndSend(exchangeName, dataToClientMQ(CommConstant.RABBITMQ_PROJECT, message));
    }


    @Override
    public void directSendMsg(String exchangeName, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, dataToClientMQ(CommConstant.RABBITMQ_PROJECT, message));
    }

    @Override
    public void topicSendMsg(String exchangeName, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey,
                dataToClientMQ(CommConstant.RABBITMQ_PROJECT, message));
    }

    @Override
    public void delaySendMsg(String exchange, String routingKey, Object message,
                             MessagePostProcessor messagePostProcessor) {

        rabbitTemplate.convertAndSend(exchange, routingKey,
                dataToClientMQ(CommConstant.RABBITMQ_PROJECT, message), messagePostProcessor);
    }


    //数据json序列化
    private String dataToClientMQ(String msgType, Object data) {
        Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put(CommConstant.MQ_CLIENT_MSG_TYPE, msgType);
        infoMap.put(CommConstant.MQ_CLIENT_DATA_INFO, data);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").
                setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
        return gson.toJson(infoMap);
    }

}
