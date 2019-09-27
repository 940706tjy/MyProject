package cn.bulaomeng.fragment.entity;
//腾讯微校在线
public class TxwxKeySecretOnlin {
    private String appKey;//应用分配的app_key
    private long timestamp;//当前unix时间戳（秒）
    private String nonce;//32位随机字符串
    private String signature;//使用签名算法计算出来的数字签名
    private int scene;//1.门禁 2.消费 3.签到 4.其它
    private String deviceNo;//设备编号
    private String location;//扫码地点
    private String authCode;//扫码获得的动态码
    private String schoolCode;//扫码的学校编码

    @Override
    public String toString() {
        return "TxwxKeySecretOnlin{" +
                "appKey='" + appKey + '\'' +
                ", timestamp=" + timestamp +
                ", nonce='" + nonce + '\'' +
                ", signature='" + signature + '\'' +
                ", scene=" + scene +
                ", deviceNo='" + deviceNo + '\'' +
                ", location='" + location + '\'' +
                ", authCode='" + authCode + '\'' +
                ", schoolCode='" + schoolCode + '\'' +
                '}';
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getScene() {
        return scene;
    }

    public void setScene(int scene) {
        this.scene = scene;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }
}
