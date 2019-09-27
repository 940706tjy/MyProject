package cn.bulaomeng.fragment.web;

import cn.bulaomeng.fragment.service.*;
import cn.bulaomeng.fragment.util.DeCodeUtil;
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
    private XZXDeCodeService deCodeService;
    @Autowired
    private TXWXDeCodeService txwxDeCodeService;

    @PostMapping(value = "/getCodeData")
    @ApiOperation(value = "接收信息", notes = "")
    public Object getCodeData(){
        if(true){
            if(false){
                //1.获取签名信息
                CreateAppSing createAppSing = deCodeService.getCreateAppSign();
                //2.签名校验(将签名成功后的信息传入)
                // CreateAppSing checkSing = deCodeService.checkSign(createAppSing);
                //3.如果验签正确，使用签名的信息获取二维码配置信息
                QrcodeConfig qc = deCodeService.getCodeConfData(createAppSing);
                //4.获取公钥
                PublicKeys publicKeys = deCodeService.getPublicKey(createAppSing);
                //二维码
                String code = "RFT://Ozjjq1GisBA9whdp8uNEdcM3JUiPGVcMc97QgD3JMrF9o5DHu8pgKK8CtuqxMGf97pIrbYJPAWHsaoKhFihTuw==|NmMVRVEJGLHr38w7AaHLfu+t2wYZ6VitCrW1qiKZLc0=";
                //5.通过公钥解密，读取二维码信息
                return  DeCodeUtil.BouncyCastleProviderdeCode(publicKeys,code,qc);
            }
           /* String code = "";
            String terminalNo = "DSF00002";
            //deCodeService.onlineDeCode(code,terminalNo,);
            return null;*/
           return txwxDeCodeService.onlineDeCodeGetData();
        }else {

            return txwxDeCodeService.deCodeGetData();
        }
    }

}
