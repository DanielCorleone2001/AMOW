package com.daniel.controller;

import com.daniel.aop.annotation.MyLog;
import com.daniel.contains.Constant;
import com.daniel.service.home.HomeService;
import com.daniel.utils.dataresult.DataResult;
import com.daniel.utils.jwt.JWToken;
import com.daniel.vo.response.home.HomeRespVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Package: com.daniel.controller
 * @ClassName: HomeController
 * @Author: daniel
 * @CreateTime: 2021/2/1 22:51
 * @Description:
 */
@RestController
@RequestMapping("/api")
@Api(tags = "首页数据")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/home")
    @ApiOperation(value = "获取首页数据接口")
    @MyLog(title = "首页数据", action = "获取首页数据接口")
    public DataResult<HomeRespVO> getHomeInfo (HttpServletRequest request) {
        String accessToken= request.getHeader(Constant.ACCESS_TOKEN);

        String userId = JWToken.getUserId(accessToken);
        DataResult<HomeRespVO> result = DataResult.success();
        result.setData(homeService.getHomeInfo(userId));
        return result;
    }
}
