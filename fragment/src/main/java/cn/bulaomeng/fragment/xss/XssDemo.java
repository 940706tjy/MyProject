package cn.bulaomeng.fragment.xss;

import org.owasp.validator.html.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class XssDemo {

    public static void main(String[] args) throws FileNotFoundException, IOException, PolicyException, ScanException {
        String xsshtml = "hyf<script>alert(1)</script>";
        Policy policy = Policy.getInstance("antisamy-ebay.xml");
        AntiSamy antiSamy = new AntiSamy();
        CleanResults cr = antiSamy.scan(xsshtml,policy);
        xsshtml = cr.getCleanHTML(); //清洗完的
        System.out.println(xsshtml);
    }
}
