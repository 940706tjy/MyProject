package cn.bulaomeng.fragment.test.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * 作用：二维码识别（图片）
 * 类名：QRCodeUtils
 **/
public class QRCodeUtils {

    /**
     * 解析二维码,此方法解析一个路径的二维码图片
     * path:图片路径
     */
    public static String deEncodeByPath(String path) {
        String content = null;
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);//解码
            System.out.println("图片中内容：  ");
            System.out.println("content： " + result.getText());
            content = result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
			//这里判断如果识别不了带LOGO的图片，重新添加上一个属性
            try {
				image = ImageIO.read(new File(path));
				LuminanceSource source = new BufferedImageLuminanceSource(image);
				Binarizer binarizer = new HybridBinarizer(source);
				BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
				Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
				//设置编码格式
				hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
				//设置优化精度
				hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
				//设置复杂模式开启（我使用这种方式就可以识别微信的二维码了）
				hints.put(DecodeHintType.PURE_BARCODE,Boolean.TYPE);
				Result result = new MultiFormatReader().decode(binaryBitmap, hints);//解码
				System.out.println("图片中内容：  ");
				System.out.println("content： " + result.getText());
				content = result.getText();
			} catch (IOException | NotFoundException ex) {
				ex.printStackTrace();
			}
        }
        return content;
    }
}
