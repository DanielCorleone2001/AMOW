package com.daniel.service.impl;

import com.daniel.entity.SysLog;
import com.daniel.mapper.SysLogMapper;
import com.daniel.service.log.LogService;
import com.daniel.utils.page.PageUtil;
import com.daniel.vo.response.log.SysLogReqVO;
import com.daniel.vo.response.page.PageVO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Package: com.daniel.service.impl
 * @ClassName: LogServiceImpl
 * @Author: daniel
 * @CreateTime: 2021/2/25 22:21
 * @Description:
 */
@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 获取日志的页面信息
     * @param sysLogReqVO
     * @return
     */
    @Override
    public PageVO<SysLog> pageInfo(SysLogReqVO sysLogReqVO) {
        PageHelper.startPage(sysLogReqVO.getPageNum(),sysLogReqVO.getPageSize());//页面配置
        List<SysLog> sysLogs = sysLogMapper.selectAllLog(sysLogReqVO);
        return PageUtil.getPageVO(sysLogs);
    }

    /**
     * 通过日志ID来批量删除日志
     * @param logIdList
     * @return
     */
    @Override
    public int deleteLog(List<String> logIdList) {
        return sysLogMapper.batchDeleteLog(logIdList);
    }
}
