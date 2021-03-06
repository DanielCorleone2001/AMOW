package com.daniel.mapper;

import com.daniel.entity.SysPermission;
import com.daniel.entity.SysRolePermission;

import java.util.List;

public interface SysPermissionMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    //获取权限的信息
    List<SysPermission> selectAll();

    //查询关联菜单的子类，即获取该菜单权限的所有子集
    List<SysPermission> selectAllChild(String pid);

    List<SysPermission> getPermissionInfoByPermissionIdList(List<String> permissionIdList);
}