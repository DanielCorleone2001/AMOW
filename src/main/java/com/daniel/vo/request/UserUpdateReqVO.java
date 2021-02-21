package com.daniel.vo.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Package: com.daniel.vo.request
 * @ClassName: UserUpdateReqVO
 * @Author: daniel
 * @CreateTime: 2021/2/21 15:25
 * @Description: 前端更新用户信息类
 */
@Data
public class UserUpdateReqVO {

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "手机号码")
    private String phoneNum;

    @ApiModelProperty(value = "所属机构")
    private String deptId;

    @ApiModelProperty(value = "账户状态 1:正常; 2:锁定")
    private Integer status;
}
