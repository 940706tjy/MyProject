package cn.bulaomeng.fragment.restTmplate;

import cn.bulaomeng.fragment.util.Md5;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class Code {
//http://paydev.greatge.net/codeonly/getqrcodeconfig.html
    public static void main(String[] args) {
        //appId：rft123456
        //itemNo： 10000100
        //outTradeNo： 1000
        //body： test
        //nonceStr： ibuaiVcKdpRxkhJA
        //1.签名算法加密
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        String appId="rft123456";
        String itemNo="10000100";
        String outTradeNo="1000";
        String body="test";
        String nonceStr="ibuaiVcKdpRxkhJA";
        //需要提供
        String key="192006250b4c09247ec02edce69f6a2d";
        //parameters.put("mfrchant_id", mfrchant_id);
        parameters.put("appId", appId);
        parameters.put("itemNo", itemNo);
        parameters.put("outTradeNo",outTradeNo);
        parameters.put("body",body);
        parameters.put("nonceStr",nonceStr);
        Map<String,Object> mySign = createSign(parameters,key);
        System.out.println("我 的签名是："+mySign.get("addSign"));
        System.out.println("========================分隔线=============================");
        //2.生成随机数
        String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
        System.out.println(uuid);
        //调用配置接口
        Map<String,Object> map = new HashMap<>();
        map.put("appId","rft123456");
        map.put("nonceStr",uuid);
        map.put("appSign",mySign.get("sign"));
        System.out.println(mySign.get("sign"));
        getCodeCofing(map);
    }

    public  static  JSONObject getCodeCofing(Map<String,Object> map){
        RestTemplate restTemplate = new RestTemplate();
        String data=restTemplate.postForObject("http://demo.greatge.net:28981/code/getqrcodeconfig",map,String.class);
        System.out.println(data);
        return null;
    }

    public static Map<String,Object> createSign(SortedMap<Object,Object> parameters,String key){
        StringBuffer sb = new StringBuffer();
        StringBuffer sbkey = new StringBuffer();
        Set es = parameters.entrySet();  //所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            //空值不传递，不参与签名组串
            if(null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
                sbkey.append(k + "=" + v + "&");
            }
        }
        //System.out.println("字符串:"+sb.toString());
        sbkey=sbkey.append("key="+key);
        System.out.println("字符串:"+sbkey.toString());
        //MD5加密,结果转换为大写字符
        String sign = Md5.getMD5(sbkey.toString()).toUpperCase();
        System.out.println("MD5加密值:"+sign);
        Map<String,Object> map = new HashMap<>();
        map.put("sign",sign);
        map.put("addSign",sb.toString()+"sign="+sign);
        return map;
    }
}
