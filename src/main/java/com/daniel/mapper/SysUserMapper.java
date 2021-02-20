package com.daniel.mapper;

import com.daniel.entity.SysUser;
import com.daniel.vo.request.UserPageReqVO;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser selectByUsername(String username);

    List<SysUser> selectAll(UserPageReqVO userPageReqVO);

}