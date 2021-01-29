package com.daniel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Package: com.daniel.controller
 * @ClassName: HelloController
 * @Author: daniel
 * @CreateTime: 2021/1/29 22:37
 * @Description:
 */
@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello() {
        return "test success";
    }
}
