package com.daniel.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.daniel.vo.response
 * @ClassName: HomeRespVO
 * @Author: daniel
 * @CreateTime: 2021/2/1 21:52
 * @Description:
 */
@Data
public class HomeRespVO {

    @ApiModelProperty(value = "用户信息")
    private UserInfoRespVO userInfoVO;

    @ApiModelProperty(value = "展示拥有权限对应的菜单")
    private List<PermissionRespNodeVO> menus;
}
