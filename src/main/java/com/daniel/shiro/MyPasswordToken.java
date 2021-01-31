package com.daniel.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @Package: com.daniel.shiro
 * @ClassName: MyPasswordToken
 * @Author: daniel
 * @CreateTime: 2021/1/31 20:19
 * @Description: 自定义PasswordToken
 */
public class MyPasswordToken extends UsernamePasswordToken {
    private String token;

    public MyPasswordToken(String token) {
        this.token = token;
    }

    /**
     * getPrincipal():最重要的身份信息，大部分情况下返回的是UserDetails接口的实现类，也是框架中的常用接口之一。
     * @return
     */
    @Override
    public Object getPrincipal(){//自定义返回的Principal为我们的token
        return token;
    }

    /**
     * getCredentials():密码信息，用户输入的密码字符串，在认证过后通常会被移除，用于保障安全。
     * @return
     */
    @Override
    public Object getCredentials(){//自定义返回的Credentials(证书)为我们的token
        return token;
    }
}
