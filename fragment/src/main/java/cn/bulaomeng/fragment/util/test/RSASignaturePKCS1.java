package cn.bulaomeng.fragment.util.test;

/**
 * Created by lxy on 2017/10/25.
 */


import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * RSA签名验签类
 */
public class RSASignaturePKCS1 {

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
            byte[] keyByte = Base64Decoder.decode(keycode);
            KeyFactory keyfactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec encoderule = new PKCS8EncodedKeySpec(keyByte);
            PrivateKey privatekey = keyfactory.generatePrivate(encoderule);

            //用私钥给入参加签
            Signature sign = Signature.getInstance("SHA1WithRSA");
            sign.initSign(privatekey);
            sign.update(param.getBytes());

            signature = sign.sign();

        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        //将加签后的入参转成16进制
        String terminal = Hex.encodeHexString(signature);
        return Base64Encoder.encode(signature);
    }

    public static String sign1(String in, PrivateKey privateKey) throws Exception {
        Signature signa = Signature.getInstance("SHA1WithRSA");
        //Signature signa = Signature.getInstance("MD5WithRSA");
        signa.initSign(privateKey);
        signa.update(in.getBytes());
        byte[] signdata = signa.sign();
        return org.apache.commons.codec.binary.Base64.encodeBase64String(signdata);
    }

    /*
     * SHA1WithRSA验签
     */
    public static boolean isValid1(String in, String signData, PublicKey publicKey) throws Exception {
        Signature signa = Signature.getInstance("SHA1WithRSA");
        //Signature signa = Signature.getInstance("MD5WithRSA");
        signa.initVerify(publicKey);
        signa.update(in.getBytes());

        byte[] sign_byte = org.apache.commons.codec.binary.Base64.decodeBase64(signData);
        boolean flag = signa.verify(sign_byte);
        return flag;
    }

    public static boolean verifyWhole(String param, String signature, String keycode) {
        try {
            //获取公钥
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] keyByte = cn.hutool.core.codec.Base64.decode(keycode);
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

        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeySpecException | InvalidKeyException e) {
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

        String privateKey = "MIIBOwIBAAJBANKryRATx3iBDQNZR0NvA3UGTLxDaGf0DDQQ4/wECfbEbXGWKKDq\n" +
                "iOKeWvIBJ+yRc1a+Mm2no8CU2GMnid+s2aECAwEAAQJAP/Z8U29N7t5vzwZ6RpUc\n" +
                "HZnODpMAzh8g3WYLcmbfh5ZLBlC7cK7deXpCNN99RM8q+h0cL/jrTfyMQjbRva92\n" +
                "iQIhAOnWOVpsysKyr5N2h8q1zvzF1eNxnL+A+CwGu10/7zWrAiEA5qN4kpeFGgp4\n" +
                "s+LkTSDHj4jaOswd4/rB03Zy3XFHyeMCIQDfIRPJPVadQnRXehtJSwMgIIdgaODx\n" +
                "u9cT67iN2pwf0QIhAIbm5PZxc+P4bgNh2gGXA8Lb3DF6BJ29pTOH28XUpZXbAiA4\n" +
                "Fq4J9sOPQdN8wLBHqO8sFz4jZUe8mRZdlBvfNurUUA==";

        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANKryRATx3iBDQNZR0NvA3UGTLxDaGf0\n" +
                "DDQQ4/wECfbEbXGWKKDqiOKeWvIBJ+yRc1a+Mm2no8CU2GMnid+s2aECAwEAAQ==";

        String pkcs8 = RsaPkcsTransformer.formatPkcs1ToPkcs8(privateKey);
        System.out.println(pkcs8);
        System.out.println("============================================");


        Map<String, Object> map = new LinkedHashMap<>(2);
        Map<String, Object> requestMap = new LinkedHashMap<>(6);
        requestMap.put("EqId", "WellDevice001");
        requestMap.put("time", "2019-04-01 15:00:06");
        requestMap.put("SysVer", "");
        map.put("app_key", "WellPub");
        map.put("format", "json");
        map.put("request", JSONObject.toJSONString(requestMap));
        map.put("method", "Heart");
        map.put("sign_method", "rsa");
        StringBuilder builder = new StringBuilder();

        map.forEach((k, v) -> builder.append(k).append(v));

        System.out.println(builder.toString());

        System.out.println("===================== 拼装数据 =======================");

        // 生成PrivateKey（前提对PKCS1变换为PKCS8）
        byte[] keyByte = cn.hutool.core.codec.Base64.decode(pkcs8);
        KeyFactory keyfactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec encodeRule = new PKCS8EncodedKeySpec(keyByte);
        PrivateKey privatekey = keyfactory.generatePrivate(encodeRule);

        System.out.println("===================== 签名返回 =======================");


        String pri = sign1(builder.toString(), privatekey);
        System.out.println(pri);
        //获取privatekey

        System.out.println("===================== 验签返回 =======================");

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] pubKeyByte = cn.hutool.core.codec.Base64.decode(publicKey);
        X509EncodedKeySpec pubEncodeRule = new X509EncodedKeySpec(pubKeyByte);
        PublicKey pubKey = keyFactory.generatePublic(pubEncodeRule);
        System.out.println(isValid1(builder.toString(), pri, pubKey));

        // System.out.println(verifyWhole(str, pri, publicKey));

        //   DigitalSignatureHelper.sign(str,)


    }

}
