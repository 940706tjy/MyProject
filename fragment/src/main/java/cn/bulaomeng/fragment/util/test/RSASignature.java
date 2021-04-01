package cn.bulaomeng.fragment.util.test;

/**
 * Created by lxy on 2017/10/25.
 */


import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * RSA签名验签类
 */
public class RSASignature {

    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 商户私钥
     * @param encode     字符集编码
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String encode) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Decoder.decode(privateKey));

            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(encode));

            byte[] signed = signature.sign();

            return Base64Encoder.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Decoder.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes("utf-8"));
            byte[] signed = signature.sign();
            //Log.i("111111---", "----"+bytesToHexString(signed));
            return bytesToHexString(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @param encode    字符集编码
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey, String encode) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64Decoder.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(encode));


            boolean bverify = signature.verify(Base64Decoder.decode(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64Decoder.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            java.security.Signature signature = java.security.Signature
                    .getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes());

            boolean bverify = signature.verify(Base64Decoder.decode(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String signWhole(String keycode, String param) throws InvalidKeySpecException {
        // 使用私钥加签
        byte[] signature = null;
        try {
            //获取privatekey
            byte[] keyByte = Base64.decode(keycode);
            KeyFactory keyfactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec encoderule = new PKCS8EncodedKeySpec(keyByte);
            PrivateKey privatekey = keyfactory.generatePrivate(encoderule);

            //用私钥给入参加签
            Signature sign = Signature.getInstance("SHA1WithRSA");
            sign.initSign(privatekey);
            sign.update(param.getBytes());

            signature = sign.sign();

        } catch (NoSuchAlgorithmException | Base64DecodingException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        //将加签后的入参转成16进制
        String terminal = Hex.encodeHexString(signature);
        return Base64.encode(signature);
    }

    public static boolean verifyWhole(String param, String signature, String keycode) {
        try {
            //获取公钥
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] keyByte = Base64.decode(keycode);
            X509EncodedKeySpec encodeRule = new X509EncodedKeySpec(keyByte);
            PublicKey publicKey = keyFactory.generatePublic(encodeRule);

            //用获取到的公钥对   入参中未加签参数param 与  入参中的加签之后的参数signature 进行验签
            Signature sign = Signature.getInstance("SHA1WithRSA");
            sign.initVerify(publicKey);
            sign.update(param.getBytes());

            //将16进制码转成字符数组
            byte[] hexByte = hexStringToBytes(signature);
            //验证签名
            return sign.verify(hexByte);

        } catch (NoSuchAlgorithmException | Base64DecodingException | SignatureException | InvalidKeySpecException | InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将Hex String转换为Byte数组
     *
     * @param hexString the hex string
     * @return the byte [ ]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (StringUtils.isEmpty(hexString)) {
            return null;
        }
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() >> 1];
        int index = 0;
        for (int i = 0; i < hexString.length(); i++) {
            if (index > hexString.length() - 1) {
                return byteArray;
            }
            byte highDit = (byte) (Character.digit(hexString.charAt(index), 16) & 0xFF);
            byte lowDit = (byte) (Character.digit(hexString.charAt(index + 1), 16) & 0xFF);
            byteArray[i] = (byte) (highDit << 4 | lowDit);
            index += 2;
        }
        return byteArray;
    }

    public static void main(String[] args) throws Exception {
        String str = "app_keyAdcformatjsonrequest{\"EqId\":\"Mark0001\",\"time\":\"2019-06-04 15:21:06\"}methodHeartsign_methodrsa";
        String pub = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDaKAUkOn2Gpgid1nouXNf66lqawyY5NjCMi+EdYcpbbnJ/fOeh/EFOF0vvMbKtnH8/S5KefbiAOpui9Zu/kO8HMvvveHX1lrX8lfZ5BW7Vz76004zH+GIUUs1+7JG5z2xikTein3WmsAu3+mNmq2BaQ+RQ1lns+Gbm7ECJOcysvwIDAQAB";
        String sign = "beN0Iu7N7E+taZa7b/tqsYr0wMmAtiV+dJAGfT7oIiV49qImHpmqkdoBbqKHDoKghXRTfTrt2vG+mjte9qMvXgPn401Ou0WxMOxDEDCj96e9c5aecVMPm3deM2R5n1rOqMcpoKjpiZ3ZEapjTeuBjxmqYIq4c6Vv0mut4SG8eSg=";
        // System.out.println("md5:" + doCheck(str, sign, pub));

        String privateKey = "MIIBOQIBAAJBAIajMDMXjrUEAEoXhIBaLUwN5wRZwwo0enz+W2xyxmxV/XMDxCdx\n" +
                "hs3ldP2Y9VEgX9Ya4pX+RHD4ZAg/zfHNp4kCAwEAAQJABJduHl/wSvYO+Hfi2em5\n" +
                "X72dokVZZHCzuK2H+qPTZoBfipRWKMKfJjnFIXnOnAVQ73JA+JLanhD3Ziu4dXMt\n" +
                "HQIhAMZZrRSTN1F8V2YqqYtQUYOQSSP5vdFjBoL6RQBllQpvAiEArcTvg5WDLnas\n" +
                "8XkkUoq5cjwPxziBmgH3NZIIPyc8yYcCIC3exzwe34gkrUGfVB43sOIwF/oN42vs\n" +
                "7rCTmCmDVHhZAiAlUcVK0kJxKSWtWqIVZBu9z+5nj9gttjxccfd5cLjaYwIgbLMv\n" +
                "zPWjFIITNTm6MnyH8a2fP/jG/JXzFIeBVSh/J2E=";

        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIajMDMXjrUEAEoXhIBaLUwN5wRZwwo0\n" +
                "enz+W2xyxmxV/XMDxCdxhs3ldP2Y9VEgX9Ya4pX+RHD4ZAg/zfHNp4kCAwEAAQ==";

//        String pkcs1 = RsaPkcsTransformer.formatPkcs1ToPkcs8(privateKey);
//
//   //     pkcs1 = pkcs1.replaceAll("-----BEGIN RSA PRIVATE KEY-----", "").replaceAll("-----END RSA PRIVATE KEY-----", "");
//        System.out.println(pkcs1);
//        System.out.println(sign(pkcs1, privateKey));
        String privatestr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANlx8rhWlKYH54BaTuL8jRuXtssu7zr04O6KvZDTzIycvArF3ohMUcgFIQ4a+JlvN5S1sokmxLTKPr4xrS6xRnaUvIW1qXh5SXvwpBEHPpCoHXqP5zNMftxA2MH4ktPfKBtWpoKLP2DsJ4EpGRr34wP3CrchYbjlYyGdY5lvMcbHAgMBAAECgYAvzDJ0fuOyE2658iABGU7TT+gohaqkpQuEpA7DdSszhYh4PcKK52vasfXwKdGXuLDZCY+zQkhfDU35dOYCq4k3Q84cjiLVe5sFlJNucBgqlEcxrLsUkTuXzNntOWjTpWjAnfUXB7GkvT7ekpnCwqWb8qlUGHzsGioTkTOy0cfIUQJBAPuQmI0EpORqP+v/Vd6SWlxq6Be20hsT5erVpz2Ke1KczbQwRi2ogBpYRMaAmT4kWfsl2hU/hbmESG/yymWS+BMCQQDdR1uCJXSJqu7Urlg4zCa4kBk0rwYYttLJj1jL6d5kKawf9iEgoAElVlRZWWXRnyD4Nesf8n5kTlgtawz+jnT9AkEAi1aP6KwF2S6wsTsAiQNvYXkljN0Ki0z+MJCezYuCu0N2/LMwa+HE8tKpZXmdZ7oizOUuYk6I9zS6GqfUS2aYWQJBAJMjzxK0y1B77IJaSGnEPv89OrWQqNIoR/QlsNsvcWVTXJSIOzERlJF6XW5ohs8kLG1AlU/SFP+oJPRWmfZvThUCQDbM7X6PF+DHa9x2YPEWYYiiThbTGTO1mOxBt2V6GuFqnGHFIa3OeZZpv7SW+aDzTGEmrtByqxRkSd3WpeJzu0I=";
        String publicstr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjyiajomfYD80A7tN8vdeXllTiGrSdocq1nvgceicanNb8QaoNGdAPE6AMuSqnMWs40tj/XoXQmPxNrdUmclwwLJza5Aq5PNqDiFC5QLmIFtATN/n3ymqIYnw78ME8Dv5yjYJs1xk0EL6+1wlFFrylApBWKUGE2c2m2seBY+in5wIDAQAB";
        String pkcs1 = RsaPkcsTransformer.formatPkcs1ToPkcs8(privateKey);
        System.out.println(pkcs1);
        String pri = signWhole(pkcs1, str);
        System.out.println(pri);
       // System.out.println(verifyWhole(str, pri, publicKey));

     //   DigitalSignatureHelper.sign(str,)


    }

}
