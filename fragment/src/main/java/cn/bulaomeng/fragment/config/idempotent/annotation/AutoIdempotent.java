package cn.bulaomeng.fragment.config.idempotent.annotation;

import java.lang.annotation.*;

/**
 * 接口幂等注解
 *
 * @author tjy
 * @date 2021/3/17
 **/

@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoIdempotent {
}
