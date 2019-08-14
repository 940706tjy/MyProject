package cn.bulaomeng.fragment.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class outMail {
    @Value("${demo-mail.from}")
    public static String from;
    @Value("${demo-mail.to}")
    public static String to;
    public static void main(String[] args) {
       /* SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("1420408096@qq.com");
        mailMessage.setTo("956905891@qq.com");

        mailMessage.setSubject("测试Springboot发送邮件");
        mailMessage.setText("正在发送。。。。");*/
      /*  List<Integer> list=new ArrayList<>();
        List<String> ls=new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add(i);
            if(i==5){
                list.clear();
            }
        }1564553982000
        System.out.println(list);*/
        // 10位的秒级别的时间戳
        long time1 = 1527767665;
        String result1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time1 * 1000));
        System.out.println("10位数的时间戳（秒）--->Date:" + result1);
        Date date1 = new Date(time1*1000);   //对应的就是时间戳对应的Date
        // 13位的秒级别的时间戳
        double time2 = 1564553982000d;
        String result2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time2);
        System.out.println("13位数的时间戳（毫秒）--->Date:" + result2);
        System.out.println("1:"+(System.currentTimeMillis()-(long)Double.parseDouble("1564553982000")));
        long ms=10800000;
        long l = System.currentTimeMillis()-(long)1564553982000d;
        System.out.println("2:"+l);
        System.out.println(3*60*60*1000);
        System.out.println("1133333333333333".substring(1));
        String name="";
        if(name!=null && !"".equals(name)){
           name=name.substring(1);
        }
        System.out.println("name:"+name);
    }

   /* public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        if(milliSecond > 0) {
            sb.append(milliSecond+"毫秒");
        }
        return sb.toString();
    }*/
}
