package cn.bulaomeng.fragment.test.qrcode;


import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *  参考：https://www.cnblogs.com/huacanfly/p/9908408.html
 *  参考：https://blog.csdn.net/StrangeSir/article/details/93143177
 *  参考：https://opencv.org/releases/
 */
public class    OpenCVQrCodePosition {
    private boolean isDebug = true;
    static {
// 加载动态库
        System.load(System.getProperty("user.dir")+"/lib/opencv_java452.dll");
    }

    // 获取二维码图片位置信息
    public Point[] getPosition(String imgUrl){
        Mat src = Imgcodecs.imread(imgUrl ,1);
        Mat src_gray = new Mat();

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        //彩色图转灰度图
        Imgproc.cvtColor(src ,src_gray ,Imgproc.COLOR_RGB2GRAY);
        //对图像进行平滑处理
        Imgproc.GaussianBlur(src_gray, src_gray, new Size(3,3), 0);
        this.printImg("1-1.jpg", src_gray);

        Imgproc.Canny(src_gray,src_gray,112,255);

        this.printImg("1-2.jpg", src_gray);
        Mat hierarchy = new Mat();

        // 寻找轮廓
        Imgproc.findContours(src_gray ,contours ,hierarchy ,Imgproc.RETR_TREE ,Imgproc.CHAIN_APPROX_NONE);
        List<MatOfPoint> markContours =  this.markContour(src_gray, contours, hierarchy);
        markContours = this.filterByAngle(markContours);
        Point[] points =  this.centerCals(markContours);
        if(points != null){
            this.cutRect(src, points, 12);
        }
        return points;
    }

    //  裁剪指定区域
    public BufferedImage cutRect(Mat src, Point[] points, int offset){
        if(points == null || points.length < 3){
            System.out.println("-------没有点---------");
            return null;
        }
        int left_x = (int)Math.min(points[0].x, Math.min(points[1].x, points[2].x)) - offset - 50;
        int right_x = (int)Math.max(points[0].x, Math.max(points[1].x, points[2].x)) + offset + 58;
        int y = (int)Math.min(points[0].y, Math.min(points[1].y, points[2].y)) - offset - 50;
        System.out.println("left ==>" + left_x);
        System.out.println("right ==>" + right_x);
        System.out.println("y ==>" + y);
        Rect roiArea = new Rect(left_x, y, right_x - left_x, right_x - left_x);
        Mat dstRoi = new Mat(src, roiArea);
        this.printImg("cutRect.jpg", dstRoi);
        return mat2BufImg(dstRoi, ".png");
    }

    // 通过角度筛选点， 只处理正常角度二维码
    private List<MatOfPoint> filterByAngle(List<MatOfPoint> markContours) {
        // 二维码有三个角轮廓，少于三个的无法定位放弃，多余三个的循环裁剪出来
        if (markContours.size() < 3){
            return null;
        }else{
            for (int i=0; i<markContours.size()-2; i++){
                List<MatOfPoint> threePointList = new ArrayList<>();
                for (int j=i+1;j<markContours.size()-1; j++){
                    for (int k=j+1;k<markContours.size();k++){
                        threePointList.add(markContours.get(i));
                        threePointList.add(markContours.get(j));
                        threePointList.add(markContours.get(k));
                        if(capture(threePointList)){
                            return threePointList;
                        }else{
                            threePointList.clear();
                        }
                    }
                }
            }
        }
        return null;
    }

    // 计算三个点是否符合 90，45，45
    private boolean capture(List<MatOfPoint> contours){
        Point[] pointthree = this.centerCals(contours);
        double angle1 = this.angle(pointthree[1],  pointthree[0],  pointthree[2]);
        double angle2 = this.angle(pointthree[0],  pointthree[1],  pointthree[2]);
        double angle3 = this.angle(pointthree[1],  pointthree[2],  pointthree[0]);

        System.out.println("angle1:"+angle1+",angle2:"+angle2+",angle3:"+angle3);
        if (Double.isNaN(angle1) || Double.isNaN(angle2) || Double.isNaN(angle3)){
            return false;
        }

        // 最大角度和最小角度
        double maxAngle = Math.max(angle3,Math.max(angle1,angle2));
        double minAngle = Math.min(angle3,Math.min(angle1,angle2));
        if (maxAngle<85 || maxAngle>95 || minAngle < 40 || minAngle > 50){ /**二维码为直角，最大角过大或者过小都判断为不是二维码*/
            return false;
        }
        return true;
    }

    //  计算夹角
    private double angle(Point p1, Point p2, Point p3){
        double[] ca = new double[2];
        double[] cb = new double[2];

        ca[0] =  p1.x - p2.x;
        ca[1] =  p1.y - p2.y;
        cb[0] =  p3.x - p2.x;
        cb[1] =  p3.y - p2.y;
        return 180/3.1415*Math.acos((ca[0]*cb[0]+ca[1]*cb[1])/(Math.sqrt(ca[0]*ca[0]+ca[1]*ca[1])*Math.sqrt(cb[0]*cb[0]+cb[1]*cb[1])));
    }
    // 转换为点
    private Point[] centerCals(List<MatOfPoint> matOfPoint){
        if(matOfPoint == null || matOfPoint.size() == 0){
            return null;
        }
        Point[] pointthree = new Point[matOfPoint.size()];
        for(int i=0; i<matOfPoint.size(); i++){
            pointthree[i] = centerCal(matOfPoint.get(i));
        }
        return pointthree;
    }

