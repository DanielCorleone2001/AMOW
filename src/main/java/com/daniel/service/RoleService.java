package com.daniel.service;

import com.daniel.entity.SysRole;
import com.daniel.vo.request.role.RoleAddReqVO;
import com.daniel.vo.request.role.RolePageReqVO;
import com.daniel.vo.request.role.RoleUpdateReqVO;
import com.daniel.vo.response.page.PageVO;

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

    SysRole detailInfo(String roleID);

    void updateRole(RoleUpdateReqVO roleUpdateReqVO);
}
