package cn.weixin.wxtjy.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;
@XStreamAlias("xml")
public class MusicMessage extends BaseMessage{

    private Music music;
    public MusicMessage(Map<String, String> map,Music music) {
        super(map);
        this.setMsgType("music");
        this.music=music;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
