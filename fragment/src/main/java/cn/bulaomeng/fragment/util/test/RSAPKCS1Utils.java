package cn.bulaomeng.fragment.util.test;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * PKCS1 -> PKCS8 C# 生成密钥对 JAVA进行对接
 * RSA加解密工具类，实现公钥加密私钥解密和私钥解密公钥解密
 */
public class RSAPKCS1Utils {

    private static final String src = "abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) throws Exception {
        System.out.println("\n");
        RSAKeyPair keyPair = generateKeyPair();
        System.out.println("公钥：" + keyPair.getPublicKey());
        System.out.println("私钥：" + keyPair.getPrivateKey());
        System.out.println("\n");
    //    test1(keyPair, src);
        System.out.println("\n");
       // test2(keyPair, src);
        System.out.println("\n");

        String s = encryptByPrivateKeyPKCS1("MIIBOwIBAAJBAN6NUNrcGYqPm4EQ/q/6dzTerulZnagX6gUeMIlMHT2767JMpsTK\n" +
                "aGkYtp8jp3Jk0nyKG/MUZaUmRHDfQC1bCk8CAwEAAQJAPYFQlyu8405M656GxJuz\n" +
                "1ii0rkjWCV6SjleJkmg2rJh+vvc/kE0NK84C9VyrUyj8D7DxcXx3GNJRVTdqS234\n" +
                "AQIhAPjYLwMPTkf4W4k/KDJpKT+tFnhU1ZM76PxsyAFBFJ1/AiEA5POV+KJPYpOi\n" +
                "SHcN/du3SaRMub8RBMR4Y3/lgANzmzECIF2Q28xouuRwy+pFJxYdWHcq2+IO8+dS\n" +
                "hX40YNen4tp9AiEAmyixFCT7Y7Tln/J/GvFSD6g3DxJ7eB2l8Nh2MgEk0aECIQC2\n" +
                "TRjpfptcbSIbpVHdr5C1kF29PMUdhgzh4TzApCWW0Q==", "20201202150544|2017648923|7842e11b");
        System.out.println(s);


