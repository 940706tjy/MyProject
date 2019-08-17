package cn.bulaomeng.fragment.util;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import java.io.*;
import java.util.Iterator;
import java.util.Map;


public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    //AntiSamy使用的策略文件
    private static Policy policy = null;
    static {
        String antiSamyPath = XssHttpServletRequestWrapper.class.getClassLoader().getResource("antisamy-querysys.xml").getFile();
        System.out.println("policy_filepath:"+antiSamyPath);
        if(antiSamyPath.startsWith("file")){
            antiSamyPath = antiSamyPath.substring(6);
        }
        try {
            policy = Policy.getInstance(antiSamyPath);
        } catch (PolicyException e) {
            e.printStackTrace();
        }
    }

    // 存放JSON数据主体
    private final String body;
    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes("UTF-8"));
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    public String getBody() {
        return this.body;
    }
    /** 
    * @Description: 将返回给过滤器的参数进行清洗
    * @Param: [request] 
    * @return:  
    * @Author: tjy
    * @Date: 2019/8/17 
    */ 
    public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            //获得POST请求的输入流
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[1024];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuffer.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuffer.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        body =cleanXSS(stringBuffer.toString());
    }

   /** 
   * @Description: Header为空直接返回，不然进行XSS清洗 
   * @Param: [name] 
   * @return: java.lang.String 
   * @Author: tjy
   * @Date: 2019/8/17 
   */ 
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if(StringUtils.isEmpty(value)){
            return value;
        }
        else{
            String newValue = cleanXSS(value);
            return newValue;
        }

    }

    /** 
    * @Description: Parameter为空直接返回，不然进行XSS清洗 
    * @Param: [name] 
    * @return: java.lang.String 
    * @Author: tjy
    * @Date: 2019/8/17 
    */ 
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if(StringUtils.isEmpty(value)){
            return value;
        }
        else{
            value = cleanXSS(value);
            return value;
        }
    }

   /** 
   * @Description: 对用户输入的参数值进行XSS清洗 
   * @Param: [name] 
   * @return: java.lang.String[] 
   * @Author: tjy
   * @Date: 2019/8/17 
   */ 
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapseValues[i] = cleanXSS(values[i]);
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }


    @SuppressWarnings("rawtypes")
    public Map<String,String[]> getParameterMap(){
        Map<String,String[]> request_map = super.getParameterMap();
        Iterator iterator = request_map.entrySet().iterator();
        System.out.println("request_map"+request_map.size());
        while(iterator.hasNext()){
            Map.Entry me = (Map.Entry)iterator.next();
            //System.out.println(me.getKey()+":");
            String[] values = (String[])me.getValue();
            for(int i = 0 ; i < values.length ; i++){
                System.out.println(values[i]);
                values[i] = cleanXSS(values[i]);
            }
        }
        return request_map;
    }


    /** 
    * @Description: AntiSamy清洗数据 
    * @Param: [taintedHTML] 
    * @return: java.lang.String 
    * @Author: tjy
    * @Date: 2019/8/17 
    */ 
    private String cleanXSS(String taintedHTML) {
        try{
            AntiSamy antiSamy = new AntiSamy();
            CleanResults cr = antiSamy.scan(taintedHTML, policy);//扫描
            taintedHTML = cr.getCleanHTML();//获取清洗后的结果
            //AntiSamy会把“&nbsp;”转换成乱码，把双引号转换成"&quot;" 先将&nbsp;的乱码替换为空，双引号的乱码替换为双引号
            String str = StringEscapeUtils.unescapeHtml4(taintedHTML);
            str = str.replaceAll(antiSamy.scan("&nbsp;",policy).getCleanHTML(),"");
            str = str.replaceAll(antiSamy.scan("\"",policy).getCleanHTML(),"\"");
            return str;
        }catch( ScanException e) {
            e.printStackTrace();
        }catch( PolicyException e) {
            e.printStackTrace();
        }
        return taintedHTML;
    }

}
