package cn.bulaomeng.fragment.service;

import cn.bulaomeng.fragment.entity.TxwxKeySecret;
import cn.bulaomeng.fragment.entity.User;
import cn.bulaomeng.fragment.mapper.TxwxKeySecretMapper;
import com.qq.weixiao.wxcode.CampusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TXWXDeCodeService {

    @Autowired
    private TxwxKeySecretMapper txwxKeySecretMapper;

    public TxwxKeySecret deCodeGetData(){
        String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAMLxqKhe6cGizEY+6MebaQLA/ikY0zYNGz4DynemnYNOFkpiusEE++los16Q/oSAxYQhgojjRZ/Y2fq2YaN3rmUCAwEAAQ==";

        TxwxKeySecret tx =  txwxKeySecretMapper.selectAll();
        //读取库中每天凌晨1点更新的配置
       /*
        String publicKey = tx.getPublicKey();
        String totpKey = tx.getSecret();//密钥
       */
        CampusCode campusCode = new CampusCode(publicKey, "1rJioSkDjSou0mTW");  //不需要解释规则

        //在线码
        campusCode.setClientTimeStamp(1525746200);  //调试时候，故意对齐本案例中的码的生成时间，正式上线请删除该行代码
        org.json.simple.JSONObject ret = campusCode.decode("http://wx.url.cn/v002.4144010559.baV7pm.U53JItdVpdvz-unznNxPuHyEkLHG6sGBpDaw2lxc5vZue_WYqYBSbdxayhFhfOHICKKbpII0K28HVd7ulQO1oA");
        System.out.println(ret); //{"code":0,"data":{"is_offline":0,"contract_type":"1","school_code":"4144010559","card_number":"00001234","identity_type":"0"},"message":"scuess"}
        User user = new User();
        //只有code为0，表示能正确解码
        if(Integer.parseInt(ret.get("code").toString()) == 0) {
            org.json.simple.JSONObject data = (  org.json.simple.JSONObject) ret.get("data");
            user.setUserNo(data.get("card_number").toString());
            System.out.println(data.get("card_number")); //学工号，key固定为card_numbe
            System.out.println(data.get("identity_type")); //对应构造方法中，规则解释方法json的字段
            System.out.println(data.get("entrusts")); //对应构造方法中，规则解释方法json的字段
        }else {
            System.out.println(ret.get("code")); //错误码
            System.out.println(ret.get("message"));//错误提示
        }
        return tx;
    }
}
