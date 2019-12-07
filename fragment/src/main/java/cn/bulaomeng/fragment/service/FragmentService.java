package cn.bulaomeng.fragment.service;

import cn.bulaomeng.fragment.entity.Fragment;
import cn.bulaomeng.fragment.mapper.FragmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
@Service
public class FragmentService {

    @Autowired
    private FragmentMapper fragmentMapper;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Value("${demo-mail.from}")
    private String from;
/*    @Value("${demo-mail.to}")
    private String to;*/
    public List<Fragment> getListAll(){
         return   fragmentMapper.selectAll();
    }
    public int insert(Fragment fragment){
        Integer result=fragmentMapper.insert(fragment);
        if(result>0){
            if(fragment.getEmail()!=null){
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(from);
                message.setTo(fragment.getEmail()); //自己给自己发送邮件
                message.setSubject("主题:登记成功!");
                message.setText("请登录: http://qingjiujiu.wang/ 查看记录 和大佬们一起换取想要的式神吧!");
                mailSender.send(message);
            }
        }
        return  result;
    }
    public List<Fragment> selectByPrimaryKey(Fragment fragment){
        return   fragmentMapper.selectByPrimaryKey(fragment);
    }

    public List<String> getListByArray(String name){

        return   fragmentMapper.getListByArray(name);
    }

    public String goEmail(String toEmail){
        //简单邮件
/*        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(toEmail); //自己给自己发送邮件
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");

        mailSender.send(message);*/
      ///////////////////////分割线//////////////////////////
        //html邮件
        /*MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject("标题：发送Html内容");

            StringBuffer sb = new StringBuffer();
            sb.append("<h1>大标题-h1</h1>")
                    .append("<p style='color:#F00'>红色字</p>")
                    .append("<p style='text-align:right'>右对齐</p>");
            helper.setText(sb.toString(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailSender.send(message);*/
        ///////////////////////分割线//////////////////////////
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(toEmail);
            helper.setSubject("主题：带附件的邮件");
            helper.setText("带附件的邮件内容");
            //注意项目路径问题，自动补用项目路径
            FileSystemResource file = new FileSystemResource(new File("d:/2019001.jpg"));
            //加入邮件
            helper.addAttachment("图片.jpg", file);
        } catch (Exception e){
            e.printStackTrace();
        }
        mailSender.send(message);
        return "发送成功";
    }

}
