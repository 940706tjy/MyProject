package cn.weixin.wxtjy.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;
@XStreamAlias("xml")
public class VoiceMessage extends BaseMessage{
    @XStreamAlias("MediaId")
    private String mediaId;
    public VoiceMessage(Map<String, String> map,String mediaId ) {
        super(map);
        this.setMsgType("voice");
        this.mediaId=mediaId;
    }
    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
