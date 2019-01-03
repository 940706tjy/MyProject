package cn.weixin.wxtjy.util;

import cn.weixin.wxtjy.entity.Article;
import cn.weixin.wxtjy.entity.BaseMessage;
import cn.weixin.wxtjy.entity.NewsMessage;
import cn.weixin.wxtjy.entity.TextMessage;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtil {
    /**
     * 解析XML
     * @param is
     * @return
     */
    public static Map<String,String> xmlToMap(InputStream is) {
        Map<String,String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        try {
            Document document=reader.read(is);
            Element root=document.getRootElement();
            List<Element>list=root.elements();
            for (Element element:list) {
                map.put(element.getName(),element.getStringValue());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }
	public static BaseMessage getFlag(Map<String,String>map) {
        BaseMessage bm=null;
        String flag=null;
        List<Article>list=new ArrayList<Article>();
        Article article=new Article();
        switch (map.get("Content")){
            case "1":flag="输入2领取红包!";
                break;
            case "2":flag="继续输入3马上揭开!";
                break;
            case "3":flag="红包就在眼前了,输入4加速!";
                break;
            case "4":flag="只剩一层纸了,输入5继续加速";
                break;
            case "5":flag="/***\n" +
                    " * --------------攻城狮--------------\n" +
                    " *             ,%%%%%%%%,\n" +
                    " *           ,%%/\\%%%%/\\%%\n" +
                    " *          ,%%%\\c \"\" J/%%%\n" +
                    " * %.       %%%%/ o  o \\%%%\n" +
                    " * `%%.     %%%%    _  |%%%\n" +
                    " *  `%%     `%%%%(__Y__)%%'\n" +
                    " *  //       ;%%%%`\\-/%%%'\n" +
                    " * ((       /  `%%%%%%%'\n" +
                    " *  \\\\    .'          |\n" +
                    " *   \\\\  /       \\  | |\n" +
                    " *    \\\\/         ) | |\n" +
                    " *     \\         /_ | |__\n" +
                    " *     (___________))))))) \n" +
                    " *-----------------------------------\n" +
                    " */";
                break;
            case "图":
                article.setDescription("我是图文描述");
                article.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/EWOcGkQBsf81CReJnwSgib1HfoDaWnJkDYoNvDEjznZeqCHtBp8BV3FfFTvgaL1RsDMaGplYUqKtliaayMEHzicVA/0");
                article.setTitle("我是图文头");
                article.setUrl("https://www.bilibili.com/video/av6945785/?p=2");
                list.add(article);
                bm=new NewsMessage(map,list);
                break;
            case "睡觉":
                article.setDescription("我是图文描述");
                article.setPicUrl("http://mmbiz.qpic.cn/mmbiz_jpg/EWOcGkQBsf81CReJnwSgib1HfoDaWnJkDjlqk0DmsQlobiblY1cS2yicauWFdphLsoiceChFh0oatia4Wm10tmnlfAw/0");
                article.setTitle("我是图文头");
                article.setUrl("https://www.bilibili.com/video/av6945785/?p=2");
                list.add(article);
                bm=new NewsMessage(map,list);
                break;
            default: flag="请输入1";
                break;
        }
        if(flag!=null){
            bm=new TextMessage(map,flag);
        }
        return  bm;
    }
}
