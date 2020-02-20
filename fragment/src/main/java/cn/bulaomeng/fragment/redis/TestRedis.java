package cn.bulaomeng.fragment.redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashSet;
import java.util.Set;

public class TestRedis {
    @Autowired
    static RedisTemplate<String,Object> redisTemplate;
    public static void main(String[] args) {
        //
       /* System.out.println("OPT02BM0011".substring(3));
        Integer num =  "BM0011".length();

        show("tutorial-name");*/
        Set<String> s1 = new HashSet<String>(); // 创建一个集合s1

        Set<String> s2 = new HashSet<String>(); // 创建一个集合s2
        s2.add("3");
        s2.add("4");

        s1.addAll(s2); // 将s2中的数据添加到s1中

        for (String str : s1) { // 输出s1
            System.out.print(str);
        }


    }
    public static String show(String data){
       return redisTemplate.opsForValue().get(data).toString();
    }
}
