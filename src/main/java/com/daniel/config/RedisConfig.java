package com.daniel.config;

import com.daniel.serializer.MyStringRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Package: com.daniel.config
 * @ClassName: RedisConfig
 * @Author: daniel
 * @CreateTime: 2021/1/30 12:50
 * @Description: 引入我们自定义Redis序列化方法，使得Redis返回的全是String类型
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String,Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //引入我们自定义序列化的类
        MyStringRedisSerializer myStringRedisSerializer = new MyStringRedisSerializer();

        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(myStringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(myStringRedisSerializer);

        return template;
    }
}
