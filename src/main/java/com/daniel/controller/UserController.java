package com.daniel.controller;

import com.daniel.entity.SysUser;
import com.daniel.service.UserService;
import com.daniel.utils.DataResult;
import com.daniel.vo.request.LoginReqVO;
import com.daniel.vo.request.UserAddReqVO;
import com.daniel.vo.request.UserOwnRoleReqVO;
import com.daniel.vo.request.UserPageReqVO;
import com.daniel.vo.response.LoginRespVO;
import com.daniel.vo.response.PageVO;
import com.daniel.vo.response.UserOwnRoleRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.crypto.Data;

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

    @PostMapping("/users")
    @ApiOperation(value = "分页查询用户接口")
    //@RequiresPermissions("sys:user:list")
    public DataResult<PageVO<SysUser>> pageInfo(@RequestBody UserPageReqVO userPageReqVO) {
        DataResult dataResult = DataResult.success();
        dataResult.setData(userService.pageInfo(userPageReqVO));
        return dataResult;
    }

    @PostMapping("/user")
    @ApiOperation(value = "新增用户接口")
    public DataResult addUser(@RequestBody @Valid UserAddReqVO userAddReqVO) {
        userService.addUser(userAddReqVO);
        return DataResult.success();
    }

    @GetMapping("/user/roles/{userId}")
    @ApiOperation(value = "赋予角色-获取用户拥有角色的接口")
    public DataResult<UserOwnRoleRespVO> getUserOwnRole(@PathVariable("userId") String userId) {
        DataResult<UserOwnRoleRespVO> result = DataResult.success();
        result.setData(userService.getUserOwnRole(userId));
        return result;
    }

    @PutMapping("/user/roles")
    @ApiOperation(value = "保持用户拥有的角色信息接口")
    public DataResult saveUserOwnRole(@RequestBody @Valid UserOwnRoleReqVO userOwnRoleReqVO) {
        DataResult result = DataResult.success();
        userService.setUserOwnRole(userOwnRoleReqVO);
        return result;
    }
}
