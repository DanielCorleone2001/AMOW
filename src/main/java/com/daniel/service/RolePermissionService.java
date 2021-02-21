package com.daniel.service;

import com.daniel.vo.request.RolePermissionOperationReqVO;

import java.util.List;

/**
 * @Package: com.daniel.service
 * @ClassName: RolePermissionService
 * @Author: daniel
 * @CreateTime: 2021/2/9 20:08
 * @Description:
 */
public interface RolePermissionService {
    //增加角色权限
    void addRolePermission(RolePermissionOperationReqVO vo);

    //根据权限ID来获取对应的所有角色的ID
    List<String> getRolesByPermissionId(String permissionId);

    //通过权限ID来删除对应的角色和菜单权限
    int removeByPermissionId(String permissionId);
}
