package com.daniel.service;

import com.daniel.vo.request.related.UserOwnRoleReqVO;

import java.util.List;

/**
 * @Package: com.daniel.service
 * @ClassName: UserRoleService
 * @Author: daniel
 * @CreateTime: 2021/2/21 9:42
 * @Description:
 */
public interface UserRoleService {
    //通过用户ID来获取角色ID
    List<String> getRoleIdsByUserId(String userId);

    //添加用户和角色的关联信息
    void addUserRoleInfo(UserOwnRoleReqVO vo);

    //通过角色ID来获取对应的用户ID
    List<String> getUserIdsByRoleId(String roleId);

    List<String> getUserIdsByRoleIdList(List<String> roleIdList);

}
