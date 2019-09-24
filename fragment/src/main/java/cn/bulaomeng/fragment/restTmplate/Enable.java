package cn.bulaomeng.fragment.restTmplate;

import cn.bulaomeng.fragment.entity.Fragment;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*@Component
@Configuration
@EnableScheduling*/
public class Enable {
    RestTemplate restTemplate = new RestTemplate();
    @Scheduled(cron = "0/5 * * * * ?")
    public void outEnable(){
        String data=restTemplate.postForObject("http://47.104.84.65/fragment/selectByPrimaryKey",new Fragment(),String.class);
        System.out.println(data);
    }
}
