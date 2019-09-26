package cn.bulaomeng.fragment.redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class TestRedis {
    @Autowired
    static RedisTemplate<String,Object> redisTemplate;
    public static void main(String[] args) {
        //
        System.out.println("OPT02BM0011".substring(3));
        Integer num =  "BM0011".length();

        show("tutorial-name");


    }
    public static String show(String data){
       return redisTemplate.opsForValue().get(data).toString();
    }
}
