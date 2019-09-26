package cn.bulaomeng.fragment.service;

import cn.bulaomeng.fragment.config.HttpsClientRequestFactory;
import cn.bulaomeng.fragment.entity.TxwxKeySecret;
import cn.bulaomeng.fragment.mapper.TxwxKeySecretMapper;
import cn.bulaomeng.fragment.util.DeCodeUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@Configuration
@EnableScheduling
@Service
public class TimingGeTConfig {

    @Autowired
    private TxwxKeySecretMapper txwxKeySecretMapper;
    /** 
    * @Description: 每天凌晨1点定时拉取配置
    * @Param: [] 
    * @return: void 
    * @Author: tjy
    * @Date: 2019/9/26 
    */ 
    @Scheduled(cron = "0 0 1 * * ?")
    public void TimingGetConfig(){
        String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        String app_key = "0FE8C24D97E240CD"; //服务商id
        String signature; //签名
        String timestamp = String.valueOf(System.currentTimeMillis()/1000) ; //当前时间戳
        String nonce = uuid;   //随机字符串
        String school_code = "wxcampus"; //学校代码
        String key="59C563A47F92D78076B71FE29F1BA209";

        //
        SortedMap<Object,Object> parameters = new TreeMap<>();
        parameters.put("app_key", app_key);
        parameters.put("timestamp",timestamp);
        parameters.put("nonce",nonce);
        parameters.put("school_code",school_code);
        signature = DeCodeUtil.createSign(parameters,key);

        Map<String,Object> map = new HashMap<>();
        map.put("app_key",app_key);
        map.put("timestamp",timestamp);
        map.put("nonce",nonce);
        map.put("school_code",school_code);
        map.put("signature",signature);
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        JSONObject js = restTemplate.getForObject("https://weixiao.qq.com/apps/school-api/key-secret?app_key={app_key}&timestamp={timestamp}&nonce={nonce}&school_code={school_code}&signature={signature}",JSONObject.class,map);
        TxwxKeySecret wxKeySecret = new TxwxKeySecret();
        wxKeySecret.setSecret(js.getString("secret"));
        wxKeySecret.setPublicKey(deleteLine(js.getString("public_key")));
        wxKeySecret.setPrevPublic(deleteLine(js.getString("prev_public")));
        wxKeySecret.setNextPublic(deleteLine(js.getString("next_public")));
        wxKeySecret.setRule(js.getJSONObject("rule").toJSONString());
        wxKeySecret.setNextRule(js.getJSONObject("next_rule").toJSONString());
        wxKeySecret.setPrevRule(js.getJSONObject("prev_rule").toJSONString());
        //写库
        //先删除库中所有数据
        txwxKeySecretMapper.deleteAll();
        //在插入
        txwxKeySecretMapper.insertSelective(wxKeySecret);
    }
    /**
     * @Description: 去掉换行及begin/end
     * @Param: [str]
     * @return: java.lang.String
     * @Author: tjy
     * @Date: 2019/9/26
     */
    public  String deleteLine(String str){
        return str.replaceAll("\r|\n","").replaceAll("-----BEGIN PUBLIC KEY-----","").replaceAll("-----END PUBLIC KEY-----","");
    }
}
