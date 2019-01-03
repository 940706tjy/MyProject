package cn.weixin.wxtjy;

import cn.weixin.wxtjy.util.WXToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WxtjyApplicationTests {
    @Value("${Access_token_Url}")
    private  String Access_token;
    @Test
    public void contextLoads() {
        /*Map<String,String>map=new HashMap<String,String>();
        map.put("toUserName","s");
        map.put("fromUserName","f");
        TextMessage textMessage=new TextMessage(map,"Nn");
        System.out.println(textMessage.toString());
        XStream xStream=new XStream();
        xStream.processAnnotations(textMessage.getClass());
        String xml=xStream.toXML(textMessage);
        System.out.println(xml);*/
        WXToken.getAccess_Token();
        System.out.println(Access_token);

    }

}

