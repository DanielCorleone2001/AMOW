package com.daniel.service;

import com.daniel.entity.SysUser;
import com.daniel.vo.request.LoginReqVO;
import com.daniel.vo.request.UserAddReqVO;
import com.daniel.vo.request.UserOwnRoleReqVO;
import com.daniel.vo.request.UserPageReqVO;
import com.daniel.vo.response.LoginRespVO;
import com.daniel.vo.response.PageVO;
import com.daniel.vo.response.UserOwnRoleRespVO;

/**
 * @Package: com.daniel.service
 * @ClassName: UserService
 * @Author: daniel
 * @CreateTime: 2021/1/30 23:30
 * @Description:
 */
public interface UserService {
    //登录
    LoginRespVO login(LoginReqVO loginReqVO);

    //页面传入
   PageVO<SysUser> pageInfo(UserPageReqVO userPageReqVO);

   void addUser(UserAddReqVO userAddReqVO);

   //通过用户ID来获取对应的角色集合
   UserOwnRoleRespVO getUserOwnRole(String userId);

   //设置用户所拥有的角色
   void setUserOwnRole(UserOwnRoleReqVO vo);
}
