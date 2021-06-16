package cn.bulaomeng.fragment.test.qrcode;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImgTest {
    private Image img;
    private final static int WIDTH = 147;
    private final static int HEIGHT = 136;

    /**
     * 改变图片的大小到宽为size，然后高随着宽等比例变化
     * @param is 上传的图片的输入流
     * @param os 改变了图片的大小后，把图片的流输出到目标OutputStream
     * @param size 新图片的宽
     * @param format 新图片的格式
     * @throws IOException
     */
    public static OutputStream resizeImage(InputStream is, OutputStream os, int size, String format) throws IOException {
        BufferedImage prevImage = ImageIO.read(is);
        double width = prevImage.getWidth();
        double height = prevImage.getHeight();
        double percent = size/width;
        int newWidth = (int)(width * percent);
        int newHeight = (int)(height * percent);
        BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);
        Graphics graphics = image.createGraphics();
        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);
        ImageIO.write(image, format, os);
        os.flush();
        is.close();
        os.close();
        ByteArrayOutputStream b = (ByteArrayOutputStream) os;
        return os;
    }
    
    public static void main(String[] args) {
        try {
            InputStream is = new FileInputStream(new File("D:\\J-USER\\desktop7\\news\\123.png"));
            OutputStream os = new FileOutputStream(new File("D:\\J-USER\\desktop7\\news\\test.png"));
            resizeImage(is, os, 10, "png");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}