        System.out.println(decryptByPublicKeyPKCS1("MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAN6NUNrcGYqPm4EQ/q/6dzTerulZnagX\n" +
                "6gUeMIlMHT2767JMpsTKaGkYtp8jp3Jk0nyKG/MUZaUmRHDfQC1bCk8CAwEAAQ==", s));

    }

    /**
     * 公钥加密私钥解密
     */
    private static void test1(RSAKeyPair keyPair, String source) throws Exception {
        System.out.println("***************** 公钥加密私钥解密开始 *****************");
        String text1 = encryptByPublicKey(keyPair.getPublicKey(), source);

        System.out.println("===================== 分隔 =======================");

        String user = "lrZL1Xnf8Tib3WBIRfKqG5gco4HsD7QqllZzvoOcbnRoaBhIC0h/+q9cA+/8pqJHgZZarH+1ia+WipeF6iAnkw==";

        String userPrivateKey = "MIIBOwIBAAJBAN6NUNrcGYqPm4EQ/q/6dzTerulZnagX6gUeMIlMHT2767JMpsTK\n" +
                "aGkYtp8jp3Jk0nyKG/MUZaUmRHDfQC1bCk8CAwEAAQJAPYFQlyu8405M656GxJuz\n" +
                "1ii0rkjWCV6SjleJkmg2rJh+vvc/kE0NK84C9VyrUyj8D7DxcXx3GNJRVTdqS234\n" +
                "AQIhAPjYLwMPTkf4W4k/KDJpKT+tFnhU1ZM76PxsyAFBFJ1/AiEA5POV+KJPYpOi\n" +
                "SHcN/du3SaRMub8RBMR4Y3/lgANzmzECIF2Q28xouuRwy+pFJxYdWHcq2+IO8+dS\n" +
                "hX40YNen4tp9AiEAmyixFCT7Y7Tln/J/GvFSD6g3DxJ7eB2l8Nh2MgEk0aECIQC2\n" +
                "TRjpfptcbSIbpVHdr5C1kF29PMUdhgzh4TzApCWW0Q==";

        String userPublicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAN6NUNrcGYqPm4EQ/q/6dzTerulZnagX\n" +
                "6gUeMIlMHT2767JMpsTKaGkYtp8jp3Jk0nyKG/MUZaUmRHDfQC1bCk8CAwEAAQ==";

   //     String text2 = decryptByPrivateKey(keyPair.getPrivateKey(), text1);
        String text2 = decryptByPrivateKey(userPublicKey, user);
        System.out.println("加密前：" + user);

        System.out.println("加密后：" + user);

        System.out.println("解密后：" + text2);
        if (source.equals(text2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }
        System.out.println("***************** 公钥加密私钥解密结束 *****************");
    }

    /**
     * 私钥加密公钥解密
     *
     * @throws Exception
     */
    private static void test2(RSAKeyPair keyPair, String source) throws Exception {


        String user = "lrZL1Xnf8Tib3WBIRfKqG5gco4HsD7QqllZzvoOcbnRoaBhIC0h/+q9cA+/8pqJHgZZarH+1ia+WipeF6iAnkw==";

        String userPrivateKey = "MIIBOwIBAAJBAN6NUNrcGYqPm4EQ/q/6dzTerulZnagX6gUeMIlMHT2767JMpsTK\n" +
                "aGkYtp8jp3Jk0nyKG/MUZaUmRHDfQC1bCk8CAwEAAQJAPYFQlyu8405M656GxJuz\n" +
                "1ii0rkjWCV6SjleJkmg2rJh+vvc/kE0NK84C9VyrUyj8D7DxcXx3GNJRVTdqS234\n" +
                "AQIhAPjYLwMPTkf4W4k/KDJpKT+tFnhU1ZM76PxsyAFBFJ1/AiEA5POV+KJPYpOi\n" +
                "SHcN/du3SaRMub8RBMR4Y3/lgANzmzECIF2Q28xouuRwy+pFJxYdWHcq2+IO8+dS\n" +
                "hX40YNen4tp9AiEAmyixFCT7Y7Tln/J/GvFSD6g3DxJ7eB2l8Nh2MgEk0aECIQC2\n" +
                "TRjpfptcbSIbpVHdr5C1kF29PMUdhgzh4TzApCWW0Q==";

        String userPublicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAN6NUNrcGYqPm4EQ/q/6dzTerulZnagX\n" +
                "6gUeMIlMHT2767JMpsTKaGkYtp8jp3Jk0nyKG/MUZaUmRHDfQC1bCk8CAwEAAQ==";

        System.out.println("***************** 私钥加密公钥解密开始 *****************");
        String text1 = encryptByPrivateKey(keyPair.getPrivateKey(), source);
        String text2 = decryptByPublicKey(keyPair.getPublicKey(), text1);
        System.out.println("加密前：" + source);
        System.out.println("加密后：" + text1);
        System.out.println("解密后：" + text2);
        if (source.equals(text2)) {
            System.out.println("解密字符串和原始字符串一致，解密成功");
        } else {
            System.out.println("解密字符串和原始字符串不一致，解密失败");
        }
        System.out.println("***************** 私钥加密公钥解密结束 *****************");
    }

    /**
     * 公钥解密
     *
     * @param publicKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 公钥解密PKCS1 - 转PKCS8(C# -> JAVA)
     *
     * @param publicKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKeyPKCS1(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 私钥加密PKCS8
     *
     * @param privateKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥加密PKCS1 - 转PKCS1（C# -> JAVA）
     *
     * @param privateKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKeyPKCS1(String privateKeyText, String text) throws Exception {
        String pkcs8 = RsaPkcsTransformer.formatPkcs1ToPkcs8(privateKeyText);
        // 生成PrivateKey（前提对PKCS1变换为PKCS8）
        byte[] keyByte = cn.hutool.core.codec.Base64.decode(pkcs8);
        KeyFactory keyfactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec encodeRule = new PKCS8EncodedKeySpec(keyByte);
        PrivateKey privatekey = keyfactory.generatePrivate(encodeRule);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privatekey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyText
     * @param text
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String privateKeyText, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    /**
     * 公钥加密
     *
     * @param publicKeyText
     * @param text
     * @return
     */
    public static String encryptByPublicKey(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(text.getBytes());
        return Base64.encodeBase64String(result);
    }

    /**
     * 构建RSA密钥对
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static RSAKeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        String publicKeyString = Base64.encodeBase64String(rsaPublicKey.getEncoded());
        String privateKeyString = Base64.encodeBase64String(rsaPrivateKey.getEncoded());
        RSAKeyPair rsaKeyPair = new RSAKeyPair(publicKeyString, privateKeyString);
        return rsaKeyPair;
    }


    /**
     * RSA密钥对对象
     */
    public static class RSAKeyPair {

        private String publicKey;
        private String privateKey;

        public RSAKeyPair(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

    }

}