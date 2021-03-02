package com.daniel.controller;

import com.daniel.aop.annotation.MyLog;
import com.daniel.entity.SysDept;
import com.daniel.service.dept.DeptService;
import com.daniel.utils.dataresult.DataResult;
import com.daniel.vo.request.dept.DeptAddReqVO;
import com.daniel.vo.request.dept.DeptUpdateReqVO;
import com.daniel.vo.response.dept.DeptRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Package: com.daniel.controller
 * @ClassName: DeptController
 * @Author: daniel
 * @CreateTime: 2021/2/20 11:29
 * @Description:
 */

@RestController
@Api(tags = "组织模块-部门管理",description = "部门管理相关接口")
@RequestMapping("/api")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/depts")
    @ApiOperation(value = "获取机构列表接口")
    @MyLog(title = "组织模块-部门管理", action = "获取机构列表接口")
    @RequiresPermissions("sys:dept:list")
    public DataResult<List<SysDept>> getAllDept() {
        DataResult<List<SysDept>> dataResult = DataResult.success();
        dataResult.setData(deptService.selectAll());
        return dataResult;
    }


    @GetMapping("/dept/tree")
    @ApiOperation(value = "获取部门树")
    @MyLog(title = "组织模块-部门管理", action = "获取部门树")
    @RequiresPermissions(value = {"sys:user:update","sys:user:add","sys:dept:add","sys:dept:update"},logical = Logical.OR)
    public DataResult<List<DeptRespVO>> getDeptTreeList(@RequestParam(required = false) String deptID) {
        DataResult<List<DeptRespVO>> result = DataResult.success();
        result.setData(deptService.deptTreeList(deptID));
        return result;
    }

    @PostMapping("/dept")
    @ApiOperation(value = "新增部门接口")
    @MyLog(title = "组织模块-部门管理", action = "新增部门接口")
    @RequiresPermissions("sys:dept:add")
    public DataResult<SysDept> addDept(@RequestBody @Valid DeptAddReqVO deptAddVO) {
        DataResult<SysDept> result = DataResult.success();
        result.setData(deptService.addDept(deptAddVO));
        return result;
    }

    @PutMapping("/dept")
    @ApiOperation(value = "编辑部门接口")
    @MyLog(title = "组织模块-部门管理", action = "编辑部门接口")
    @RequiresPermissions("sys:dept:update")
    public DataResult updateDept (@RequestBody @Valid DeptUpdateReqVO deptUpdateReqVO ) {
        deptService.updateDeptInfo(deptUpdateReqVO);
        return DataResult.success();
    }

    @DeleteMapping("/dept/{id}")
    @ApiOperation(value = "删除部门的接口")
    @MyLog(title = "组织模块-部门管理", action = "删除部门的接口")
    @RequiresPermissions("sys:dept:delete")
    public DataResult deleteDept(@PathVariable("id") String deptId) {
        deptService.deleteDept(deptId);
        return DataResult.success();
    }
}
