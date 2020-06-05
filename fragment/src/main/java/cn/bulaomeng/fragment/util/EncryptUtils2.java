package cn.bulaomeng.fragment.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;


@Slf4j
public class EncryptUtils2 {


    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return Base64.encode(encryptData);
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText, String secretKey1) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey1.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

        return new String(decryptData, encoding);
    }

    //key1:oBzHvOE3tb17opoIsNMzL4iz
    //key2:WeUqGoZJQ8qS3via9hgegtzX
    // 密钥
    private final static String secretKey = "WeUqGoZJQ8qS3via9hgegtzX";
    private final static String secretKey1 = "oBzHvOE3tb17opoIsNMzL4iz";
    // 向量
    private final static String iv = "00000000";
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";


    public static void main(String[] args) throws Exception {
        String inItStr = "YXY9990SxPjdjRboUG6KjmxhvHdSzH+tKbf4fh1NdJMblmzkZ+epZC7mhYVd/5r/SyIe6p";
        String subStr = inItStr.split("YXY999")[1];
        log.debug(subStr);
        String decodeStr = decode(subStr, secretKey);
        log.info("msg ==>  :[{}]", decodeStr);

        // openid@学校号@时间戳
        String[] spStr = decodeStr.split("@");


        for (String s : spStr) {
            log.info("msgSp ==> :[{}] ", s);
        }
        String decodeOpenid = decode(spStr[0], secretKey1);
        log.info("decodeOpenid ==>  :[{}]", decodeOpenid);


    }
}