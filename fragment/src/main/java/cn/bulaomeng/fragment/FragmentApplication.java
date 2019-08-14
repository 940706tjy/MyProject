package cn.bulaomeng.fragment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
@RestController
public class FragmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(FragmentApplication.class, args);
    }

}
