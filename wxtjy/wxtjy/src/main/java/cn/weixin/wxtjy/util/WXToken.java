package cn.weixin.wxtjy.util;

import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
public class WXToken {
    private static final String TOKEN="wx123";
    @Value("Access_token_Url")
    private  static  String  Access_token;
    public static String getAccess_Token(){
        StringBuffer sb=new StringBuffer();
        try {
            URL u=new URL(Access_token);
            URLConnection urlConnection = u.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            byte[] b=new byte[1024];
            int len;
            while ((len=inputStream.read(b))!=-1){
                sb.append(new String(b,0,len));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    /**
     * 验证签名
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    public static boolean check(String timestamp,String nonce,String signature){

        String[] strs=new String[]{TOKEN,timestamp,nonce};
        System.out.println("排序前***********:"+strs.toString());
        Arrays.sort(strs);
        System.out.println("排序后***********:"+strs.toString());
        String str=SHA1.encode(strs[0]+strs[1]+strs[2]);
        System.out.println(str);
        System.out.println(signature+"--------------------");
        //sha1加密
        return str!=null?str.equals(signature.toUpperCase()):false;
    }
}

