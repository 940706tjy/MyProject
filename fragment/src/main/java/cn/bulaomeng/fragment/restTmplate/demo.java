package cn.bulaomeng.fragment.restTmplate;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class demo {
    private static final String QR_CODE_IMAGE_PATH = "D:\\ideaProject\\fragment\\src\\main\\resources\\code/blmMsgRQCode.png";

    private static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

    }

    public static void main(String[] args) throws IOException {
        try {
            generateQRCodeImage("http://blm.free.idcfengye.com/message.html", 350, 350, QR_CODE_IMAGE_PATH);
        } catch (WriterException e) {
            System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
        }
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
