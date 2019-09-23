package cn.bulaomeng.fragment.service;

import java.util.Map;

//请求公共接口
public class ParamRestTemplate {
    private Map<String,Object> param; //请求参数
    private String url; //请求地址

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
