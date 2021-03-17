package cn.bulaomeng.fragment.config.idempotent;

import cn.bulaomeng.fragment.constant.CommConstant;
import cn.bulaomeng.fragment.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisUtil redisService;

    /**
     * 创建token
     */
    @Override
    public String createToken() {
        String str = UUID.randomUUID().toString().replace("-", "");
        StringBuilder token = new StringBuilder();
        try {
            token.append(CommConstant.IDEMPOTENT_TOKEN_PARAM).append(str);
            // 将生成的token存入缓存
            redisService.set(CommConstant.IDEMPOTENT_TOKEN_PREFIX + token.toString(), token.toString(), 60);
            return token.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 检验token
     */
    @Override
    public boolean checkToken(HttpServletRequest request) {

        // 获取请求头上的token信息
        String token = request.getHeader(CommConstant.IDEMPOTENT_TOKEN_PREFIX_HEAD);
        // header中不存在token则获取参数上的token
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(CommConstant.IDEMPOTENT_TOKEN_PREFIX_HEAD);
            // parameter中也不存在token
            if (StringUtils.isEmpty(token)) {
                throw new ServiceException("601", "参数异常");
            }
        }

        if (redisService.get(CommConstant.IDEMPOTENT_TOKEN_PREFIX + token) == null) {
            throw new ServiceException("601", "重复请求接口");
        }
        // 操作成功后，删除缓存
        redisService.del(CommConstant.IDEMPOTENT_TOKEN_PREFIX + token);
        return true;
    }
}
 