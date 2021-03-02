package com.daniel.mapper;

import com.daniel.entity.SysRole;
import com.daniel.vo.request.role.RolePageReqVO;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    //获取所有角色的权限
    List<SysRole> selectAll(RolePageReqVO vo);

    //通过角色ID集合来获取角色姓名
    List<String> selectRoleNameByRoleIdList(List<String> roleId);
}