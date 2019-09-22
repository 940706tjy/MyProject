package cn.bulaomeng.fragment.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.*;
import java.util.Date;

/*
     AES/CBC/PKCS7Padding 加/解密工具（128）
 */
public class AESPKCS7Util {
    public static void main(String[] args) throws Exception {
        //原始数据
        String data = "9,100000,7000,0,103312";
        //密钥
        String sessionKey = "GreatgeRonfton81";
        //向量
        String iv = "GreatgeRonfton81";

        //用Base64编码
        Base64.Encoder encoder = Base64.getEncoder();
        String baseData = encoder.encodeToString(data.getBytes());
        String baseSessionKey = encoder.encodeToString(sessionKey.getBytes());
        String baseIv = encoder.encodeToString(iv.getBytes());

        //导入支持AES/CBC/PKCS7Padding的Provider
        Security.addProvider(new BouncyCastleProvider());

        //获取加密数据
        String encryptedData = encrypt(baseData,baseSessionKey,baseIv);
        //通过加密数据获得原始数据
        System.out.println(encryptedData);
        String dataReborn = decrypt(encryptedData,baseSessionKey,baseIv);

        //打印解密出来的原始数据
        System.out.println(dataReborn);
        String getSpStr[] = dataReborn.split(",");
        String getDate = getSpStr[getSpStr.length-1];
        System.out.println("时间：----"+getDate);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String dateString = sdf.format(date);
        System.out.println(dateString);
    }
    /**
    * @Description: 加密
    * @Param: [data, sessionKey, iv]
    * @return: java.lang.String
    * @Author: tjy
    * @Date: 2019/9/22
    */
    public static String encrypt(String data,String sessionKey,String iv) throws Exception {

        //加密之前，先从Base64格式还原到原始格式
        Decoder decoder = Base64.getDecoder();
        byte[] dataByte = decoder.decode(data);
        byte[] keyByte = decoder.decode(sessionKey);
        byte[] ivByte = decoder.decode(iv);

        String encryptedData = null;

        //指定算法，模式，填充方式，创建一个Cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");

        //生成Key对象
        Key sKeySpec = new SecretKeySpec(keyByte, "AES");

        //把向量初始化到算法参数
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(ivByte));

        //指定用途，密钥，参数 初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, params);

        //指定加密
        byte[] result = cipher.doFinal(dataByte);

        //对结果进行Base64编码，否则会得到一串乱码，不便于后续操作
        Base64.Encoder encoder = Base64.getEncoder();
        encryptedData = encoder.encodeToString(result);


        return encryptedData;
    }
    /** 
    * @Description: 解密 
    * @Param: [encryptedData, sessionKey, iv] 
    * @return: java.lang.String 
    * @Author: tjy
    * @Date: 2019/9/22 
    */ 
    public static String decrypt(String encryptedData,String sessionKey,String iv) throws Exception {
        //解密之前先把Base64格式的数据转成原始格式
        Decoder decoder = Base64.getDecoder();
        byte[] dataByte = decoder.decode(encryptedData);
        byte[] keyByte = decoder.decode(sessionKey);
        byte[] ivByte = decoder.decode(iv);

        String data = null;

        //指定算法，模式，填充方法 创建一个Cipher实例
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");

        //生成Key对象
        Key sKeySpec = new SecretKeySpec(keyByte, "AES");

        //把向量初始化到算法参数
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(ivByte));

        //指定用途，密钥，参数 初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, params);

        //执行解密
        byte[] result = cipher.doFinal(dataByte);

        //解密后转成字符串
        data = new String(result);

        return data;
    }
}
