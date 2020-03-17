package cn.bulaomeng.fragment.config.rabbitmq;

import cn.bulaomeng.fragment.entity.Fragment;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @Author : JCccc
 * @CreateTime : 2019/9/3
 * @Description :
 **/
@Service
@Slf4j
public class FanoutReceiverA {
 
    //@RabbitListener(queues = "topicLog")
    public void process(Message fragment, @Payload Object msg,  Channel channel) {
        // 如果手动ACK,消息会被监听消费,但是消息在队列中依旧存在,
        // 如果 未配置 acknowledge-mode 默认是会在消费完毕后自动ACK掉
       // final long deliveryTag = fragment.getMessageProperties().getDeliveryTag();
        JSONObject jsonBody;
        //String routingKey = "";
        //判断类型传入消息的类型
        try {
            //channel.basicAck(deliveryTag, false);
            if (msg instanceof String) {
                jsonBody = JSONObject.parseObject((String) msg);
            } else if (msg instanceof byte[]) {
                jsonBody = (JSONObject) JSONObject.parse((byte[]) msg);
            } else if (msg instanceof Message) {
                //获得body体重的数据
                jsonBody = (JSONObject) JSONObject.parse(((Message) msg).getBody());
                //获得路由键
           /* MessageProperties mss = fragment.getMessageProperties();
            routingKey = mss.getReceivedRoutingKey();*/
            } else {
                log.error("未知数据类型,已经忽略:{}", msg);
                return;
            }
            if (null == jsonBody || !jsonBody.containsKey("msgType") || !jsonBody.containsKey("data")) {
                log.error("mq接收的数据格式错误，记录忽略:{}", jsonBody);
                return;
            }
            //获取所标记的名称
            String msgType = jsonBody.get("msgType").toString();
            //根据不同类别，进行业务区分
            switch (msgType){
                case "":
                    break;
                    default:
                        break;
            }
            //获取数据
            Fragment obj = jsonBody.getObject("data",Fragment.class);
            //log.info("路由键:" + routingKey + "类型:" + msgType + "对象：" + obj);
            log.info("类型:" + msgType + "对象：" + obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}