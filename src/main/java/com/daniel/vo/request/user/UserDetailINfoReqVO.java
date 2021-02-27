package com.daniel.vo.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.daniel.vo.request.user
 * @ClassName: UserDetailINfoReqVO
 * @Author: daniel
 * @CreateTime: 2021/2/27 23:37
 * @Description: 用户详细信息类 用于和前端接受的数据交互
 */
@Data
public class UserDetailINfoReqVO {

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "用户性别 1:男; 2:女")
    private Integer sex;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "账户状态 1:正常; 2:锁定")
    private Integer status;
}
