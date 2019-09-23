package cn.bulaomeng.fragment.service;

//在线获取用户实体
public class CodeUser {
    private String returnCode; //返回状态码
    private String returnMsg; //返回信息
    private String appId; //开放平台应用ID
    private String nonceStr; //随机字符串
    private String resultCode; //业务结果
    private String errCode; //错误代码
    private String errCodeDes; //错误代码描述
    private String appSign; //签名
    private String qrcodeStatus; //二维码状态
    private String networkStatus; //联网状态
    private String sno;//用户编号
    private String name;//用户姓名

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getAppSign() {
        return appSign;
    }

    public void setAppSign(String appSign) {
        this.appSign = appSign;
    }

    public String getQrcodeStatus() {
        return qrcodeStatus;
    }

    public void setQrcodeStatus(String qrcodeStatus) {
        this.qrcodeStatus = qrcodeStatus;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
