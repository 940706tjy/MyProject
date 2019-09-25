package cn.bulaomeng.fragment.service;

import cn.bulaomeng.fragment.util.DeCodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/*
    离线解码获取用户信息
 */
@Service
public class DeCodeService {

    //签名接口地址
    private final String CREATE_APP_SIGN = "http://60.205.182.0:9000/sign/createappsign";
    //验证签名接口接口地址
    private final String VERIFY_APP_SIGN = "http://60.205.182.0:9000/sign/verifyappsign";
    //二维码配置接口地址
    private final String GET_CODE_CONFIG = "http://demo.greatge.net:28981/code/getqrcodeconfig";
    //公钥接口
    private final String GET_PUBLIC_KEYS = "http://demo.greatge.net:28981/cmn/getpublickeys";
    //在线获取数据地址
    private final String GET_IDENTY_BY_QRCODE = "http://60.205.182.0:9000/query/getidentitybyqrcode";

    //所需参数
    String appId = "LibraryAccessControl";
    String name  ="test";
    String key = "ryAJCLlelunLLb02rt4V7xUaH6ScZn1l";
    /*** 
    * @Description: 请求签名接口 
    * @Param:
    * @return: cn.bulaomeng.fragment.service.CreateAppSing 
    * @Author: tjy
    * @Date: 2019/9/23 
    */ 
    public CreateAppSing getCreateAppSign(){

        //map参数中所需的对象
        InputParam inputParam = new InputParam();
        inputParam.setAppId(appId);
        //inputParam.setName(name);

        //生成随机数(32位)
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        /*
            签名算法
         */
        //将参数存入map，调用签名算法
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("data",JSON.toJSONString(inputParam)); //这里需要将对象转成json字符串，使用fastjson工具
        parameters.put("nonceStr",uuid);
        //调用签名算法，得到签名字符串
        String mySign = DeCodeUtil.createSign(parameters,key);
        //通过RestTemplate请求对应接口
        ParamRestTemplate par = new ParamRestTemplate();
        //返回对象
        CreateAppSing createAppSing = new CreateAppSing();
        //RestTemplate中所需的Map参数
        Map<String,Object> map = new HashMap<>();
        //签名所需参数
        /*
          待签名数据  	data		   String	 示例： {appid :"rft123456", name: "第三饭堂"}
          随机字符串 	nonceStr	   String	 示例： 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
          本地认证签名	localAuthSign  String	 示例： D363A06B5C35CF99C85CA477FCE8B36B
         */
        inputParam.setAppId(appId);
        //inputParam.setName(name);
        map.put("data",JSON.toJSONString(inputParam));//这里需要将对象转成json字符串，使用fastjson工具
        map.put("nonceStr",uuid);
        map.put("localAuthSign",mySign);
        par.setParam(map);
        par .setUrl(CREATE_APP_SIGN);
        //获取接口返回结果
        JSONObject js = reqeusRestTemplate(par);
        //将结果存入对象
        if(js == null){
            return null;
        }
        createAppSing.setReturnCode(js.getString("returnCode"));
        createAppSing.setReturnMsg(js.getString("returnMsg"));
        //当returnCode为SUCCESS的时候，还会包括以下字段
        if ("SUCCESS".equals(createAppSing.getReturnCode())){
            createAppSing.setAppSign(js.getString("appSign"));
            createAppSing.setLocalAuthSign(js.getString("localAuthSign"));
            createAppSing.setNonceStr(js.getString("nonceStr"));
        }else {
            return null;
        }
        return createAppSing;
    }

