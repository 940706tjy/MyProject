package cn.bulaomeng.fragment.util.page;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by Administrator on 2018-03-03.
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {SearchOrderByValidator.class})
public @interface SearchOrderByValid {
    String message() default "排序字段不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
