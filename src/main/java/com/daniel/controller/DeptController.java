package com.daniel.controller;

import com.daniel.entity.SysDept;
import com.daniel.service.DeptService;
import com.daniel.utils.DataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.internal.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
