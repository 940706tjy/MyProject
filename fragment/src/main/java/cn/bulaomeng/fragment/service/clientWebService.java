package cn.bulaomeng.fragment.service;

import javax.xml.ws.Endpoint;

public class clientWebService {
    public static void main(String[] args) {
        Endpoint.publish("http://127.0.0.1:12345/weather", new WebServiceDemo());
    }
}
