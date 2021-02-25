package com.daniel.controller;

import com.daniel.aop.annotation.MyLog;
import com.daniel.utils.DataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/swagger")
@Api(tags = "测试接口模块",description = "提供测试接口")
public class HelloController {

    @RequestMapping("hello")
    public String hello() {
        return "test success";
    }

    @GetMapping("/test")
    @ApiOperation(value = "测试接口")
    @MyLog(title = "测试接口模块" , action = "测试接口")
    public String testSwagger() {
       return "test success";
    }

    @GetMapping("/test/data")
    @ApiOperation(value = "统一格式格式的测试接口")
    @MyLog(title = "测试接口模块" , action = "统一格式格式的测试接口")
    public DataResult<String> dataResultTest() {
        DataResult dataResult = DataResult.success("统一格式格式的测试接口");
        return dataResult;
    }
}
