package com.daniel.config;

import com.daniel.shiro.MyHashedCredentialsMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Package: com.daniel.config
 * @ClassName: ShiroConfig
 * @Author: daniel
 * @CreateTime: 2021/1/31 22:54
 * @Description:
 */
@Configuration
public class ShiroConfig {

    @Bean
    public MyHashedCredentialsMatcher myHashedCredentialsMatcher () {
        return new MyHashedCredentialsMatcher();
    }


}
