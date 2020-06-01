package cn.bulaomeng.fragment.config.rabbitmq;

import cn.bulaomeng.fragment.constant.CommConstant;
import cn.bulaomeng.fragment.util.DateUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
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
@Slf4j
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
    public void delaySendMsg(String exchange, String routingKey, Object obj,
                             Integer delayTime) {

        rabbitTemplate.convertAndSend(exchange, routingKey,
                dataToClientMQ(CommConstant.RABBITMQ_PROJECT, obj),
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    message.getMessageProperties().setDelay(delayTime);   // 毫秒为单位，指定此消息的延时时长
                    return message;
                });
    }

    @Override
    public void formatDelaySendMsg(String exchange, String routingKey, Object obj, String formatDelayTime) throws Exception {

        // integer 最大取值范围（对应毫秒转换天 24d）
        Integer maxTime = 2147483647;
        // 取当前时间与延时时间的差值（单位s）
        Long difference = DateUtil.dateDiff("second" ,DateUtil.strToTimestamp(formatDelayTime,
                "yyyy-MM-dd HH:mm:ss") ,DateUtil.nowTimestamp());

        // 如果设置时间 < 当前时间 手动抛出异常
        if(difference <= 0){
            throw new Exception("设置延时日期小于当前时间");
        }

        // 如果超出阈值 则手动抛出异常
        if(maxTime < difference * 1000){
            throw new Exception("设置延时日期超长");
        }

        // 转为ms
        Integer delayTime = Math.toIntExact(difference * 1000);

        log.info("延时任务时间差值（ms）: [{}]", delayTime);
        rabbitTemplate.convertAndSend(exchange, routingKey,
                dataToClientMQ(CommConstant.RABBITMQ_PROJECT, obj),
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    message.getMessageProperties().setDelay(delayTime);   // 毫秒为单位，指定此消息的延时时长
                    return message;
                });

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
