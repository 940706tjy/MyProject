package cn.bulaomeng.fragment.restTmplate;

import cn.bulaomeng.fragment.service.InputParam;
import cn.bulaomeng.fragment.util.DeCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class TxCode {
    public static void main(String[] args) {
        //2.生成随机数
        String random = DeCodeUtil.getGUID();
        System.out.println(random);
        String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        System.out.println(uuid);
        //签名算法
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        String appId = "LibraryAccessControl";
        String name ="test";
        //需要提供
        String key="ryAJCLlelunLLb02rt4V7xUaH6ScZn1l";
        parameters.put("appId", appId);
        parameters.put("name", name);
        parameters.put("nonceStr",uuid);
        String mySign = DeCodeUtil.createSign(parameters,key);
        System.out.println("我 的签名是："+mySign);

        Map<String,Object> map = new HashMap<>();
        String appKey = "0FE8C24D97E240CD";
        String timestamp = Long.toString(System.currentTimeMillis());
        System.out.println(timestamp);
        map.put("app_key","");
        map.put("timestamp","");
        map.put("nonce","");
        map.put("school_code","");
        map.put("signature","");
        RestTemplate restTemplate = new RestTemplate();
        JSONObject js = restTemplate.getForObject("https://weixiao.qq.com/apps/school-api/key-secret",JSONObject.class,map);

    }
}
