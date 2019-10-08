package cn.bulaomeng.fragment.service;

import javax.jws.WebService;

@WebService
public class WebServiceDemo {

    public String queryWeather(String cityName) {
        System.out.println("获取城市名"+cityName);
        String weather="暴雨";
        return weather;
    }

}
