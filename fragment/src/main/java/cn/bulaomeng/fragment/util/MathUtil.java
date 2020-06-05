package cn.bulaomeng.fragment.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MathUtil {

    /**
     * 计算百分比
     * @author tjy
     * @date 2020/6/5
     **/
    public static String accuracy(double num, double total, int scale){
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracyNum = num / total * 100;
        return df.format(accuracyNum)+"%"; // 53.8 45
    }

    public static void main(String[] args) {
        double num = 1850;
        double num2 = 1547;
        double num3 = 42;

        int total = 3439;
        int scale = 1;
        String result = accuracy(num3, total, scale);
        System.out.println(result);
    }
}
