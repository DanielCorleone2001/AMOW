package com.daniel.service;

import com.daniel.entity.SysRole;
import com.daniel.vo.request.RoleAddReqVO;
import com.daniel.vo.request.RolePageReqVO;
import com.daniel.vo.response.PageVO;

import java.util.List;

/**
 * @Package: com.daniel.service
 * @ClassName: RoleService
 * @Author: daniel
 * @CreateTime: 2021/2/7 20:30
 * @Description:
 */
public interface RoleService {
    PageVO<SysRole> pageInfo(RolePageReqVO vo);

    SysRole addRole(RoleAddReqVO vo);

    //获取所有角色的集合
    List<SysRole> selectAllRoles();
}
