package cn.bulaomeng.fragment.restTmplate;


import cn.bulaomeng.fragment.entity.Fragment;
import cn.bulaomeng.fragment.service.CreateAppSing;
import cn.bulaomeng.fragment.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class demo {
    private static final String QR_CODE_IMAGE_PATH = "D:\\ideaProject\\fragment\\src\\main\\resources\\code/blmMsgRQCode.png";

    private static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }

    public static void main(String[] args) throws IOException, ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date =new Date();
        String currentDate = sdf.format(date);
        //记录当前时间
        Long currentFlag = Long.parseLong(currentDate);
        System.out.println(currentFlag);


      /*  CreateAppSing createAppSing = new CreateAppSing();
        createAppSing.setAppSign(" 1");

        Map<String,Object> map = new HashMap<>();
      *//*  map.put("appId","rft123456");
        map.put("nonceStr","5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
        map.put("appSign","D363A06B5C35CF99C85CA477FCE8B36B");*//*
        map.put("name","清");
        RestTemplate restTemplate = new RestTemplate();
        JSONObject js=restTemplate.postForObject("http://47.104.84.65/fragment/selectByPrimaryKey",map, JSONObject.class);
        System.out.println("签名:---"+js);*/
       /* RestTemplate restTemplate = new RestTemplate();
        Map<String,Object> map = new HashMap<>();
        map.put("exchange","");
        map.put("haveFragment","蛇");
        map.put("name","");
        String data=restTemplate.postForObject("http://47.104.84.65/fragment/selectByPrimaryKey",new Fragment(),String.class);
        if("ok".equals( JSONObject.parseObject(data).get("msg"))){
            System.out.println(1);
        }
        System.out.println(data);*/

        //System.out.println(JSONObject.parseObject(data).get("msg"));
       /* try {
            generateQRCodeImage("http://blm.free.idcfengye.com/message.html", 350, 350, QR_CODE_IMAGE_PATH);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }*/

    }

  /*  public static void main(String[] args) {
        List<Integer> a=new ArrayList<>();
        List<Integer> a1=new ArrayList<>();
        a.add(1);
        a1.add(1);
        a1.add(2);
        a.add(2);
        a1.removeAll(a);
       a=null;
       a1.addAll(a);
        System.out.println(a1);

    }*/
}
