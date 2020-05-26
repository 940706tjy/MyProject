package cn.bulaomeng.fragment.config.log.logaspect;

import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 不过滤路径判断
 * Created by zhangchao on 2019/3/7.
 */
public class IncludePath {

    private static List<Pattern> patterns = new ArrayList<>();

    /**
     * 是否包含该url
     *
     * @param url
     * @return
     */
    public static boolean includePath(String url) {
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 静态变量注入
     *  : 处理默认不配置不报错，注入 空字符串
     *
     * @param wedsFrameworkExcludePath
     */
    @Value("${weds.framework.exclude.path:}")
    public static void setWedsFrameworkExcludePath(String wedsFrameworkExcludePath) {
        //默认的不过滤路径
        patterns.add(Pattern.compile("v2/api-docs.*"));
        patterns.add(Pattern.compile("configuration/ui.*"));
        patterns.add(Pattern.compile("swagger-resources.*"));
        patterns.add(Pattern.compile("configuration/security.*"));
        patterns.add(Pattern.compile("swagger-ui.html"));
        patterns.add(Pattern.compile("spec"));
        patterns.add(Pattern.compile("healthy"));
        patterns.add(Pattern.compile("webjars.*"));
        patterns.add(Pattern.compile("index.html"));
        patterns.add(Pattern.compile("static.*"));
        patterns.add(Pattern.compile("images.*"));
        patterns.add(Pattern.compile("att.*"));
        patterns.add(Pattern.compile("img.*"));
        patterns.add(Pattern.compile("imageRe.*"));
        patterns.add(Pattern.compile("favicon.ico"));
        if (null != wedsFrameworkExcludePath && !"".equals(wedsFrameworkExcludePath)) {
            String[] excludePathArr = wedsFrameworkExcludePath.split(",");
            for (String excludePath : excludePathArr) {
                patterns.add(Pattern.compile(excludePath));
            }
        }
    }
}
