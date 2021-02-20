package com.daniel.controller;

import com.daniel.entity.SysDept;
import com.daniel.service.DeptService;
import com.daniel.utils.DataResult;
import com.daniel.vo.request.DeptAddVO;
import com.daniel.vo.response.DeptRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.internal.dynalink.linker.LinkerServices;
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
@Api(tags = "组织模块-部门管理")
@RequestMapping("/api")
public class DeptController {

    @Autowired
    private DeptService deptService;

    @GetMapping("/depts")
    @ApiOperation(value = "获取机构列表接口")
    public DataResult<List<SysDept>> getAllDept() {
        DataResult<List<SysDept>> dataResult = DataResult.success();
        dataResult.setData(deptService.selectAll());
        return dataResult;
    }


    @GetMapping("/dept/tree")
    @ApiOperation(value = "获取部门树")
    public DataResult<List<DeptRespVO>> getDeptTreeList() {
        DataResult<List<DeptRespVO>> result = DataResult.success();
        result.setData(deptService.deptTreeList());
        return result;
    }

    @PostMapping("/dept")
    @ApiOperation(value = "新增部门接口")
    public DataResult<SysDept> addDept(@RequestBody @Valid DeptAddVO deptAddVO) {
        DataResult<SysDept> result = DataResult.success();
        result.setData(deptService.addDept(deptAddVO));
        return result;
    }
}
