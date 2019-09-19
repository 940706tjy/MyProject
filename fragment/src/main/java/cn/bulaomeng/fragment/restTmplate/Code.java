package cn.bulaomeng.fragment.restTmplate;

import cn.bulaomeng.fragment.util.Md5;
import cn.bulaomeng.fragment.util.RSAUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class Code {
//http://paydev.greatge.net/codeonly/getqrcodeconfig.html
    //http://paydev.greatge.net/identitydocs/qrcode/decodealgorithm.html
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
        //System.out.println(mySign.get("sign"));
        //getCodeCofing(map);
        Map<String,Object> signMap = new HashMap<>();
        signMap.put("keyId","3aqwiuh123");
        signMap.put("nonceStr","5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
        signMap.put("nonceStrCipherText","abcdefg");
        //goSign(signMap);
        decode();
    }
    //获得二维码配置接口
    public  static  JSONObject getCodeCofing(Map<String,Object> map){
        RestTemplate restTemplate = new RestTemplate();
        String data=restTemplate.postForObject("http://demo.greatge.net:28981/code/getqrcodeconfig",map,String.class);
        System.out.println(data);
        return null;
    }
    //签到
    public  static  JSONObject goSign (Map<String,Object> map){
        RestTemplate restTemplate = new RestTemplate();
        String data=restTemplate.postForObject("http://demo.greatge.net:28981/checkin",map,String.class);
        System.out.println("签到:---"+data);
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

    public static void decode(){
        //公钥
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAObmFl6JiP3ea9SUJtROpPvdmKjJGG0l2YgLZN3HO0pa3g4kqBYs6IAN9Zjil6K92D3Siq55ujkqESjE31+fNUMCAwEAAQ==";
        //二维码
        String code = "RFT://k2n/+6HCVbDCpgEsA+oqCbpqYikBZ39qnq1AG0m9zfhVHK7itN+1h6rlJzb1Zyf067lfnVohYRKkVnVasUzm/A==|ionGGcU+zaOgkeP8mqlGrVXUtydb35qLh08+mTI/zZM=";
        //配置接口协议头
        String protocolHeader = "RFT://";

        //1.根据配置接口获取的协议头判断是否系统发放的码。
        String isHeader = code.substring(0,protocolHeader.length());
        if(protocolHeader.equals(isHeader)){
            System.out.println(""+"匹配协议头");
            //2.去除协议头
            code = code.substring(isHeader.length());
            System.out.println(code);
            //3.使用竖线“|”分割字符串，得到第一部分字符串为二维码编码字符串。
            String splitCode[] = code.split("\\|");
            System.out.println(splitCode[0]);
            /*
            4.使用AES算法解密“时间字符串”，密钥固定为“GreatgeRonfton81”算法使用“AES/CBC/PKCS7Padding”，向量为密钥的前16byte。解密后得到一个逗号分割的字符串，如：9,100000,7000,0,103717。最后一部分“103717”
            为二维码在小程序上展示的时间，通过与当前的时间进行比对，如果两者相差超过二维码配置接口返回的“displayLimitTime”的秒数，则认为二维码已经失效，程序应该提示用户二维码已经失效
             */

            //5.根据当前时间获取当前周期，进而获取当前周期的公钥

            //6.加载公钥，初始化环境。密钥长度为512bit，使用的padding为“RSA/ECB/PKCS1Padding”

            //7.对二维码字符串进行base64解码，得到byte数组。
            String byteStr = splitCode[0];
            //解码
            byte decode[] = Base64.decodeBase64(byteStr.getBytes());
            System.out.println(byteStr + "\t字符串解码后为：" + decode);
            /*
            8.使用公钥对byte数组解密，得到解密后的byte数组。如果解密失败，则根据风控允许的误差时间（参考配置接口获取）
            加载前一周期或者下一周期的公钥再尝试解密。注意，如果base64解码后得到的byte数组的比特长度超过密钥的长度时，需要分片进行解密，然后再顺序拼接成一个byte数组
             */
            try {
                byte[] deByteArr = RSAUtil.publicDecrypt(byteStr,publicKey);
                String deStr = new String(deByteArr,"UTF-8");
                System.out.println("解密过后的byte数组转为utf-8形式的字符串:"+"\n"+deStr);
                //9.把解密后的byte数组使用UTF_8编码转换为字符串。使用逗号分割符分割字符串为四部分，分别得到学工号、姓名、性别和字符串编码表示的二进制内容。
                String deSplitStr[] = deStr.split(",");
                //10.把字符串编码表示的二进制内容使用ISO_8859_1编码转换为byte数组，按照下面的分割方式获取对应的数据
                byte isoByteStr[] =deSplitStr [deSplitStr.length-1].getBytes("ISO_8859_1");
                for (byte b : isoByteStr){
                    System.out.println(b);
                }
                System.out.println("============================================================分割线===========================================================");
                //https://blog.csdn.net/u010277446/article/details/52459613
                for (byte b : subBytes(isoByteStr,isoByteStr.length-7,2)){
                    System.out.println(b);
                }
                System.out.println(bytes2Int(subBytes(isoByteStr,isoByteStr.length-7,2)));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    /**
    * @Description: 将解密后的字节数组按要求截取
    * @Param:
    * @return:
    * @Author: tjy
    * @Date: 2019/9/19
    */
    public static byte[] subBytes(byte[] src, int srcPos, int length) {
        byte[] bs = new byte[length];
        System.arraycopy(src, srcPos, bs, 0, length);
        return bs;
    }
    public static int bytes2Int(byte[] bytes) {
        int result = 0;
        //将每个byte依次搬运到int相应的位置
        result = bytes[0] & 0xff;
        for (int i=1;i<bytes.length;i++){
            result = result << 8 | bytes[i] & 0xff;
        }
        return result;
    }

    public static String conver2HexStr(byte [] b)
    {
        StringBuffer result = new StringBuffer();
        for(int i = 0;i<b.length;i++)
        {
            result.append(Long.toString(b[i] & 0xff, 2)+",");
        }
        return result.toString().substring(0, result.length()-1);
    }
}
