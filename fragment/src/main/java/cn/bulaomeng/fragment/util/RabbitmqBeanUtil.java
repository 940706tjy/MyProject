package cn.bulaomeng.fragment.util;

import cn.bulaomeng.fragment.config.rabbitmq.RabbitmqParam;
import cn.bulaomeng.fragment.service.rabbitmq.RabbitmqService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: tjy
 * @Create: 2019-05-15 09:43
 **/
@Component
public class RabbitmqBeanUtil {

    @Autowired
    private RabbitmqService rabbitmqService;

    public void sendMessage(RabbitmqParam param, String message) {
        RabbitTemplate template = rabbitmqService.getSender(param);
        template.convertAndSend(param.getExchange(), param.getRoutingKey(), message);
    }
}
