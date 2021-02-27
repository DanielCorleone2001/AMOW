package com.daniel.utils.token;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @Package: com.daniel.utils
 * @ClassName: TokenConfig
 * @Author: daniel
 * @CreateTime: 2021/1/30 21:44
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")//映射到配置文件里的配置
@Data//get set 方法
public class TokenConfig {
    private String securityKey;
    private Duration accessTokenExpireTime;
    private Duration refreshTokenExpireTime;
    private Duration refreshTokenExpireAppTime;
    private String issuer;

}
