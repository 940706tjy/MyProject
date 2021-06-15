package cn.bulaomeng.fragment.practice.stream;


import java.util.Arrays;
import java.util.List;

public class StreamTest1 {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

//结果：1234567
        list.stream().forEach(System.out::print);
        System.out.println("*********************");
//结果：5726134
        list.parallelStream().forEach(System.out::print);
        System.out.println("*********************");

//结果：1234567
        list.parallelStream().forEachOrdered(System.out::print);

    }
}