    /** 
    * @Description: 验证签名
    * @Param: [singData]
    * @return: cn.bulaomeng.fragment.service.CreateAppSing
    * @Author: tjy
    * @Date: 2019/9/23 
    */ 
    public CreateAppSing checkSign(CreateAppSing singData){
        //判断签到成功后返回的数据是否存在
        if(StringUtils.isBlank(singData.getNonceStr()) || StringUtils.isBlank(singData.getAppSign()) || StringUtils.isBlank(singData.getLocalAuthSign()) ){
            return null;
        }
        //map参数中所需的对象
        InputParam inputParam = new InputParam();
        //将签名返回的数据当参数传入
        inputParam.setAppId(appId);
        inputParam.setNonceStr(singData.getNonceStr());
        inputParam.setAppSign(singData.getAppSign());
        //将参数存入map，调用签名算法
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("data",JSON.toJSONString(inputParam)); //这里需要将对象转成json字符串，使用fastjson工具
        parameters.put("nonceStr",singData.getNonceStr());
        //调用签名算法，得到签名字符串
        String mySign = DeCodeUtil.createSign(parameters,key);

        //通过RestTemplate请求对应接口
        ParamRestTemplate par = new ParamRestTemplate();
        //返回对象
        CreateAppSing createAppSing = new CreateAppSing();
        //RestTemplate中所需的Map参数
        Map<String,Object> map = new HashMap<>();
        /*
         待验签数据	    data		    String   {appid :"rft123456", name: "第三饭堂",\"nonceStr" : \"DFDAFLJ5656356LKFADF675DDD","appSign": "D363A06B5C35CF99C85CA477FCE8B36B"}	需要进行验签的数据，json格式，即开放平台返回的数据，其中包括应用认证签名
         随机字符串	    nonceStr		String	 5K8264ILTKCH16CQ2502SI8ZNMTM67VS	用于签名
         本地认证签名	localAuthSign	String	 D363A06B5C35CF99C85CA477FCE8B36B	按照签名算法，使用本地安全密钥钥对上述所有数据进行签名
         */

        map.put("data",JSON.toJSONString(inputParam));
        map.put("nonceStr",singData.getNonceStr());
        map.put("localAuthSign",mySign);
        par.setParam(map);
        par.setUrl(VERIFY_APP_SIGN);
        JSONObject js = reqeusRestTemplate(par);
        if(js == null){
            return null;
        }
        //将结果存入对象
        createAppSing.setReturnCode(js.getString("returnCode"));
        createAppSing.setReturnMsg(js.getString("returnMsg"));
        //判断是否存在以下字段，如不存在标示 returnCode 或 returnMsg 中存在问题则
        if ("SUCCESS".equals(createAppSing.getReturnCode())){
            //签名结果 1：通过；0：不通过  不通过如何处理？
            //这里如果在这递归处理，如果一直请求不成功就会进入无限循环
            if("0".equals(js.getString("state"))){
                return null;
            }
            createAppSing.setState(js.getString("state"));
            createAppSing.setLocalAuthSign(js.getString("localAuthSign"));
            createAppSing.setNonceStr(js.getString("nonceStr"));
        }
        return createAppSing;
    }
    
