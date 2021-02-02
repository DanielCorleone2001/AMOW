package com.daniel.service;

import com.daniel.vo.response.HomeRespVO;

/**
 * @Package: com.daniel.service
 * @ClassName: HomeService
 * @Author: daniel
 * @CreateTime: 2021/2/1 22:39
 * @Description:
 */
public interface HomeService {

    HomeRespVO getHomeInfo(String userId);
}