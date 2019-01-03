package cn.weixin.wxtjy.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;
@XStreamAlias("xml")
public class BaseMessage {
    @XStreamAlias("ToUserName")
    // 开发者微信号
    private String toUserName;
    @XStreamAlias("FromUserName")
    // 发送方帐号（一个OpenID）
    private String fromUserName;
    @XStreamAlias("CreateTime")
    // 消息创建时间 （整型）
    private long createTime;
    @XStreamAlias("MsgType")
    // 消息类型（text/image/location/link）
    private String msgType;

    public BaseMessage() {
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public BaseMessage(Map<String,String> map){
        this.toUserName=map.get("FromUserName");
        this.fromUserName=map.get("ToUserName");
        this.createTime=System.currentTimeMillis()/1000;
    }

    @Override
    public String toString() {
        return "BaseMessage{" +
                "toUserName='" + toUserName + '\'' +
                ", fromUserName='" + fromUserName + '\'' +
                ", createTime=" + createTime +
                ", msgType='" + msgType + '\'' +
                '}';
    }
}
