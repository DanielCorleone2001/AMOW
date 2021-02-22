package com.daniel.vo.response.login;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.daniel.vo.response
 * @ClassName: LoginRespVO
 * @Author: daniel
 * @CreateTime: 2021/1/30 23:26
 * @Description:
 */
@Data//get and set
public class LoginRespVO {

    @ApiModelProperty(value = "业务访问Token")
    private String accessToken;
    @ApiModelProperty(value = "业务Token刷新凭证")
    private String refreshToken;
    @ApiModelProperty(value = "用户ID")
    private String userId;
}
