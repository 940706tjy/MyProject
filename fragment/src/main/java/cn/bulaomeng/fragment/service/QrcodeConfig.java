package cn.bulaomeng.fragment.service;

//获取二维码配置接口数据返回实体
public class QrcodeConfig {
    private String returnCode;//返回状态码
    private String returnMsg; //返回信息
    private String appId; //开放平台应用ID
    private String nonceStr; //随机字符串
    private String resultCode; //业务结果
    private String errCode; //错误代码
    private String errCodeDes; //错误代码描述
    private String appSign; //签名
    private Integer cycleMinutes; //周期间隔
    private Integer bufferCycles; //周期数量
    private Integer riskControlTime; //风险控制时间
    private String protocolHeader; //二维码协议头
    private Integer displayLimitTime; //二维码过期时间

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

    public Integer getCycleMinutes() {
        return cycleMinutes;
    }

    public void setCycleMinutes(Integer cycleMinutes) {
        this.cycleMinutes = cycleMinutes;
    }

    public Integer getBufferCycles() {
        return bufferCycles;
    }

    public void setBufferCycles(Integer bufferCycles) {
        this.bufferCycles = bufferCycles;
    }

    public Integer getRiskControlTime() {
        return riskControlTime;
    }

    public void setRiskControlTime(Integer riskControlTime) {
        this.riskControlTime = riskControlTime;
    }

    public String getProtocolHeader() {
        return protocolHeader;
    }

    public void setProtocolHeader(String protocolHeader) {
        this.protocolHeader = protocolHeader;
    }

    public Integer getDisplayLimitTime() {
        return displayLimitTime;
    }

    public void setDisplayLimitTime(Integer displayLimitTime) {
        this.displayLimitTime = displayLimitTime;
    }
}
