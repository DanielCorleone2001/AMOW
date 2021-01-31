package com.daniel.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Package: com.daniel.vo.request
 * @ClassName: LoginReqVO
 * @Author: daniel
 * @CreateTime: 2021/1/30 23:20
 * @Description:
 */

@Data
public class LoginReqVO {

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "登陆方式(1:PC端 2:移动端)")
    @NotBlank(message = "登陆方式不能为空")
    private String type;
}
