package cn.bulaomeng.fragment.restTmplate;

import cn.bulaomeng.fragment.config.HttpsClientRequestFactory;
import cn.bulaomeng.fragment.entity.TxwxKeySecret;
import cn.bulaomeng.fragment.entity.User;
import cn.bulaomeng.fragment.util.DeCodeUtil;
import com.alibaba.fastjson.JSONObject;
import com.qq.weixiao.wxcode.CampusCode;
import org.springframework.web.client.RestTemplate;


import java.util.*;

public class TxCode {
    public static void main(String[] args) {
        //2.生成随机数
        String random = DeCodeUtil.getGUID();
        System.out.println(random);
        String uuid = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
  /*      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date =new Date();
        String currentDate = sdf.format(date);*/
        String app_key = "0FE8C24D97E240CD"; //服务商id
        String signature; //签名
        String timestamp = String.valueOf(System.currentTimeMillis()/1000) ; //当前时间戳
        String nonce = uuid;   //随机字符串
        System.out.println(nonce);
        String school_code = "wxcampus"; //学校代码
        String key="59C563A47F92D78076B71FE29F1BA209";

        //签名算法
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("app_key", app_key);
        parameters.put("timestamp",timestamp);
        parameters.put("nonce",nonce);
        parameters.put("school_code",school_code);
        signature = DeCodeUtil.createSign(parameters,key);
        System.out.println("我的签名是："+signature);

        Map<String,Object> map = new HashMap<>();
        System.out.println(timestamp);
        map.put("app_key",app_key);
        map.put("timestamp",timestamp);
        map.put("nonce",nonce);
        map.put("school_code",school_code);
        map.put("signature",signature);
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        JSONObject js = restTemplate.getForObject("https://weixiao.qq.com/apps/school-api/key-secret?app_key={app_key}&timestamp={timestamp}&nonce={nonce}&school_code={school_code}&signature={signature}",JSONObject.class,map);
        System.out.println(js);
        TxwxKeySecret wxKeySecret = new TxwxKeySecret();
        wxKeySecret.setSecret(js.getString("secret"));
        wxKeySecret.setPublicKey(deleteLine(js.getString("public_key")));
        wxKeySecret.setPrevPublic(deleteLine(js.getString("prev_public")));
        wxKeySecret.setNextPublic(deleteLine(js.getString("next_public")));
        wxKeySecret.setRule(js.getJSONObject("rule").toJSONString());
        wxKeySecret.setNextRule(js.getJSONObject("next_rule").toJSONString());
        wxKeySecret.setPrevRule(js.getJSONObject("prev_rule").toJSONString());
        System.out.println(wxKeySecret);

        /*
            /////////////////////////////////////////////////////////////////////////////////////////////
         */
        System.out.println(wxKeySecret.getPublicKey());
        System.out.println(wxKeySecret.getNextPublic());
        System.out.println(wxKeySecret.getPrevPublic());
        CampusCode campusCode = new CampusCode(wxKeySecret.getPublicKey(), wxKeySecret.getSecret());  //不需要解释规则
        CampusCode campusCode1 = new CampusCode(wxKeySecret.getPrevPublic(), wxKeySecret.getSecret()); //不需要解释规则
        CampusCode campusCode2 = new CampusCode(wxKeySecret.getNextPublic(), wxKeySecret.getSecret()); //不需要解释规则
        String code = "http://wx.url.cn/v002.wxcampus.v4bBue.S7843m3t4ntdeXpoPKXybfdkOwibYNza72js4DcZwrpvafbgxra1j6f7sZviAyO7iO86NQpCYJTF-g_7_uSL6g";
        //在线码
        //campusCode.setClientTimeStamp(Long.parseLong(timestamp));  //调试时候，故意对齐本案例中的码的生成时间，正式上线请删除该行代码
        org.json.simple.JSONObject ret = campusCode.decode(code);
        org.json.simple.JSONObject ret1 = campusCode1.decode(code);
        org.json.simple.JSONObject ret2 = campusCode2.decode(code);
        System.out.println(ret); //{"code":0,"data":{"is_offline":0,"contract_type":"1","school_code":"4144010559","card_number":"00001234","identity_type":"0"},"message":"scuess"}
        System.out.println(ret1); //{"code":0,"data":{"is_offline":0,"contract_type":"1","school_code":"4144010559","card_number":"00001234","identity_type":"0"},"message":"scuess"}
        System.out.println(ret2); //{"code":0,"data":{"is_offline":0,"contract_type":"1","school_code":"4144010559","card_number":"00001234","identity_type":"0"},"message":"scuess"}
        User user = new User();
        //只有code为0，表示能正确解码
        if(Integer.parseInt(ret.get("code").toString()) == 0) {
            org.json.simple.JSONObject data = (  org.json.simple.JSONObject) ret.get("data");
            user.setUserNo(data.get("card_number").toString());
            System.out.println(data.get("card_number")); //学工号，key固定为card_numbe
            System.out.println(data.get("identity_type")); //对应构造方法中，规则解释方法json的字段
            System.out.println(data.get("entrusts")); //对应构造方法中，规则解释方法json的字段
        }else {
            System.out.println(ret.get("code")); //错误码
            System.out.println(ret.get("message"));//错误提示
        }
        if(Integer.parseInt(ret1.get("code").toString()) == 0) {
            org.json.simple.JSONObject data = (  org.json.simple.JSONObject) ret1.get("data");
            user.setUserNo(data.get("card_number").toString());
            System.out.println(data.get("card_number")); //学工号，key固定为card_numbe
            System.out.println(data.get("identity_type")); //对应构造方法中，规则解释方法json的字段
            System.out.println(data.get("entrusts")); //对应构造方法中，规则解释方法json的字段
        }else {
            System.out.println(ret1.get("code")); //错误码
            System.out.println(ret1.get("message"));//错误提示
        }
        if(Integer.parseInt(ret2.get("code").toString()) == 0) {
            org.json.simple.JSONObject data = (  org.json.simple.JSONObject) ret2.get("data");
            user.setUserNo(data.get("card_number").toString());
            System.out.println(data.get("card_number")); //学工号，key固定为card_numbe
            System.out.println(data.get("identity_type")); //对应构造方法中，规则解释方法json的字段
            System.out.println(data.get("entrusts")); //对应构造方法中，规则解释方法json的字段
        }else {
            System.out.println(ret2.get("code")); //错误码
            System.out.println(ret2.get("message"));//错误提示
        }
    }

    /** 
    * @Description: 去掉换行及begin/end
    * @Param: [str] 
    * @return: java.lang.String 
    * @Author: tjy
    * @Date: 2019/9/26 
    */ 
    public static String deleteLine(String str){
        return str.replaceAll("\r|\n","").replaceAll("-----BEGIN PUBLIC KEY-----","").replaceAll("-----END PUBLIC KEY-----","");
    }
}
