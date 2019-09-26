package cn.bulaomeng.fragment.entity;

//获取解码配置
public class TxwxKeySecret {
    private Integer id;
    private String secret;  //totp 密钥
    private String publicKey; //rsa 当前公钥
    private String prevPublic; //rsa 上个公钥
    private String nextPublic; //rsa 下个公钥
    private String rule;    //当前解码规则
    private String prevRule; //上个解码规则
    private String nextRule; //下个解码规则

    @Override
    public String toString() {
        return "TxwxKeySecret{" +
                "id=" + id +
                ", secret='" + secret + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", prevPublic='" + prevPublic + '\'' +
                ", nextPublic='" + nextPublic + '\'' +
                ", rule='" + rule + '\'' +
                ", prevRule='" + prevRule + '\'' +
                ", nextRule='" + nextRule + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrevPublic() {
        return prevPublic;
    }

    public void setPrevPublic(String prevPublic) {
        this.prevPublic = prevPublic;
    }

    public String getNextPublic() {
        return nextPublic;
    }

    public void setNextPublic(String nextPublic) {
        this.nextPublic = nextPublic;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getPrevRule() {
        return prevRule;
    }

    public void setPrevRule(String prevRule) {
        this.prevRule = prevRule;
    }

    public String getNextRule() {
        return nextRule;
    }

    public void setNextRule(String nextRule) {
        this.nextRule = nextRule;
    }
}
