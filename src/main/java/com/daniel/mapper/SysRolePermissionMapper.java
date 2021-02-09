package com.daniel.mapper;

import com.daniel.entity.SysRolePermission;

import java.util.List;

public interface SysRolePermissionMapper {
    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    //批量插入角色信息
    int batchInsertRolePermission(List<SysRolePermission> list);

    int removeByRoleId(String roleId);
}