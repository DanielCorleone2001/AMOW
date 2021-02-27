package com.daniel.service.permission;

import com.daniel.entity.SysPermission;
import com.daniel.vo.request.permission.PermissionAddReqVO;
import com.daniel.vo.request.permission.PermissionUpdateReqVO;
import com.daniel.vo.response.permission.PermissionRespNodeVO;

import java.util.List;

/**
 * @Package: com.daniel.service
 * @ClassName: PermissionService
 * @Author: daniel
 * @CreateTime: 2021/2/2 16:46
 * @Description:
 */
public interface PermissionService {
    List<SysPermission> selectAll();

    //菜单权限树接口
    List<PermissionRespNodeVO> selectAllMenuByTree();

    //添加菜单权限
    SysPermission addPermission(PermissionAddReqVO vo);

    // 创建根据用户id获取菜单权限接口(
    List<PermissionRespNodeVO> permissionTreeList(String userId);

    List<PermissionRespNodeVO> selectAllByTree();

    //更新权限
    void updatePermission(PermissionUpdateReqVO permissionUpdateReqVO);

    //删除菜单
    void deletePermission(String permissionId);
}
