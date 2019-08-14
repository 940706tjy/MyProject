package cn.bulaomeng.fragment.io;

import java.io.*;

public class TestIo {
    public static void main(String[] args) throws IOException {
        File file = new File("d://testio/test.txt");
        FileInputStream fis = null;
        try {
            //创建一个文件输入流
            fis = new FileInputStream(file);
            //创建一个字节数组用来存储读取的信息
            byte[] buf = new byte[1024];
            //len表示读取的长度
            int len = 0;
            //只要len大于-1说明读取到元素，可对元素直接进行操作
            while((len=fis.read(buf))>-1){
                //通过控制台输出程序，需要指明输出的长度
                System.out.println(len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if (fis!=null) {
                    //操作完成之后关闭流
                    fis.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
