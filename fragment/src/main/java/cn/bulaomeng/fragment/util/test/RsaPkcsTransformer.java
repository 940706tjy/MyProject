package cn.bulaomeng.fragment.util.test;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.StringWriter;

public class RsaPkcsTransformer {
//    private static final String COMMENT_BEGIN_FLAG = "-----";
//    private static final String RETURN_FLAG_R = "\r";
//    private static final String RETURN_FLAG_N = "\n";


    //format PKCS#8 to PKCS#1
    public static String formatPkcs8ToPkcs1(String rawKey) throws Exception {
        String result = null;
        //extract valid key content
        String validKey = rawKey;//RsaPemUtil.extractFromPem(rawKey); // pem文件多行合并为一行
//        if(!Strings.isNullOrEmpty(validKey))
        //将BASE64编码的私钥字符串进行解码
        byte[] encodeByte = Base64.decodeBase64(validKey);

        //==========
        //pkcs8Bytes contains PKCS#8 DER-encoded key as a byte[]
        PrivateKeyInfo pki = PrivateKeyInfo.getInstance(encodeByte);
        RSAPrivateKeyStructure pkcs1Key = RSAPrivateKeyStructure.getInstance(pki.getPrivateKey());
        byte[] pkcs1Bytes = pkcs1Key.getEncoded();//etc.
        //==========

        String type = "RSA PRIVATE KEY";
        result = format2PemString(type, pkcs1Bytes);
        return result;
    }

    //format PKCS#1 to PKCS#8
    public static String formatPkcs1ToPkcs8(String rawKey) throws Exception {
        String result = null;
        //extract valid key content
        String validKey = rawKey;//RsaPemUtil.extractFromPem(rawKey); // pem文件多行合并为一行
//        if(!Strings.isNullOrEmpty(validKey))
        //将BASE64编码的私钥字符串进行解码
        byte[] encodeByte = Base64.decodeBase64(validKey);

        AlgorithmIdentifier algorithmIdentifier = new AlgorithmIdentifier(PKCSObjectIdentifiers.pkcs8ShroudedKeyBag);    //PKCSObjectIdentifiers.pkcs8ShroudedKeyBag
//            ASN1Object asn1Object = ASN1Object.fromByteArray(encodeByte);
        ASN1Object asn1Object = ASN1ObjectIdentifier.fromByteArray(encodeByte);
        PrivateKeyInfo privKeyInfo = new PrivateKeyInfo(algorithmIdentifier, asn1Object);
        byte[] pkcs8Bytes = privKeyInfo.getEncoded();

        String type = "PRIVATE KEY";
//            result = format2PemString(type, pkcs8Bytes); // 格式化为pem多行格式输出
        return Base64.encodeBase64String(pkcs8Bytes); // 直接一行字符串输出

    }

    // Write to pem file
    // 字符串换行显示
    private static String format2PemString(String type, byte[] privateKeyPKCS1) throws Exception {
        PemObject pemObject = new PemObject(type, privateKeyPKCS1);
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        String pemString = stringWriter.toString();
        return pemString;
    }

    //=== Testing ===
    public static void main(String[] args) throws Exception {
        String rawKey_pkcs1 = "";
        String rawKey_pkcs8 = "";

        rawKey_pkcs1 = "MIICXQIBAAKBgQDgDxka1U8SWI1vBY7pA1UiCVnrdtSgE+PpyTqe2YSEWCgkYQ2YsohZwsaUao7nya7QnBgRiPKEHgS/Eey+L9iwo32Sn5fUwb0nJ1+JeXRA6JsDEKpONJojIbF2nfgHLWsNn4bzn5Webc6WZLx0GyLTQuZGadFVuVq2dQqEsrq7HwIDAQABAoGAYtaGLo4WWXNywJzlE+kCbwdNAU/kL9FWYtT/5P7zNCZnXtTpWIi5GU+QpfvzmlAfq6qP+3w77wgG8/qGQsd8gGu3mydi0ImmD9sJdhhsJuWZhCMM+qmvSmvG/gvIr+bdEmPhpCQpa3BLveUkFDA/OnwfTVL6ruwZayMzuToB6WECQQD63Gx9DZVhYoSxR7qSmiGf/TjfOJusTcrmc27Z5X5MS36a3Ux9Z+c9EaYFldZ3cPzP521ugNVvZdovKqFKIcQ5AkEA5KYeKBVlkrLaamSEu5WAX3DqJ6iDRqEjzMoVad5B1I7kJHO+NijUxNHaWaSqLOHuk37X+EAjSTozzwOkKwbqFwJBAM1NhhAWBNHtcdEwddWzBJ/N+jRdPLIX/Fz7zZXQRruj8VpGkGn1lf6ZqfjaNuoLcyunKB0OnR6NCbIePl/QIKkCQGgEQjfN9BVWlBJOhCuqCWphvcQo3v+kktq5HCC7YYtHLfZ/SQrubEzVgtXBGUGtzpD+5VUkKGlJtwP4Dhkc3iUCQQCwiFKuQe/OdlkYk1L4mb0H0fzy+/6mYxyUqpTXUw/6BVDOyowvzieh9oh2ZhnQS7YPBWz5ZXzwUH4YVwGqxiwA";
        rawKey_pkcs8 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOAPGRrVTxJYjW8FjukDVSIJWet21KAT4+nJOp7ZhIRYKCRhDZiyiFnCxpRqjufJrtCcGBGI8oQeBL8R7L4v2LCjfZKfl9TBvScnX4l5dEDomwMQqk40miMhsXad+Actaw2fhvOflZ5tzpZkvHQbItNC5kZp0VW5WrZ1CoSyursfAgMBAAECgYBi1oYujhZZc3LAnOUT6QJvB00BT+Qv0VZi1P/k/vM0Jmde1OlYiLkZT5Cl+/OaUB+rqo/7fDvvCAbz+oZCx3yAa7ebJ2LQiaYP2wl2GGwm5ZmEIwz6qa9Ka8b+C8iv5t0SY+GkJClrcEu95SQUMD86fB9NUvqu7BlrIzO5OgHpYQJBAPrcbH0NlWFihLFHupKaIZ/9ON84m6xNyuZzbtnlfkxLfprdTH1n5z0RpgWV1ndw/M/nbW6A1W9l2i8qoUohxDkCQQDkph4oFWWSstpqZIS7lYBfcOonqINGoSPMyhVp3kHUjuQkc742KNTE0dpZpKos4e6Tftf4QCNJOjPPA6QrBuoXAkEAzU2GEBYE0e1x0TB11bMEn836NF08shf8XPvNldBGu6PxWkaQafWV/pmp+No26gtzK6coHQ6dHo0Jsh4+X9AgqQJAaARCN830FVaUEk6EK6oJamG9xCje/6SS2rkcILthi0ct9n9JCu5sTNWC1cEZQa3OkP7lVSQoaUm3A/gOGRzeJQJBALCIUq5B7852WRiTUviZvQfR/PL7/qZjHJSqlNdTD/oFUM7KjC/OJ6H2iHZmGdBLtg8FbPllfPBQfhhXAarGLAA=";

        String formatKey1 = formatPkcs1ToPkcs8(rawKey_pkcs1);
        String formatKey2 = formatPkcs8ToPkcs1(rawKey_pkcs8);
        System.out.println(formatKey1);
        System.out.println(formatKey2);

    }

}