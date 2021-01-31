package com.daniel.shiro;

import com.daniel.contains.Constant;
import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.service.RedisService;
import com.daniel.utils.JWToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;

/**
 * @Package: com.daniel.shiro
 * @ClassName: My
 * @Author: daniel
 * @CreateTime: 2021/1/31 22:32
 * @Description:
 */
@Slf4j
public class MyHashedCredentialsMatcher extends HashedCredentialsMatcher {
    @Autowired
    private RedisService redisService;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        MyPasswordToken passwordToken = (MyPasswordToken)token;
        String accessToken = (String) passwordToken.getPrincipal();
        String userId = JWToken.getUserId(accessToken);

        /**
         * 判断用户是否被锁定
         */
        if(redisService.hasKey(Constant.ACCOUNT_LOCK_KEY+userId)){
            throw new BusinessException(BaseResponseCode.ACCOUNT_LOCK);
        }
        /**
         * 判断用户是否被删除
         */
        if(redisService.hasKey(Constant.DELETED_USER_KEY+userId)){
            throw new BusinessException(BaseResponseCode.ACCOUNT_HAS_DELETED_ERROR);
        }
        /**
         * 判断token是否通过校验
         */
        if(!JWToken.validateToken(accessToken)){
            throw new BusinessException(BaseResponseCode.TOKEN_PAST_DUE);
        }

        return true;
    }
}
