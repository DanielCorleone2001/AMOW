package com.daniel.service;

import com.daniel.vo.request.RolePermissionOperationReqVO;

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
}
