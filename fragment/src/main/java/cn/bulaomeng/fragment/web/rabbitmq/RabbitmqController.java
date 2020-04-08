package cn.bulaomeng.fragment.web.rabbitmq;

import cn.bulaomeng.fragment.service.rabbitmq.MqSendService;
import io.swagger.annotations.Api;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/rabbitmq")
@RestController
//@Api(description = "rabbitmq接口")
public class RabbitmqController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MqSendService sendService;

    //@PostMapping("/test")
    public void testMq(){
        Map<String,Object> map = new HashMap<>();
        map.put("userId",1);
        map.put("userName","加油");
        sendService.sendMsg("fragment",map);
    }
}
