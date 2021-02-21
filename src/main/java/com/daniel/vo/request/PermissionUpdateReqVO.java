package com.daniel.vo.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Package: com.daniel.vo.request
 * @ClassName: PermissionUpdateReqVO
 * @Author: daniel
 * @CreateTime: 2021/2/21 19:16
 * @Description:
 */

@Data
public class PermissionUpdateReqVO {

    @ApiModelProperty(value = "ID")
    @NotBlank(message = "ID不能为空")
    private String id;

    @ApiModelProperty(value = "状态: 1:正常; 2:禁用")
    private Integer status;

    @ApiModelProperty(value = "菜单权限的名称")
    private String name;

    @ApiModelProperty(value = "菜单权限标识,shiro适配restful")
    private String perms;

    @ApiModelProperty(value = "接口地址")
    private String url;

    @ApiModelProperty(value = "请求方式和url配合使用")
    private String method;

    @ApiModelProperty(value = "父级菜单的ID")
    private String pid;

    @ApiModelProperty(value = "排序码")
    private Integer orderNum;

    @ApiModelProperty(value = "菜单权限类型: 1:目录; 2:菜单; 3:按钮")
    private Integer type;

    @ApiModelProperty(value = "编码")
    private String code;
}
