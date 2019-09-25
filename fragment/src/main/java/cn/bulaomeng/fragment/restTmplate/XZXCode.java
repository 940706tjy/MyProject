package cn.bulaomeng.fragment.restTmplate;

import cn.bulaomeng.fragment.service.InputParam;
import cn.bulaomeng.fragment.util.DeCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class XZXCode {
    public static void main(String[] args) {
        //2.生成随机数
        String random = DeCodeUtil.getGUID();
        System.out.println(random);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(uuid);
        //签名算法
        //appId：rft123456
        //itemNo： 10000100
        //outTradeNo： 1000
        //body： test
        //nonceStr： ibuaiVcKdpRxkhJA
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        String appId = "LibraryAccessControl";
        String name ="test";
        String key="ryAJCLlelunLLb02rt4V7xUaH6ScZn1l";
        InputParam inputParam = new InputParam();
        inputParam.setName(name);
        inputParam.setAppId(appId);
        parameters.put("data",JSON.toJSONString(inputParam));
        parameters.put("nonceStr",uuid);

        String mySign = DeCodeUtil.createSign(parameters,key);
        System.out.println("我 的签名是："+mySign);
        //map参数中所需的对象

        //RestTemplate中所需的Map参数
        Map<String,Object> map = new HashMap<>();
        System.out.println(JSON.toJSONString(inputParam));
        map.put("data",JSON.toJSONString(inputParam));
        map.put("nonceStr",uuid);
        map.put("localAuthSign",mySign);
        RestTemplate restTemplate = new RestTemplate();
        JSONObject js = restTemplate.postForObject("http://60.205.182.0:9000/sign/createappsign",map, JSONObject.class);
        System.out.println(js);


/*
===============================================================================================================================
 */
     /*   //2.生成随机数
        String random = DeCodeUtil.getGUID();
        System.out.println(random);
        String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        System.out.println(uuid);
        //签名算法
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        String appId = "LibraryAccessControl";
        String name ="test";
        //String nonceStr = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";
        //需要提供
        String key="ryAJCLlelunLLb02rt4V7xUaH6ScZn1l";
        //String key="192006250b4c09247ec02edce69f6a2d";
        parameters.put("appId", appId);
        parameters.put("name", name);
        parameters.put("nonceStr",uuid);
        Map<String,Object> mySign = DeCodeUtil.createSign(parameters,key);

        Map<String,Object> map = new HashMap<>();
        map.put("app_key","");
        map.put("timestamp","");
        map.put("nonce","");
        map.put("school_code","");
        map.put("signature","");
        RestTemplate restTemplate = new RestTemplate();
        JSONObject js = restTemplate.getForObject("https://weixiao.qq.com/apps/school-api/key-secret",JSONObject.class,map);*/
    }
}
