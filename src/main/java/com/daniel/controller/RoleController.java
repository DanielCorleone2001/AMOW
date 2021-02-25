package com.daniel.controller;

import com.daniel.aop.annotation.MyLog;
import com.daniel.entity.SysRole;
import com.daniel.service.RoleService;
import com.daniel.utils.DataResult;
import com.daniel.vo.request.role.RoleAddReqVO;
import com.daniel.vo.request.role.RolePageReqVO;
import com.daniel.vo.request.role.RoleUpdateReqVO;
import com.daniel.vo.response.page.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @MyLog(title = "组织模块-角色管理", action = "分页获取角色信息接口" )
    public DataResult<PageVO<SysRole>> pageInfo(@RequestBody RolePageReqVO vo) {
        DataResult<PageVO<SysRole>> result = DataResult.success();
        result.setData(roleService.pageInfo(vo));
        return result;
    }

    @PostMapping("/role")
    @ApiOperation(value = "新增角色接口")
    @MyLog(title = "组织模块-角色管理", action = "新增角色接口" )
    public DataResult<SysRole> addRole(@RequestBody @Valid RoleAddReqVO vo){
        DataResult result =DataResult.success();
        result.setData(roleService.addRole(vo));
        return result;
    }

    @GetMapping("/role/{id}")
    @ApiOperation(value = "查询角色详情的接口")
    @MyLog(title = "组织模块-角色管理", action = "查询角色详情的接口" )
    public DataResult<SysRole> RoleDetailInfo(@PathVariable("id") String roleID) {
        DataResult<SysRole> dataResult = DataResult.success();
        dataResult.setData(roleService.detailInfo(roleID));
        return dataResult;
    }

    @PutMapping("/role")
    @ApiOperation(value = "更新角色信息的接口")
    @MyLog(title = "组织模块-角色管理", action = "更新角色信息的接口" )
    public DataResult updateRoleInfo (@RequestBody @Valid RoleUpdateReqVO roleUpdateReqVO) {
        roleService.updateRole(roleUpdateReqVO);
        return DataResult.success();
    }

    @DeleteMapping("/role/{id}")
    @ApiOperation(value = "删除角色的接口")
    @MyLog(title = "组织模块-角色管理", action = "删除角色的接口" )
    public DataResult deleteRole(@PathVariable("id") String roleID) {
        roleService.deleteRole(roleID);
        return DataResult.success();
    }
}
