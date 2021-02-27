package com.daniel.controller;

import com.daniel.aop.annotation.MyLog;
import com.daniel.contains.Constant;
import com.daniel.entity.SysUser;
import com.daniel.service.user.UserService;
import com.daniel.utils.dataresult.DataResult;
import com.daniel.utils.jwt.JWToken;
import com.daniel.vo.request.login.LoginReqVO;
import com.daniel.vo.request.related.UserOwnRoleReqVO;
import com.daniel.vo.request.user.UserAddReqVO;
import com.daniel.vo.request.user.UserPageReqVO;
import com.daniel.vo.request.user.UserUpdateReqVO;
import com.daniel.vo.response.login.LoginRespVO;
import com.daniel.vo.response.page.PageVO;
import com.daniel.vo.response.related.UserOwnRoleRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录接口")
    @PostMapping("/user/login")
    @MyLog(title = "用户模块接口", action = "用户登录接口")
    public DataResult<LoginRespVO> login(@RequestBody @Valid LoginReqVO loginReqVO){
        DataResult dataResult = DataResult.success();
        dataResult.setData(userService.login(loginReqVO));
        return dataResult;
    }

    @PostMapping("/users")
    @ApiOperation(value = "分页查询用户接口")
    @MyLog(title = "用户模块接口", action = "分页查询用户接口")
    //@RequiresPermissions("sys:user:list")
    public DataResult<PageVO<SysUser>> pageInfo(@RequestBody UserPageReqVO userPageReqVO) {
        DataResult dataResult = DataResult.success();
        dataResult.setData(userService.pageInfo(userPageReqVO));
        return dataResult;
    }

    @PostMapping("/user")
    @ApiOperation(value = "新增用户接口")
    @MyLog(title = "用户模块接口", action = "新增用户接口")
    public DataResult addUser(@RequestBody @Valid UserAddReqVO userAddReqVO) {
        userService.addUser(userAddReqVO);
        return DataResult.success();
    }

    @GetMapping("/user/roles/{userId}")
    @ApiOperation(value = "赋予角色-获取用户拥有角色的接口")
    @MyLog(title = "用户模块接口", action = "赋予角色-获取用户拥有角色的接口")
    public DataResult<UserOwnRoleRespVO> getUserOwnRole(@PathVariable("userId") String userId) {
        DataResult<UserOwnRoleRespVO> result = DataResult.success();
        result.setData(userService.getUserOwnRole(userId));
        return result;
    }

    @PutMapping("/user/roles")
    @ApiOperation(value = "保持用户拥有的角色信息接口")
    @MyLog(title = "用户模块接口", action = "保持用户拥有的角色信息接口")
    public DataResult saveUserOwnRole(@RequestBody @Valid UserOwnRoleReqVO userOwnRoleReqVO) {
        DataResult result = DataResult.success();
        userService.setUserOwnRole(userOwnRoleReqVO);
        return result;
    }

    @GetMapping("/user/token")
    @ApiOperation(value = "用户刷新token的接口")
    @MyLog(title = "用户模块接口", action = "用户刷新token的接口")
    public DataResult<String> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(Constant.REFRESH_TOKEN);
        DataResult<String> result = DataResult.success();
        result.setData(userService.refreshToken(refreshToken));
        return result;
    }

    @PutMapping("/user")
    @ApiOperation(value = "用户更新信息的接口")
    @MyLog(title = "用户模块接口", action = "用户更新信息的接口")
    public DataResult updateUserInfo(@RequestBody @Valid UserUpdateReqVO vo, HttpServletRequest request ) {
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String userId= JWToken.getUserId(accessToken);
        DataResult result = DataResult.success();
        userService.updateUserInfo(vo,userId);
        return result;
    }

    @DeleteMapping("/user")
    @ApiOperation(value = "删除用户的接口")
    @MyLog(title = "用户模块接口", action = "删除用户的接口")
    public DataResult deleteUser (@RequestBody @ApiParam(value = "用户ID的集合") List<String> userIdList, HttpServletRequest request) {
        String operationId = JWToken.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        userService.deleteUsers(userIdList,operationId);
        return DataResult.success();
    }

    @GetMapping("/user/logout")
    @ApiOperation(value = "用户退出登录的接口")
    @MyLog(title = "用户模块接口", action = "用户退出登录的接口")
    public DataResult logout(HttpServletRequest request ) {
        try{
            String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
            String refreshToken = request.getHeader(Constant.REFRESH_TOKEN);
            userService.logout(accessToken,refreshToken);
        }catch (Exception e ) {
            log.error("logout error {}", e);
        }
        return DataResult.success();
    }
}
