package cn.bulaomeng.fragment.service.rabbitmq;

import cn.bulaomeng.fragment.config.rabbitmq.RabbitmqParam;
import cn.bulaomeng.fragment.util.RabbitmqBeanUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: tjy
 * @Create: 2019-05-15 09:33
 **/
@Service
public class MqSendService {

    @Autowired
    private RabbitmqParam rabbitmqParam;

    @Autowired
    private RabbitmqService rabbitmqService;

    @Autowired
    private RabbitmqBeanUtil rabbitmqBeanUtil;

    //发送消息
    public void sendMsg(String msgType,Object T){
        rabbitmqService.publishTopic(rabbitmqParam);
        rabbitmqBeanUtil.sendMessage(rabbitmqParam, dataToClientMQ(msgType, T));
    }

    //数据json序列化
    protected String dataToClientMQ(String msgType, Object data) {
        Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("msgType", msgType);
        infoMap.put("data", data);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
        return gson.toJson(infoMap);
    }
}
