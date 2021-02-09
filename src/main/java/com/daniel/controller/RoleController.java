package com.daniel.controller;

import com.daniel.entity.SysRole;
import com.daniel.service.RoleService;
import com.daniel.utils.DataResult;
import com.daniel.vo.request.RoleAddReqVO;
import com.daniel.vo.request.RolePageReqVO;
import com.daniel.vo.response.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Package: com.daniel.controller
 * @ClassName: RoleController
 * @Author: daniel
 * @CreateTime: 2021/2/7 20:34
 * @Description:
 */

@RequestMapping("/api")
@RestController
@Api(tags = "组织模块-角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;


    @PostMapping("/roles")
    @ApiOperation(value = "分页获取角色信息接口")
    public DataResult<PageVO<SysRole>> pageInfo(@RequestBody RolePageReqVO vo) {
        DataResult<PageVO<SysRole>> result = DataResult.success();
        result.setData(roleService.pageInfo(vo));
        return result;
    }

    @PostMapping("/role")
    @ApiOperation(value = "新增角色接口")
    public DataResult<SysRole> addRole(@RequestBody @Valid RoleAddReqVO vo){
        DataResult result =DataResult.success();
        result.setData(roleService.addRole(vo));
        return result;
    }
}
