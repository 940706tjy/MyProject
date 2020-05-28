package cn.bulaomeng.fragment.config.log.logaspect;

import cn.bulaomeng.fragment.util.JsonUtil;
import cn.bulaomeng.fragment.util.request.IpUtil;
import cn.bulaomeng.fragment.util.request.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 日志切面
 *
 * @date 2018/12/21 15:00
 */
@Aspect
@Component
@Slf4j
public class LogAspect {




    @Pointcut("(@annotation(org.springframework.web.bind.annotation.RequestMapping)) " +
            "|| (@annotation(org.springframework.web.bind.annotation.GetMapping)) " +
            "|| (@annotation(org.springframework.web.bind.annotation.PostMapping)) ")
    private void logAspect() {
    }

    /**
     * 日志切面
     *
     * @param pjp 连接点
     * @return java.lang.Object
     */
    @SuppressWarnings("unchecked")
    @Around("logAspect()")
    public Object doAround(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();
        Object object = null;
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != servletRequestAttributes) {

            HttpServletRequest request = servletRequestAttributes.getRequest();
            String requestIp = IpUtil.getIpAddr(request);

            // 拦截的实体类，就是当前正在执行的controller
            Object target = pjp.getTarget();
            //类名
            String className = target.getClass().getSimpleName();
            // 拦截的方法名称。当前正在执行的方法
            String methodName = pjp.getSignature().getName();
            // 拦截的方法参数
            Object[] args = pjp.getArgs();
            // 拦截的类型
            Signature sig = pjp.getSignature();
            MethodSignature msig;
            try {
                if (!(sig instanceof MethodSignature)) {
                    throw new CommonException(400, "该注解只能用于方法");
                }
            } catch (CommonException e) {
                log.error("occurs error ", e);
            }
            msig = (MethodSignature) sig;
            String[] parameterNames = msig.getParameterNames();
            StringBuilder requestMethod = new StringBuilder(200);
            requestMethod.append(className).append(".");
            requestMethod.append(methodName).append("() ");
            StringBuilder requestInfo = new StringBuilder(200);
            requestInfo.append(requestMethod);
            requestInfo.append(" 请求方式 [").append(request.getMethod()).append("] 请求IP [").append(requestIp).append("] params [ ");
            int i = 0;

            //这里把参数日志去掉，因为base64图片的字符串实在是太大了，几个请求文件就压满了。
            /*for (String parameter : parameterNames) {
                requestInfo.append(parameter).append(" ");
                requestInfo.append(args[i++]).append(" ");
            }*/
            requestInfo.append(" ] ");
            //log.info(requestInfo.toString());
            long duration = 0l;
            try {
                object = pjp.proceed();
                duration = System.currentTimeMillis() - startTime;
            } catch (CommonException e) {
                JsonResult jsonResult = new JsonResult(e.getCode(), e.getMsg());
                log.error(" [{}] [{}] occurs error ", className, methodName, e);
                return jsonResult;
            } catch (Exception e) {
                JsonResult jsonResult = new JsonResult(500, "异常");
                log.error(" [{}] [{}] occurs exception  ", className, methodName, e);
                return jsonResult;
            } catch (Throwable throwable) {
                JsonResult jsonResult = new JsonResult(500, "异常");
                log.error(" [{}] [{}] occurs exception ", className, methodName, throwable);
                return jsonResult;
            }
            if (!Objects.isNull(object)) {
                //返回值字符串超过300自动截取前300
                String response = JsonUtil.obj2string(object);
                if (response.length() > 300) {
                    response = response.substring(0, 300);
                }
                byte[] objectByte = object.toString().getBytes();
                //计算kb
                int objectSize = objectByte.length / 1024;
                log.info(String.valueOf(requestInfo.append(" response [ ").append(response).append(" ] response size ").append(objectSize).append(" kb 耗时 ").append(duration).append(" ms ")));
            } else {
                log.info(String.valueOf(requestInfo.append(" response is null ").append(" 耗时 ").append(duration).append(" ms ")));
            }
        } else {
            try {
                object = pjp.proceed();
            } catch (Exception e) {
                log.error("  occurs exception ", e);
            } catch (Throwable throwable) {
                log.error("  occurs throwable ", throwable);
            }
        }
        return object;
    }

}