    // 转换为中心点
    private Point centerCal(MatOfPoint matOfPoint){
        double centerx=0,centery=0;
        MatOfPoint2f mat2f = new MatOfPoint2f( matOfPoint.toArray() );
        RotatedRect rect = Imgproc.minAreaRect( mat2f );
        Point vertices[] = new Point[4];
        rect.points(vertices);
        centerx = ((vertices[0].x + vertices[1].x)/2 + (vertices[2].x + vertices[3].x)/2)/2;
        centery =  ((vertices[0].y + vertices[1].y)/2 + (vertices[2].y + vertices[3].y)/2)/2;
        Point point= new Point(centerx,centery);
        return point;
    }


    // 寻找二维码轮廓点
    private List<MatOfPoint> markContour(Mat src_gray, List<MatOfPoint> contours, Mat hierarchy) {
        List<MatOfPoint> markContours = new ArrayList<>();
        for (int i = 0; i< contours.size(); i++ ) {
            MatOfPoint2f newMtx = new MatOfPoint2f(contours.get(i).toArray() );
            RotatedRect rotRect = Imgproc.minAreaRect( newMtx );
            double w = rotRect.size.width;
            double h = rotRect.size.height;
            double rate =  Math.max(w, h)/Math.min(w, h) ;
            //长短轴比小于1.3，总面积大于60
            if (rate < 1.3 && w < src_gray.cols()/4 && h< src_gray.rows()/4 && Imgproc.contourArea(contours.get(i))>60) {
                //计算层数，二维码角框有五层轮廓（有说六层），这里不计自己这一层，有4个以上子轮廓则标记这一点
                double[] ds = hierarchy.get(0, i);
                if (ds != null && ds.length>3){
                    int count =0;
                    if (ds[3] == -1){/**最外层轮廓排除*/
                        continue;
                    }
                    //计算所有子轮廓数量
                    while ((int) ds[2] !=-1){
                        ++count;
                        ds = hierarchy.get(0 ,(int) ds[2]);
                    }
                    if (count >= 4){
                        markContours.add(contours.get(i));
                    }
                }
            }
        }
        return markContours;
    }

    // 测试打印图片
    private  void printImg(String name, Mat img){
        if(isDebug) {
            String path = "D:\\J-USER\\desktop7\\news\\" + name;
            System.out.println("out:" + path);
            Imgcodecs.imwrite(path, img);
        }
    }

    /**
     * Mat转换成BufferedImage
     *
     * @param matrix        要转换的Mat
     * @param fileExtension 格式为 ".jpg", ".png", etc
     * @return
     */
    public static BufferedImage mat2BufImg(Mat matrix, String fileExtension) {
        // convert the matrix into a matrix of bytes appropriate for
        // this file extension
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(fileExtension, matrix, mob);
        // convert the "matrix of bytes" into a byte array
        byte[] byteArray = mob.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImage;
    }

    /**
     * BufferedImage转换成Mat
     *
     * @param original
     *            要转换的BufferedImage
     * @param imgType
     *            bufferedImage的类型 如 BufferedImage.TYPE_3BYTE_BGR
     * @param matType
     *            转换成mat的type 如 CvType.CV_8UC3
     */
    public static Mat bufImg2Mat (BufferedImage original, int imgType, int matType) {
        if (original == null) {
            throw new IllegalArgumentException("original == null");
        }
        // Don't convert if it already has correct type
        if (original.getType() != imgType) {
            // Create a buffered image
            BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imgType);
            // Draw the image onto the new buffer
            Graphics2D g = image.createGraphics();
            try {
                g.setComposite(AlphaComposite.Src);
                g.drawImage(original, 0, 0, null);
            } finally {
                g.dispose();
            }
        }
        DataBufferByte dbi = (DataBufferByte) original.getRaster().getDataBuffer();
        byte[] pixels = dbi.getData();
        Mat mat = Mat.eye(original.getHeight(), original.getWidth(), matType);
        mat.put(0, 0, pixels);
        return mat;
    }

    /**
     * 解析读取二维码
     * 先使用ZXING二维码识别，若失败，使用OPENCV自带的二维码识别
     * 个人测试，两者的识别率差不多，都不尽人意，但一起使用还是可以略微提高一点识别率，毕竟实现算法不一样
     * 若还要其它的识别，类似Zbar，都可以集成进来
     *
     * @param qrCodePath 二维码图片路径
     * @return 成功返回二维码识别结果，失败返回null
     * @throws Exception
     */
    public static String decodeQRcode(String qrCodePath){
        String qrCodeText = null;
        try {
            BufferedImage image = ImageIO.read(new File(qrCodePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            qrCodeText = result.getText();
        } catch (Exception e) {
            qrCodeText = new QRCodeDetector().detectAndDecode(Imgcodecs.imread(qrCodePath, 1));
        }
        return qrCodeText;
    }

    public static void main(String[] args) {
        String img = "D:\\J-USER\\desktop7\\news\\1416928305_886138.jpg";
        Icon icon = null;
        try {
            icon = CompressPic.getRatioWidth(img, CompressPic.WIDTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(" ### " + icon); //生成新图片的位置；
        Point[] points =  new OpenCVQrCodePosition().getPosition(img);
        if(points == null) {
            System.out.println("可能不包含二维码:"+img);
            String newPath = "D:\\J-USER\\desktop7\\news\\tes1.jpg";
            try {
                ImageOptimizationUtil.binarization(img, newPath);
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(QRCodeUtils.deEncodeByPath(newPath));
            return;
        }
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];
            System.out.println("点" + i + ": " + "(" + p.x + "," + p.y + ")");
        }
        String fullPath = "D:\\J-USER\\desktop7\\news\\cutRect.jpg";
        String newPath = "D:\\J-USER\\desktop7\\news\\tes1.jpg";
        try {
            ImageOptimizationUtil.binarization(fullPath, newPath);
            TimeUnit.MILLISECONDS.sleep(100);
            System.out.println(QRCodeUtils.deEncodeByPath(newPath));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


