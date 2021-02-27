package com.daniel.config;

import com.daniel.shiro.MyAccessControlFilter;
import com.daniel.shiro.MyHashedCredentialsMatcher;
import com.daniel.shiro.MyRealm;
import com.daniel.shiro.RedisCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public RedisCacheManager cacheManager() {
        return new RedisCacheManager();
    }

    @Bean
    public MyHashedCredentialsMatcher myHashedCredentialsMatcher () {
        return new MyHashedCredentialsMatcher();
    }

    @Bean
    public MyRealm myRealm() {//自定义bean，配置我们自定义的HashedCredentialsMatcher，再返回自定义的RealM
        MyRealm myRealm = new MyRealm();
        myRealm.setCredentialsMatcher(myHashedCredentialsMatcher());
        myRealm.setCacheManager(cacheManager());
        return myRealm;
    }

    @Bean//注入SecurityManager
    public SecurityManager securityManager() {//自定义bean，配置我们自定义的RealM，再返回SecurityManager
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm());
        return securityManager;
    }



    @Bean//配置一些要放行的url以及要拦截认证的url
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //自定义拦截器限制并发人数
        LinkedHashMap<String, Filter> filtersMap = new LinkedHashMap<>();
        //用来校验token
        filtersMap.put("token", new MyAccessControlFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/api/user/login", "anon");
        filterChainDefinitionMap.put("/index/**","anon");
        filterChainDefinitionMap.put("/static/images/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/layui/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/treetable-lay/**", "anon");
        filterChainDefinitionMap.put("/api/user/token", "anon");
        filterChainDefinitionMap.put("/api/user/**", "anon");
        //放开swagger-ui地址
        filterChainDefinitionMap.put("/swagger/**", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/captcha.jpg", "anon");
        //druid sql监控配置
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/**","token,authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    @Bean//开启shiro的AOP注解支持，因为我们使用的是代理方式，所以需要开启代码支持
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor (SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public RedisCacheManager redisCacheManager() {
        return new RedisCacheManager();
    }

}
