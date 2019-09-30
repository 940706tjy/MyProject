package cn.bulaomeng.fragment.web;

import cn.bulaomeng.fragment.service.*;
import cn.bulaomeng.fragment.util.DeCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Api(value = "新中新二维码对接")
@RequestMapping("/decode")
public class DeCodeController {

    @Autowired
    private XZXDeCodeService deCodeService;
    @Autowired
    private TXWXDeCodeService txwxDeCodeService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping(value = "/getCodeData")
    @ApiOperation(value = "接收信息", notes = "")
    public Object getCodeData(String code){
        if(true){

                //1.获取签名信息
                CreateAppSing createAppSing = deCodeService.getCreateAppSign();
                //2.签名校验(将签名成功后的信息传入)
                // CreateAppSing checkSing = deCodeService.checkSign(createAppSing);
                //3.如果验签正确，使用签名的信息获取二维码配置信息
                QrcodeConfig qc = deCodeService.getCodeConfData(createAppSing);
                //4.获取公钥
                PublicKeys publicKeys = deCodeService.getPublicKey(createAppSing);
                //二维码
           /* List<String> list = new ArrayList<>();
            list.add("aaa");
            list.add("bbb");
            list.add("ccc");
            list.add("ddd");
            Map<String,Object> map = new HashMap<>();
            map.put("SpringBoot","SpringBoot");
            map.put("SpringData","SpringData");
            map.put("SpringJpa","SpringJpa");
            map.put("SpringNice","SpringNice");
               System.out.println(redisTemplate.opsForValue().get("rrew"));
               //redisTemplate.opsForList().rightPush("dens",list);
             List<Object> list1 = redisTemplate.opsForList().range("dens", 0, -1);
            System.out.println(list1.get(0));*/
            //5.通过公钥解密，读取二维码信息
                return  DeCodeUtil.BouncyCastleProviderdeCode(publicKeys,code,qc);
       /*     CreateAppSing createAppSing = deCodeService.getCreateAppSign();
            String terminalNo = "DSF00002";
            return deCodeService.onlineDeCode(code,terminalNo,createAppSing);*/
           //return txwxDeCodeService.onlineDeCodeGetData();
        }else {

            return txwxDeCodeService.deCodeGetData();
        }
    }

}