    /** 
    * @Description: 获取二维码配置数据
    * @Param: [] 
    * @return: cn.bulaomeng.fragment.service.QrcodeConfig 
    * @Author: tjy
    * @Date: 2019/9/23 
    */ 
    public QrcodeConfig getCodeConfData(CreateAppSing singData){
        //判断签到成功后返回的数据是否存在
        if(StringUtils.isBlank(singData.getAppSign()) || StringUtils.isBlank(singData.getLocalAuthSign())){
            return null;
        }
        //通过RestTemplate请求对应接口
        ParamRestTemplate par = new ParamRestTemplate();
        //返回对象
        QrcodeConfig qrcodeConfig = new QrcodeConfig();
        //RestTemplate中所需的Map参数
        Map<String,Object> map = new HashMap<>();
        /*
            开放平台应用ID	appId		String	    rft123456	                        开放平台应用ID
            随机字符串	    nonceStr	String	    5K8264ILTKCH16CQ2502SI8ZNMTM67VS	用于签名
            应用认证签名	    appSign		String	    D363A06B5C35CF99C85CA477FCE8B36B	按照签名算法，使用应用动态秘钥对上述所有数据进行签名
         */
        //将签名成功后的信息作为参数
        InputParam inputParam = new InputParam();
        inputParam.setAppId(appId);
        map.put("appId",appId);//这里需要将对象转成json字符串，使用fastjson工具
        map.put("nonceStr",singData.getNonceStr());
        map.put("appSign",singData.getAppSign());
        map.put("data",JSON.toJSONString(inputParam));
        par.setParam(map);
        par.setUrl(GET_CODE_CONFIG);
        JSONObject js = reqeusRestTemplate(par);
        if(js == null){
            return null;
        }
        //将结果存入对象
        qrcodeConfig.setReturnCode(js.getString("returnCode"));
        qrcodeConfig.setReturnMsg(js.getString("returnMsg"));

        //当returnCode为SUCCESS的时候，还会包括以下字段：
        /*
            开放平台应用ID	    appId		    String	    rft123456
            随机字符串	        nonceStr	    String	    5K8264ILTKCH16CQ2502SI8ZNMTM67VS
            业务结果	            resultCode	    String	    SUCCESS
            错误代码            	errCode		    String	    SYSTEMERROR (必填：否)
            错误代码描述	        errCodeDes	    String	    系统错误 (必填：否)
            签名	                appSign		    String	    C380BEC2BFD727A4B6845133519F3AD6
         */
        if ("SUCCESS".equals(qrcodeConfig.getReturnCode())){
            qrcodeConfig.setAppId(js.getString("appId"));
            qrcodeConfig.setNonceStr(js.getString("nonceStr"));
            qrcodeConfig.setResultCode(js.getString("resultCode"));
            qrcodeConfig.setAppSign(js.getString("appSign"));
            //errCode和errCodeDes判断是否有，因为它是非必填字段
            if(StringUtils.isNotBlank(js.getString("errCode")) && StringUtils.isNotBlank(js.getString("errCodeDes"))){
                qrcodeConfig.setErrCode(js.getString("errCode"));
                qrcodeConfig.setErrCodeDes(js.getString("errCodeDes"));
            }
        }
        //当returnCode 和resultCode都为SUCCESS的时，还会包括以下字段
        /*
            	周期间隔            	    cycleMinutes		Int		   120       周期间隔(单位为分钟)，最小值为10分钟。例如：120表示每个周期长度为120分钟，一天24小时就分为 24 * 60 / 12 = 12 个周期。
	            周期数量	                bufferCycles		Int		   24        可以缓存码的最大周期数量。例如：24表示按照每天12个周期，就能够获取从当前周期开始最多两天的二维码。
	            风险控制时间	            riskControlTime		Int		   10        风控允许的误差，单位为秒。用于在周期的边界，判断是否应该加载前一周期或者下一周期的公钥来解密数据。
	            二维码协议头	            protocolHeader		String	 RFT://	     用来标记二维码是否APP或小程序生成的标记，类似网页地址的头部部分。
	            二维码展示后过期时间	    displayLimitTime	Int		   65        APP或小程序展示二维码时在码上面记录显示的时间，超过过期时间的二维码必须视为失效的码。（单位为秒）
         */
        if ("SUCCESS".equals(qrcodeConfig.getReturnCode()) && "SUCCESS".equals(qrcodeConfig.getResultCode())) {
            qrcodeConfig.setCycleMinutes(js.getInteger("cycleMinutes"));
            qrcodeConfig.setBufferCycles(js.getInteger("bufferCycles"));
            qrcodeConfig.setRiskControlTime(js.getInteger("riskControlTime"));
            qrcodeConfig.setDisplayLimitTime(js.getInteger("displayLimitTime"));
            qrcodeConfig.setProtocolHeader(js.getString("protocolHeader"));

        }
            return  qrcodeConfig;
    }
    /** 
    * @Description: 获取公钥
    * @Param: [] 
    * @return: cn.bulaomeng.fragment.service.PublicKeys 
    * @Author: tjy
    * @Date: 2019/9/23 
    */ 
    public PublicKeys getPublicKey(CreateAppSing singData){
        //判断签到成功后返回的数据是否存在
        if(StringUtils.isBlank(singData.getAppSign()) || StringUtils.isBlank(singData.getLocalAuthSign())){
            return null;
        }
        //通过RestTemplate请求对应接口
        ParamRestTemplate par = new ParamRestTemplate();
        //返回对象
        PublicKeys pk = new PublicKeys();
        //RestTemplate中所需的Map参数
        Map<String,Object> map = new HashMap<>();
        /*
            开放平台应用ID	appId		String	    rft123456	                        开放平台应用ID
            随机字符串	    nonceStr	String	    5K8264ILTKCH16CQ2502SI8ZNMTM67VS	用于签名
            应用认证签名	    appSign		String	    D363A06B5C35CF99C85CA477FCE8B36B	按照签名算法，使用应用动态秘钥对上述所有数据进行签名
        */
        //将签名成功后的信息作为参数
        map.put("nonceStr",singData.getNonceStr());
        map.put("appSign",singData.getAppSign());
        map.put("appId",appId);
        par.setParam(map);
        par.setUrl(GET_PUBLIC_KEYS);
        JSONObject js = reqeusRestTemplate(par);
        if(js == null){
            return null;
        }
        //将结果存入对象
        pk.setReturnCode(js.getString("returnCode"));
        pk.setReturnMsg(js.getString("returnMsg"));
        System.out.println(js);
        //当returnCode为SUCCESS的时候，还会包括以下字段：
        /*
            开放平台应用ID	    appId		    String	    rft123456
            随机字符串	        nonceStr	    String	    5K8264ILTKCH16CQ2502SI8ZNMTM67VS
            业务结果	            resultCode	    String	    SUCCESS
            错误代码            	errCode		    String	    SYSTEMERROR (必填：否)
            错误代码描述	        errCodeDes	    String	    系统错误 (必填：否)
            签名	                appSign		    String	    C380BEC2BFD727A4B6845133519F3AD6
         */
        if ("SUCCESS".equals(pk.getReturnCode())){
            pk.setAppId(js.getString("appId"));
            pk.setNonceStr(js.getString("nonceStr"));
            pk.setResultCode(js.getString("resultCode"));
            pk.setAppSign(js.getString("appSign"));
            //errCode和errCodeDes判断是否有，因为它是非必填字段
            if(StringUtils.isNotBlank(js.getString("errCode")) && StringUtils.isNotBlank(js.getString("errCodeDes"))){
                pk.setErrCode(js.getString("errCode"));
                pk.setErrCodeDes(js.getString("errCodeDes"));
            }
        }
        //当returnCode 和resultCode都为SUCCESS的时，还会包括以下字段：公钥数组 publicKeys
        if ("SUCCESS".equals(pk.getReturnCode()) && "SUCCESS".equals(pk.getResultCode())) {
            List<PublicKeysList> list = JSONObject.parseArray(js.getString("publicKeys"), PublicKeysList.class);
            pk.setPublicKeys(list);
        }
            return pk;
    }

