package com.daniel.service;

import com.daniel.entity.SysPermission;
import com.daniel.vo.response.PermissionRespNodeVO;

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
}
