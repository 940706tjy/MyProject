package cn.bulaomeng.fragment.util;

import cn.bulaomeng.fragment.entity.User;
import cn.bulaomeng.fragment.service.PublicKeys;
import cn.bulaomeng.fragment.service.PublicKeysList;
import cn.bulaomeng.fragment.service.QrcodeConfig;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.text.SimpleDateFormat;
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

    /** 
    * @Description: 获取与当前时间最近的公钥值及开始、结束时间
    * @Param: [list1] 
    * @return: cn.bulaomeng.fragment.service.PublicKeysList 
    * @Author: tjy
    * @Date: 2019/9/25 
    */ 
    public static PublicKeysList getCurrentList(List<PublicKeysList> list1){
        //这里设置一个整形数组，用来存放不同时段的公钥
        long[] dateArr = new long[list1.size()+1];
        //获得当前时间（与平台传来的参数格式一致）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date =new Date();
        String currentDate = sdf.format(date);
        //记录当前时间
        long currentFlag = Long.parseLong(currentDate);
        //将当前时间放入数组第一个位置
        dateArr[0] = currentFlag;
        //循环将从平台获取的数组存入数组（位置从第二个开始）
        for (int i=0;i<list1.size();i++){
            dateArr[i+1] = Long.parseLong(list1.get(i).getStartTime());
        }
        //将数组进行排序，找到当前时间的下标，取当前时间下标的下一个下标数据就为最近时间的数据
        Arrays.sort(dateArr);
        //用来记录时间最近的下标
        Integer currentIndex=-1;
        for (int i=0;i<dateArr.length;i++){
            if(currentFlag == dateArr[i]){
                currentIndex=i-1;
                break;
            }
        }
        //记录最终数据下表
        Integer listIndex=-1;
        //根据最近的时间去集合中找对应数据
        for (int i=0;i<list1.size();i++){
            if(dateArr[currentIndex] == Long.parseLong(list1.get(i).getStartTime())){
                listIndex=i;
                break;
            }
        }
        PublicKeysList getKey = new PublicKeysList();
        getKey.setEndTime(list1.get(listIndex).getEndTime());
        getKey.setStartTime(list1.get(listIndex).getStartTime());
        getKey.setPublicKey(list1.get(listIndex).getPublicKey());
        return getKey;
    }
    /**
    * @Description: 解密
    * @Param: [publicKey, code, qc]
    * @return: cn.bulaomeng.fragment.entity.User
    * @Author: tjy
    * @Date: 2019/9/25
    */
    public static User BouncyCastleProviderdeCode(PublicKeys publicKey,String code, QrcodeConfig qc){
        String pk;
        //1.根据配置接口获取的协议头判断是否系统发放的码。
        String isHeader = code.substring(0,qc.getProtocolHeader().length());
        //判断是否与配置接口获得的协议头吻合
        if(qc.getProtocolHeader().equals(isHeader)){
            System.out.println(qc.getProtocolHeader()+" --> "+isHeader+" 匹配协议头");
            //2.去除协议头
            code = code.substring(isHeader.length());

            //3.使用竖线“|”分割字符串，得到第一部分字符串为二维码编码字符串。得到第二部分为展示时间和其它数据的字符串（以下简称时间字符串）。二维码字符串使用RSA算法解密，时间字符串使用AES算法解密。
            String splitCode[] = code.split("\\|");

            /*
            4.使用AES算法解密“时间字符串”，密钥固定为“GreatgeRonfton81”算法使用“AES/CBC/PKCS7Padding”，向量为密钥的前16byte。解密后得到一个逗号分割的字符串，如：9,100000,7000,0,103717。最后一部分“103717”
            为二维码在小程序上展示的时间，通过与当前的时间进行比对，如果两者相差超过二维码配置接口返回的“displayLimitTime”的秒数，则认为二维码已经失效，程序应该提示用户二维码已经失效
             */
            String deCodeDate;
            try {
                //密钥
                String sessionKey = "GreatgeRonfton81";
                //向量
                String iv = "GreatgeRonfton81";
                Base64.Encoder encoder = Base64.getEncoder();
                String baseSessionKey = encoder.encodeToString(sessionKey.getBytes());
                String baseIv = encoder.encodeToString(iv.getBytes());
                Security.addProvider(new BouncyCastleProvider());
                //通过 AESPKCS7 解密字符串
                deCodeDate = AESPKCS7Util.decrypt(splitCode[1],baseSessionKey,baseIv);
                //根据逗号分隔时间字符串
                String getSpStr[] = deCodeDate.split(",");
                //获得截取时间字符串中最后的时间字段
                String codeDate = getSpStr[getSpStr.length-1];
                //获取当前时间时分秒，与截取的时间做差比较如果大于所规定时间为二维码超时
                SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
                String currentDate = sdf.format(new Date());
                //当前时间 - 二维码生成时间
                Integer endDate = Integer.parseInt(currentDate) - Integer.parseInt(codeDate);
                //按照配置接口返回的参数 规定大于为超时需要给出提示过期
              /*  if(endDate>=qc.getRiskControlTime()){
                    System.out.println("二维码已过期！");
                    return null;
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }

            //5.根据当前时间获取当前周期，进而获取当前周期的公钥
            PublicKeysList pkl = getCurrentList(publicKey.getPublicKeys());
            System.out.println(pkl);
                //离当前时间最近的公钥
            pk = pkl.getPublicKey();
            System.out.println("公钥："+pk);
            //6.加载公钥，初始化环境。密钥长度为512bit，使用的padding为“RSA/ECB/PKCS1Padding”

            //7.对二维码字符串进行base64解码，得到byte数组。
            String byteStr = splitCode[0];
            //解码这步在第8 RSAUtil.publicDecrypt 工具类中实现
            /*
            byte decode[] = Base64.decodeBase64(byteStr.getBytes());
            System.out.println(byteStr + "\t字符串解码后为：" + decode);
            */

            /*
            8.使用公钥对byte数组解密，得到解密后的byte数组。如果解密失败，则根据风控允许的误差时间（参考配置接口获取）
            加载前一周期或者下一周期的公钥再尝试解密。注意，如果base64解码后得到的byte数组的比特长度超过密钥的长度时，需要分片进行解密，然后再顺序拼接成一个byte数组
             */
            try {
                byte[] deByteArr = RSAUtil.publicDecrypt(byteStr,pk);
                String deStr = new String(deByteArr,"UTF-8");
                //9.把解密后的byte数组使用UTF_8编码转换为字符串。使用逗号分割符分割字符串为四部分，分别得到学工号、姓名、性别和字符串编码表示的二进制内容。
                String deSplitStr[] = deStr.split(",");
                //10.把字符串编码表示的二进制内容使用ISO_8859_1编码转换为byte数组，按照下面的分割方式获取对应的数据
                byte isoByteStr[] =deSplitStr [deSplitStr.length-1].getBytes("ISO_8859_1");
                User user =new User();
                user.setCheckCode(DeCodeUtil.bytesChar(DeCodeUtil.subBytes(isoByteStr,isoByteStr.length-5,5))); //存储解密后的校验码
                user.setUserName(deSplitStr[1]);//存储用户名称
                user.setUserNo(deSplitStr[0]);//存储学工号
                //把学工号使用UTF_8编码转换为byte数组。
                byte userNo[] = deSplitStr[0].getBytes("UTF-8");
                //把姓名使用UTF_8编码转换为byte数组。
                byte name[] = deSplitStr[1].getBytes("UTF-8");
                //把性别使用UTF_8编码转换为byte数组。
                byte sex[] = deSplitStr[2].getBytes("UTF-8");
                //除校验码的二进制内容
                byte all[] = DeCodeUtil.subBytes(isoByteStr,0,isoByteStr.length-5);
                //把byte数据按照学工号、姓名、性别和第9步的列表中除去校验码外的数据按照顺序拼接为一个byte数组。使用md5算法得到十六进编码的字符串，取最后5个字符，再使用UTF_8编码转换为5个字节的byte数组。
                byte[] byteResult = new byte[userNo.length+name.length+sex.length+all.length];
                System.arraycopy(userNo,0,byteResult,0,userNo.length);
                System.arraycopy(name,0,byteResult,userNo.length,name.length);
                System.arraycopy(sex,0,byteResult,userNo.length+name.length,sex.length);
                System.arraycopy(all,0,byteResult,userNo.length+name.length+sex.length,all.length);
                //比较解码得到的校验码和计算得到的校验码是否相同，确定解码的内容是否有效。
                String md5Str1 = DeCodeUtil.md5Encrypt3(new String(byteResult,"UTF-8"));
                byte md5StrSp[] = md5Str1.substring(md5Str1.length()-5).getBytes("UTF-8");
                user.setLinkCheckCode(DeCodeUtil.bytesChar(md5StrSp)); //截取后的校验码
                if(user.getCheckCode().equals(user.getLinkCheckCode())){
                    System.out.println("匹配校验码");
                }else {
                    return null;
                }
                return user;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
