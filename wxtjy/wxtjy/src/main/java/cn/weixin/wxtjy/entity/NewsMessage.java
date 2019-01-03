package cn.weixin.wxtjy.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;
import java.util.Map;
@XStreamAlias("xml")
public class NewsMessage extends BaseMessage {
    @XStreamAlias("ArticleCount")
    private String articleCount;
    @XStreamAlias("Articles")
    private List<Article> articles;

    public NewsMessage(Map<String, String> map, List<Article> articles) {
        super(map);
        setMsgType("news");
        this.articleCount = articles.size()+"";
        this.articles = articles;
    }

    public String getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(String articleCount) {
        this.articleCount = articleCount;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
