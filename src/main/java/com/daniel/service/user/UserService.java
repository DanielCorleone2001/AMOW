package com.daniel.service.user;

import com.daniel.entity.SysUser;
import com.daniel.vo.request.login.LoginReqVO;
import com.daniel.vo.request.related.UserOwnRoleReqVO;
import com.daniel.vo.request.user.UserAddReqVO;
import com.daniel.vo.request.user.UserDetailINfoReqVO;
import com.daniel.vo.request.user.UserPageReqVO;
import com.daniel.vo.request.user.UserUpdateReqVO;
import com.daniel.vo.response.login.LoginRespVO;
import com.daniel.vo.response.page.PageVO;
import com.daniel.vo.response.related.UserOwnRoleRespVO;

import java.util.List;

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

   //刷新token
   String refreshToken(String refreshToken);

   //更新用户信息，operationId是操作人
   void updateUserInfo(UserUpdateReqVO userUpdateReqVO, String operationId);

   //删除用户
   void deleteUsers(List<String> userIds,String operationId);

   //退出登录
    void logout(String accessToken, String refreshToken);

    //获取用户信息
    SysUser getUserDetailInfo(String userId);

    //更新用户详细信息
    void updateUserDetailInfo(String userId, UserDetailINfoReqVO userDetailINfoReqVO);
}
