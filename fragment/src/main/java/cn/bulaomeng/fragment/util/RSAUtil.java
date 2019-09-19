package cn.bulaomeng.fragment.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtil {
    //base64 解码
    public static byte[] decode(byte[] bytes) {
        return Base64.decodeBase64(bytes);
    }

    //base64 编码
    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
    public static void main(String[] args) throws Exception {
        //生成公钥和私钥
        genKeyPair();
        //加密字符串
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAObmFl6JiP3ea9SUJtROpPvdmKjJGG0l2YgLZN3HO0pa3g4kqBYs6IAN9Zjil6K92D3Siq55ujkqESjE31+fNUMCAwEAAQ==";
        String message = "k2n/+6HCVbDCpgEsA+oqCbpqYikBZ39qnq1AG0m9zfhVHK7itN+1h6rlJzb1Zyf067lfnVohYRKkVnVasUzm/A==";
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEn = encrypt(message,keyMap.get(0));
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn,keyMap.get(1));
        System.out.println("还原后的字符串为:" + messageDe);

        byte[] decoded = Base64.decodeBase64(publicKey);
        byte[] inputByte = Base64.decodeBase64(message.getBytes("UTF-8"));
        KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(decoded);
        PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
        Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        System.out.println("解密过后的byte数组："+new String(cipher.doFinal(inputByte),"UTF-8"));
        //把解密后的byte数组使用UTF_8编码转换为字符串。使用逗号分割符分割字符串为四部分，分别得到学工号、姓名、性别和字符串编码表示的二进制内容。
        String deStr[] = new String(cipher.doFinal(inputByte),"UTF-8").split(",");
        for (String str:deStr){
            System.out.println(str);
        }
        //
    }

    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0,publicKeyString);  //0表示公钥
        keyMap.put(1,privateKeyString);  //1表示私钥
    }
    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception{
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }
    /** 
    * @Description: RSA公钥解密
    * @Param: [str, privateKey] 
    * @return: byte[] 
    * @Author: tjy
    * @Date: 2019/9/19 
    */ 
    public static byte[] publicDecrypt(String message, String publicKey){
        try {
            byte[] decoded = Base64.decodeBase64(publicKey);
            byte[] inputByte = Base64.decodeBase64(message.getBytes("UTF-8"));
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(decoded);
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance(mykeyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(inputByte);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    } 

}
