package com.daniel.mapper;

import com.daniel.entity.SysLog;
import com.daniel.vo.response.log.SysLogReqVO;

import java.util.List;

public interface SysLogMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);

    //获取所有的日志
    List<SysLog> selectAllLog(SysLogReqVO sysLogReqVO);

    //批量删除日志
    int batchDeleteLog(List<String> logIdList);
}