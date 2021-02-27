package com.daniel.controller;

import com.daniel.aop.annotation.MyLog;
import com.daniel.entity.SysLog;
import com.daniel.service.log.LogService;
import com.daniel.utils.dataresult.DataResult;
import com.daniel.vo.response.log.SysLogReqVO;
import com.daniel.vo.response.page.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Package: com.daniel.controller
 * @ClassName: LogController
 * @Author: daniel
 * @CreateTime: 2021/2/25 22:24
 * @Description:
 */

@RestController
@Api(tags = "系统模块-系统操作日志管理")
@RequestMapping("/api")
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping("/logs")
    @ApiOperation(value = "分页查询系统操作日志的接口")
    public DataResult<PageVO<SysLog>> pageInfo(@RequestBody SysLogReqVO sysLogReqVO ) {
        PageVO<SysLog> sysLogPageVO = logService.pageInfo(sysLogReqVO);
        DataResult<PageVO<SysLog>> result = DataResult.success();
        result.setData(sysLogPageVO);
        return result;
    }

    @DeleteMapping("/log")
    @ApiOperation(value = "批量删除系统操作日志的接口")
    @MyLog(title = "系统模块-系统操作日志管理", action = "批量删除系统操作日志的接口")
    public DataResult batchDeleteLog(@RequestBody List<String> logIdList ) {
       DataResult result = DataResult.success();
       result.setData(logService.deleteLog(logIdList));
       return result;
    }
}
