package com.daniel.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Package: com.daniel.controller
 * @ClassName: IndexController
 * @Author: daniel
 * @CreateTime: 2021/2/1 20:00
 * @Description: 页面跳转控制器
 */
@Controller
@RequestMapping("/index")
@Api(tags = "视图views" , description = "视图跳转控制器")
public class IndexController {

    @GetMapping("/404")
    @ApiOperation("跳转到404错误页面")
    public String error404(){
        return "error/404";
    }

    @GetMapping("/login")
    @ApiOperation(value = "跳转到登录页面")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    @ApiOperation("跳转至首页")
    public String home() {
        return "home";
    }

    @GetMapping("/main")
    @ApiOperation("跳转至主页")
    public String main() {
        return "main";
    }

    @GetMapping("/menus")
    @ApiOperation("跳转到菜单")
    public String menus() {
        return "menus/menus";
    }


    @GetMapping("/roles")
    @ApiOperation("跳转到菜单")
    public String roles() {
        return "roles/role";
    }

    @GetMapping("/depts")
    @ApiOperation("跳转到部门")
    public String deptList() {
        return "depts/dept";
    }

}
