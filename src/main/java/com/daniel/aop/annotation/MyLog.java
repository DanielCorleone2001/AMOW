package com.daniel.aop.annotation;

import java.lang.annotation.*;

/**
 * @Package: com.daniel.aop.annotation
 * @ClassName: MyLog
 * @Author: daniel
 * @CreateTime: 2021/2/25 20:23
 * @Description:
 */

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyLog {

    //模块
    String title() default "";

    //功能
    String action() default "";
}