    /**
    * @Description:  在线获取二维码信息
    * @Param: [authCode, terminalNo]
    * @return: cn.bulaomeng.fragment.entity.User
    * @Author: tjy
    * @Date: 2019/9/23
    */
    public CodeUser onlineDeCode(String authCode,String terminalNo,CreateAppSing singData){
        //通过RestTemplate请求对应接口
        ParamRestTemplate par = new ParamRestTemplate();
        //返回对象
        CodeUser user = new CodeUser();
        //RestTemplate中所需的Map参数
        Map<String,Object> map = new HashMap<>();
        //authCode--二维码码文  terminalNo--终端编号
        map.put("authCode",authCode);
        map.put("terminalNo",terminalNo);
        map.put("nonceStr",singData.getNonceStr());
        map.put("localAuthSign",singData.getLocalAuthSign());
        par.setParam(map);
        par.setUrl(GET_IDENTY_BY_QRCODE);
        JSONObject js = reqeusRestTemplate(par);
        if(js==null){
            //TODO
        }
        //将结果存入对象
        user.setReturnCode(js.getString("returnCode"));
        user.setReturnMsg(js.getString("returnMsg"));
        //当returnCode为SUCCESS的时候，还会包括以下字段：
        /*
            开放平台应用ID	    appId		    String	    rft123456
            随机字符串	        nonceStr	    String	    5K8264ILTKCH16CQ2502SI8ZNMTM67VS
            业务结果	            resultCode	    String	    SUCCESS
            错误代码            	errCode		    String	    SYSTEMERROR (必填：否)
            错误代码描述	        errCodeDes	    String	    系统错误 (必填：否)
            签名	                appSign		    String	    C380BEC2BFD727A4B6845133519F3AD6
         */
        if ("SUCCESS".equals(user.getReturnCode())){
            user.setAppId(js.getString("appId"));
            user.setNonceStr(js.getString("nonceStr"));
            user.setResultCode(js.getString("resultCode"));
            user.setAppSign(js.getString("appSign"));
            //errCode和errCodeDes判断是否有，因为它是非必填字段
            if(StringUtils.isNotBlank(js.getString("errCode")) && StringUtils.isNotBlank(js.getString("errCodeDes"))){
                user.setErrCode(js.getString("errCode"));
                user.setErrCodeDes(js.getString("errCodeDes"));
            }
        }
        //当returnCode 和resultCode都为SUCCESS的时，还会包括以下字段
        /*
            二维码状态	qrcodeStatus	String	 AVAILABLE	如果是扫码，则返回二维码状态：可用AVAILABLE、无法识别UNRECOGNIZED、可识别已失效INVALID
	        联网状态	   networkStatus	String	 ONLINE	    联网ONLINE、脱网OFFLINE，联网即融付通开放安全服务可连接到开放平台的服务。
         */
        if ("SUCCESS".equals(user.getReturnCode()) && "SUCCESS".equals(user.getResultCode())) {
            user.setQrcodeStatus(js.getString("qrcodeStatus"));
            user.setNetworkStatus(js.getString("networkStatus"));
            //二维码状态==可用，还会返回以下身份信息：
            if("AVAILABLE".equals(user.getQrcodeStatus())){
                //这里二维码状态可用只取 用户编号、姓名两个字段
                user.setSno(js.getString("sno"));
                user.setName(js.getString("name"));
            }
        }else {
            //TODO
        }
            return user;
    }

    /**
    * @Description:  RestTemplate请求公共方法
    * @Param: [paramRestTemplate]
    * @return: com.alibaba.fastjson.JSONObject
    * @Author: tjy
    * @Date: 2019/9/23
    */
    public JSONObject reqeusRestTemplate(ParamRestTemplate paramRestTemplate){
        if(paramRestTemplate.getParam() == null || paramRestTemplate.getUrl() == null){
            //TODO
        }
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(paramRestTemplate.getUrl(),paramRestTemplate.getParam(),JSONObject.class);
    }
}
