package com.daniel.controller;

import com.daniel.service.UserService;
import com.daniel.utils.DataResult;
import com.daniel.vo.request.LoginReqVO;
import com.daniel.vo.response.LoginRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Package: com.daniel.controller
 * @ClassName: UserController
 * @Author: daniel
 * @CreateTime: 2021/1/30 23:27
 * @Description:
 */
@RestController
@RequestMapping("/api")
@Api(tags = "用户模块接口")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录接口")
    @PostMapping("/user/login")
    public DataResult<LoginRespVO> login(@RequestBody @Valid LoginReqVO loginReqVO){
        DataResult dataResult = DataResult.success();
        dataResult.setData(userService.login(loginReqVO));
        return dataResult;
    }
}