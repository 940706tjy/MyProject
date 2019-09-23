package cn.bulaomeng.fragment.service;

import java.util.List;

//公钥返回结果实体
public class PublicKeys {
    private String returnCode; //返回状态码
    private String returnMsg; //返回信息
    private String appId; //开放平台应用ID
    private String nonceStr; //随机字符串
    private String resultCode; //业务结果
    private String errCode; //错误代码
    private String errCodeDes; //错误代码描述
    private String appSign; //签名
    private List<PublicKeysList> publicKeys; //公钥数组

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

    public List<PublicKeysList> getPublicKeys() {
        return publicKeys;
    }

    public void setPublicKeys(List<PublicKeysList> publicKeys) {
        this.publicKeys = publicKeys;
    }
}
