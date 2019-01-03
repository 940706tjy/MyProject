package cn.weixin.wxtjy.ctrl;

import cn.weixin.wxtjy.entity.BaseMessage;
import cn.weixin.wxtjy.entity.ImageMessage;
import cn.weixin.wxtjy.util.MessageUtil;
import cn.weixin.wxtjy.util.WXToken;
import com.thoughtworks.xstream.XStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

@RestController
public class Wxctrl {
    private BaseMessage bm;
    @GetMapping("/user")
    public String getWx(@RequestParam("signature")String signature, @RequestParam("timestamp")String timestamp,@RequestParam("nonce")String nonce,@RequestParam("echostr")String echostr){
        System.out.println(signature+"--"+timestamp+"--"+"--"+nonce+"--"+echostr);
        if(WXToken.check(timestamp,nonce,signature)){
            System.out.println("******微信接口接入!");
            return echostr;
        }else {
            System.out.println("******微信接口失败!");
        }
        return null;
    }
    @PostMapping("/user")
    public String getS(HttpServletRequest request, HttpServletResponse response){
         try {
            //将微信请求xml转为map格式，获取所需的参数
            Map<String,String> map = MessageUtil.xmlToMap(request.getInputStream());
            System.out.println(map);
            request.setCharacterEncoding("utf8");
            response.setCharacterEncoding("utf8");
            String xml=null;
            XStream xStream=new XStream();
            //判断是哪种类型的请求
            switch (map.get("MsgType")){
                case "text":
                    System.out.println("进入text-----");
                    bm=MessageUtil.getFlag(map);
                    xStream.processAnnotations(bm.getClass());
                    xml=xStream.toXML(bm);
                    break;
                case  "image":
                    System.out.println("进入image-----");
                    bm =new ImageMessage(map,"Nn");
                    xStream.processAnnotations(bm.getClass());
                    xml=xStream.toXML(bm);
                    break;
                case "news":
                    break;
                case "voice":
                    break;
                case "video":
                    break;
                case "music":
                    break;
                    default:
                        break;
            }
            //需要的xml格式
            PrintWriter pw= response.getWriter();
            response.reset();
            System.out.println(xml);
            pw.print(xml);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
