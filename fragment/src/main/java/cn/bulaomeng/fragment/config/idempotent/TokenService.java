package cn.bulaomeng.fragment.config.idempotent;


import javax.servlet.http.HttpServletRequest;

/**
 * token相关接口
 *
 * @author tjy
 * @date 2021/3/17
 **/
public interface TokenService {
    /**
     * 创建token
     */
    String createToken();

    /**
     * 检验token
     */
    boolean checkToken(HttpServletRequest request);


}
