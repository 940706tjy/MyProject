package cn.bulaomeng.fragment.web;

import cn.bulaomeng.fragment.service.CreateAppSing;
import cn.bulaomeng.fragment.service.DeCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "新中新二维码对接")
@RequestMapping("/decode")
public class DeCodeController {

    @Autowired
    private DeCodeService deCodeService;

    @PostMapping(value = "/getCodeData")
    @ApiOperation(value = "接收信息", notes = "")
    public Object getCodeData(){
       /* //获取签名
        CreateAppSing createAppSing = deCodeService.getCreateAppSign();
        //验签
        //CreateAppSing checkSing = deCodeService.checkSign();
        //获取配置
         QrcodeConfig qc = deCodeService.getCodeConfData(createAppSing);
        //公钥
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAObmFl6JiP3ea9SUJtROpPvdmKjJGG0l2YgLZN3HO0pa3g4kqBYs6IAN9Zjil6K92D3Siq55ujkqESjE31+fNUMCAwEAAQ==";
        //二维码
        String code = "RFT://k2n/+6HCVbDCpgEsA+oqCbpqYikBZ39qnq1AG0m9zfhVHK7itN+1h6rlJzb1Zyf067lfnVohYRKkVnVasUzm/A==|ionGGcU+zaOgkeP8mqlGrVXUtydb35qLh08+mTI/zZM=";
        //通过配置接口协议头
        String protocolHeader = qc.getProtocolHeader();
        //解密并获得用户信息
        User user = XZXDeCode.deCode(publicKey,code,protocolHeader);
        System.out.println("用户信息为："+user);*/


        //获取签名信息
        CreateAppSing createAppSing = deCodeService.getCreateAppSign();
        System.out.println(createAppSing);
        //签名校验(将签名成功后的信息传入)
        CreateAppSing checkSing = deCodeService.checkSign(createAppSing);
        //在线获取信息
        return checkSing;

    }

}
