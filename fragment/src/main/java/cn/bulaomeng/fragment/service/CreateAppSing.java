package cn.bulaomeng.fragment.service;


//签名返回结果实体
public class CreateAppSing {
    private String returnCode;//返回状态码
    private String returnMsg; //返回信息
    private String appSign; //本地认证签名
    private String nonceStr; //随机字符串
    private String localAuthSign; //签名
    private String state; //签名结果（用于验证签名接口）

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

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

    public String getAppSign() {
        return appSign;
    }

    public void setAppSign(String appSign) {
        this.appSign = appSign;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getLocalAuthSign() {
        return localAuthSign;
    }

    public void setLocalAuthSign(String localAuthSign) {
        this.localAuthSign = localAuthSign;
    }
}
