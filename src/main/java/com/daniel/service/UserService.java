package com.daniel.service;

import com.daniel.entity.SysUser;
import com.daniel.vo.request.LoginReqVO;
import com.daniel.vo.request.UserPageReqVO;
import com.daniel.vo.response.LoginRespVO;
import com.daniel.vo.response.PageVO;
import com.github.pagehelper.PageInfo;

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
}
