package com.daniel.service.role;

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

    //角色信息，包括获取角色相关的权限
    SysRole detailInfo(String roleID);

    //更新角色信息(编辑角色功能)
    void updateRole(RoleUpdateReqVO roleUpdateReqVO);

    //删除角色
    void deleteRole(String roleID);

    //通过用户ID去查找拥有的角色
    List<String> getRoleNameListByUserId(String userID);
}
