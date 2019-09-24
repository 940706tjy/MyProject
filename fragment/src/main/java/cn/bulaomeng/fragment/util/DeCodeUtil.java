package cn.bulaomeng.fragment.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

public class DeCodeUtil {

    /** 
    * @Description: 签名算法
    * @Param: [parameters, key] 
    * @return: java.util.Map<java.lang.String,java.lang.Object> 
    * @Author: tjy
    * @Date: 2019/9/20 
    */ 
    public static String createSign(SortedMap<Object,Object> parameters, String key){
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
        return sign;
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
    /** 
    * @Description: byte数组转int类型 
    * @Param: [bytes] 
    * @return: int 
    * @Author: tjy
    * @Date: 2019/9/20 
    */ 
    public static int bytes2Int(byte[] bytes) {
        int result = 0;
        //将每个byte依次搬运到int相应的位置
        result = bytes[0] & 0xff;
        for (int i=1;i<bytes.length;i++){
            result = result << 8 | bytes[i] & 0xff;
        }
        return result;
    }
    /**
     * @Description: 将byte数组转为对应二进制并拼接
     * @Param: [bytes]
     * @return: java.lang.String
     * @Author: tjy
     * @Date: 2019/9/20
     */
    public static String bytesChar(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        for(byte b:bytes){
            sb.append((char)b);
        }
        return sb.toString();
    }

    /*
    * @Description: MD5解密工具
    * @Param:  
    * @return:  
    * @Author: tjy
    * @Date: 2019/9/22 
    */ 
    
    //16进制数字数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"};

    //将byte数组转换成16进制字符串
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    //将byte转为16进制字符串
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String md5Encrypt3(String src){
        String resultString = null;
        try {
            resultString = src;
            //获取md5算法
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密，并转换成16进制字符串
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    //可以选择指定编码
    public static String md5Encrypt4(String src, String charsetname){
        String resultString = null;
        try {
            resultString = src;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname)){
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            } else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
//生成不重复16位随机数
    public static String getGUID() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type){
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char)(rd.nextInt(25)+65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char)(rd.nextInt(25)+97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

}
