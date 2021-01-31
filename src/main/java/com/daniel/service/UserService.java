package com.daniel.service;

import com.daniel.vo.request.LoginReqVO;
import com.daniel.vo.response.LoginRespVO;

/**
 * @Package: com.daniel.service
 * @ClassName: UserService
 * @Author: daniel
 * @CreateTime: 2021/1/30 23:30
 * @Description:
 */
public interface UserService {
    LoginRespVO login(LoginReqVO loginReqVO);
}
