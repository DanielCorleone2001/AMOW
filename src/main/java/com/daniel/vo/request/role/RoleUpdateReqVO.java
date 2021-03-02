package com.daniel.vo.request.role;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Package: com.daniel.vo.request.role
 * @ClassName: RoleUpdateReqVO
 * @Author: daniel
 * @CreateTime: 2021/2/22 10:41
 * @Description: 用于接收前端更新角色信息，一一对应的数据类
 */

@Data
public class RoleUpdateReqVO {

    @ApiModelProperty(value = "角色ID")
    @NotBlank(message = "角色ID不能为空")
    private String id;

    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "角色状态 1:正常; 2:禁用")
    private Integer status;

    @ApiModelProperty(value = "角色拥有的菜单权限")
    private List<String> permissions;
}
