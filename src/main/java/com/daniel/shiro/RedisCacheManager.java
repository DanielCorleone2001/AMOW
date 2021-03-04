package com.daniel.shiro;

import com.daniel.service.redis.RedisService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Package: com.daniel.shiro
 * @ClassName: ShiroCacheManager
 * @Author: daniel
 * @CreateTime: 2021/2/1 12:42
 * @Description: 获取redis缓存
 */
public class RedisCacheManager implements CacheManager {

    @Autowired
    private RedisService redisService;
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new RedisCache<>(redisService);
    }
}
