package com.daniel.controller;

import com.daniel.entity.SysPermission;
import com.daniel.service.PermissionService;
import com.daniel.utils.DataResult;
import com.daniel.vo.response.PermissionRespNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Package: com.daniel.controller
 * @ClassName: PermissionController
 * @Author: daniel
 * @CreateTime: 2021/2/2 16:54
 * @Description:
 */
@RestController
@RequestMapping("/api")
@Api(tags = "菜单权限管理")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/permissions")
    @ApiOperation(value = "获取所有菜单权限接口")
    public DataResult<List<SysPermission>> getAllMenusPermission() {
        DataResult<List<SysPermission>> result = DataResult.success();
        result.setData(permissionService.selectAll());
        return result;
    }

    @GetMapping("/permissions/tree")
    @ApiOperation( value = "菜单权限树")
    public DataResult<List<PermissionRespNodeVO>> getAllPermissionTree() {
        DataResult result = DataResult.success();
        result.setData(permissionService.selectAllMenuByTree());
        return  result;
    }
}
