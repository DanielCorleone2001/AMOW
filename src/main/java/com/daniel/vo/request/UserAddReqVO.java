package com.daniel.vo.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Package: com.daniel.vo.request
 * @ClassName: UserAddReqVO
 * @Author: daniel
 * @CreateTime: 2021/2/20 19:41
 * @Description: 新增用户类
 */

@Data
public class UserAddReqVO {
    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "电话号码")
    private String phoneNum;

    @ApiModelProperty(value = "创建来源 1:web; 2:android 3:iOS")
    private Integer createForm;

    @ApiModelProperty(value = "所属部门")
    @NotBlank(message = "所属部门不能为空")
    private String deptId;

    @ApiModelProperty(value = "用户状态 1:正常; 2:锁定")
    private Integer status;


}
