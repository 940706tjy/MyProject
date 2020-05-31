package cn.bulaomeng.fragment.web.rabbitmq;

import cn.bulaomeng.fragment.config.rabbitmq.DelayedTopicRabbitConfig;
import cn.bulaomeng.fragment.config.rabbitmq.RabbitMQSend;
import cn.bulaomeng.fragment.entity.Fragment;
import cn.bulaomeng.fragment.util.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static cn.bulaomeng.fragment.config.rabbitmq.DelayedTopicRabbitConfig.SKYPYB_DELAY_KEY;


/**
 * @author tjy
 * @date 2020/5/28
 **/
@RestController
@Slf4j
public class SendMessageController {

    @Autowired
    private RabbitMQSend rabbitMQSend;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping("/test")
    public void test() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        Fragment fr = new Fragment();
        fr.setId(1);
        fr.setName("我是延时队列");
        // 通过广播模式发布延时消息 延时30分钟 持久化消息 消费后销毁 这里无需指定路由，会广播至每个绑定此交换机的队列
        log.info("开始请求时间 [{}]", DateUtil.nowTimestamp());
        rabbitMQSend.delaySendMsg(DelayedTopicRabbitConfig.DELAY_EXCHANGE_NAME, SKYPYB_DELAY_KEY, fr,
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    message.getMessageProperties().setDelay(10000);   // 毫秒为单位，指定此消息的延时时长
                    return message;
                });
    }

    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage() {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        Fragment fr = new Fragment();
        fr.setId(1);
        fr.setName("暗殺教室可是大客戶");
        //Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        log.info("开始请求时间 [{}]", DateUtil.nowTimestamp());

        rabbitMQSend.directSendMsg("TestDirectExchange", "TestDirectRouting", fr);
        return "ok";
    }

    @RabbitListener(queues = "TestDirectQueue")
    public void getData(Message message, Object msg, Channel channel, @Headers Map<String, Object> map) {

        log.info("接收到请求时间 [{}]", DateUtil.nowTimestamp());
        log.info(" [{}]", message.getBody());
        log.info("[{}]", msg);
        log.info("[{}]", map);
        final long deliveryTag = message.getMessageProperties().getDeliveryTag();
        JSONObject json;
        if (msg instanceof String) {
            json = JSONObject.parseObject((String) msg);
        } else if (msg instanceof byte[]) {
            json = (JSONObject) JSONObject.parse((byte[]) msg);
        } else if (msg instanceof Message) {
            //获得body体的数据
            json = (JSONObject) JSONObject.parse(((Message) msg).getBody());
        } else {
            log.error(" === 未知数据类型,已经忽略:{}", msg);
            try {
                channel.basicAck(deliveryTag, false);
            } catch (IOException e) {
                log.error("mq手动ack异常 [{}]", e.getMessage(), e);
            }
            return;
        }

        System.out.println(json.containsKey("msgType"));
        System.out.println(json.containsKey("data"));
        if (!json.containsKey("msgType") || !json.containsKey("data")) {
            log.error("mq接收的数据格式错误，记录忽略:{}", json);
            log.error(" === 未知数据类型,已经忽略:{}", msg);
            try {
                channel.basicAck(deliveryTag, false);
            } catch (IOException e) {
                log.error("mq手动ack异常 [{}]", e.getMessage(), e);
            }
            return;
        }
        //获取所标记的名称
        String msgType = json.get("msgType").toString();

         json = (JSONObject) JSONObject.parse(((Message) msg).getBody());
        Fragment fragment = json.getObject("data", Fragment.class);
        log.info("获得的对象为：[{}]", fragment);
        log.info("Type : [{}]", msgType);

        //判断类型传入消息的类型
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                log.error("mq手动ack异常 [{}]", e.getMessage(), e);
            }
            log.error("mq手动ack异常 [{}]", e.getMessage(), e);

        }
    }

}