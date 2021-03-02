package com.daniel.mapper;

import com.daniel.entity.SysRolePermission;

import java.util.List;

public interface SysRolePermissionMapper {
    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    //批量插入角色信息
    int batchInsertRolePermission(List<SysRolePermission> list);

    int removeByRoleId(String roleId);

    //根据权限的ID获得所有关联的角色ID的集合
    List<String> getRoleIdsByPermissionId(String permissionId);

    //删除角色和菜单权限关联的数据
    int removeByPermissionId (String permissionId);

    //根据角色Id来获取到该角色关联的菜单权限ID的集合
    List<String> getPermissionIdsByRoleId(String roleId);

    //根据角色Id来获取到该角色关联的菜单权限ID的集合
    List<String> getPermissionIdsByRoleIdList(List<String> roleIds);

    //删除角色
    int removeRoleByID(String roleID);

    List<String> getPermissionIdListByRoleIdList(List<String> roleIdList);
}