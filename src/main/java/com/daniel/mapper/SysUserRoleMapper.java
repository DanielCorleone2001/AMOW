package com.daniel.mapper;

import com.daniel.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

    //通过用户ID来查询拥有的角色ID集合
    List<String> getRoleIdsByUserId(String userId);

    //根据用户ID删除和该用户关联的角色关联表数据
    int removeByUserId(String userId);

    //批量插入用户和角色关联的数据+
    int batchInsertUserRole(List<SysUserRole> list);

    //根据角色ID来获取对应的用户Id
    List<String> getUserIdsByRoleIdList(List<String> roleIds);

    List<String> getUserIdsByRoleId(String roleId);

    List<String> getUserInfoByRoleId(String roleId);

}