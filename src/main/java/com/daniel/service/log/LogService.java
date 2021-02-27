package com.daniel.service.log;

import com.daniel.entity.SysLog;
import com.daniel.vo.response.log.SysLogReqVO;
import com.daniel.vo.response.page.PageVO;

import java.util.List;

/**
 * @Package: com.daniel.service
 * @ClassName: LogService
 * @Author: daniel
 * @CreateTime: 2021/2/25 22:19
 * @Description: 日志服务接口
 */
public interface LogService {

    //获取日志的页面信息
    PageVO<SysLog> pageInfo(SysLogReqVO sysLogReqVO);

    //批量删除日志
    int deleteLog(List<String> logIdList);
}
