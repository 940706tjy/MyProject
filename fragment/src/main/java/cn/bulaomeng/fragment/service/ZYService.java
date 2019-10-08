package cn.bulaomeng.fragment.service;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService
public class ZYService {

    public String HelloWord(String name) {
        return "Hello: " + name;
    }

    /**
     * 添加exclude=true后，HelloWord2()方法不会被发布
     *
     * @param name
     * @return
     */
    @WebMethod(exclude = true)
    public String HelloWord2(String name) {
        return "Hello: " + name;
    }

    public static void main(String[] args) {
    /**
     *参数1：服务的发布地址
     *参数2：服务的实现者
     */
        Endpoint.publish("http://localhost:8095/helloWord", new ZYService());

    }

    public String getSoapHeader(){
        Integer nThirdType = 0;
        Integer nSecret1 = 0;
        String nSecret2 = "";
        //上面代码为从缓存中取到我们需求传递到认证头的数据 下面开始添加认证头
        StringBuffer soapHeader = new StringBuffer();
        soapHeader.append("<soap:Header>");
        soapHeader.append("<SecurityHeader xmlns=\"http://www.hzsun.com/\">");
        soapHeader.append("<nThirdType>"+nThirdType+"</nThirdType>");
        soapHeader.append("<nSecret1>"+nSecret1+"</nSecret1>");
        soapHeader.append("<nSecret2>"+nSecret2+"</nSecret2>");
        soapHeader.append("</SecurityHeader>");
        soapHeader.append("</soap:Header>");
        return soapHeader.toString();
    }

    public String getAccInfoXml(String sIDNo, int nIDType){
        StringBuffer template = new StringBuffer();
        String header = getSoapHeader();
        template.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
        template.append(header);
        template.append("<soap:Body>");
        template.append("<GetAccInfo xmlns=\"http://www.hzsun.com/\">");
        template.append("<sIDNo>"+sIDNo+"</sIDNo>");
        template.append("<nIDType>"+nIDType+"</nIDType>");
        template.append("</GetAccInfo>");
        template.append("</soap:Body>");
        template.append("</soap:Envelope>");
        return template.toString();
    }


}
