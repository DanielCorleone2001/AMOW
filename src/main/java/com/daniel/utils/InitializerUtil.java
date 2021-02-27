package com.daniel.utils;

import com.daniel.utils.jwt.JWToken;
import com.daniel.utils.token.TokenConfig;
import org.springframework.stereotype.Component;

/**
 * @Package: com.daniel.utils
 * @ClassName: InitializerUtil
 * @Author: daniel
 * @CreateTime: 2021/1/30 22:20
 * @Description:
 */
@Component//注册到容器中
public class InitializerUtil {
    private TokenConfig tokenConfig;
    public InitializerUtil(TokenConfig tokenConfig) {
        JWToken.initJWT(tokenConfig);
    }
}
