package com.daniel.vo.request.user;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @Package: com.daniel.vo.request.user
 * @ClassName: UserEditPasswordReqVO
 * @Author: daniel
 * @CreateTime: 2021/2/28 12:32
 * @Description:
 */

@Data
public class UserEditPasswordReqVO {

    @ApiModelProperty(value = "用户旧密码")
    private String oldPwd;

    @ApiModelProperty(value = "用户新密码")
    private String newPwd;
}
