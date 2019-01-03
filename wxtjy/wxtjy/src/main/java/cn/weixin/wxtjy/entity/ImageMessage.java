package cn.weixin.wxtjy.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;
@XStreamAlias("xml")
public class ImageMessage extends BaseMessage{
    @XStreamAlias("MediaId")
    private String mediaId;

    public ImageMessage(Map<String, String> map,String mediaId) {
        super(map);
        this.setMsgType("image");
        this.mediaId=mediaId;
    }
    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }
}
