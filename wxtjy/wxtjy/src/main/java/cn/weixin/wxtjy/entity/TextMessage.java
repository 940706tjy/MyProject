package cn.weixin.wxtjy.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;
@XStreamAlias("xml")
public class TextMessage extends BaseMessage {
    @XStreamAlias("Content")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        content = content;
    }

    public TextMessage(Map<String,String> map,String content) {
        super(map);
        this.setMsgType("text");
        this.content = content;
    }
}
