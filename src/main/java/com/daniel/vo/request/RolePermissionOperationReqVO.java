package com.daniel.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Package: com.daniel.vo.request
 * @ClassName: RolePermissionOperationReqVO
 * @Author: daniel
 * @CreateTime: 2021/2/9 19:53
 * @Description:
 */

@Data
public class RolePermissionOperationReqVO {

    @ApiModelProperty(value = "角色ID")
    @NotBlank(message = "角色ID不能为空")
    private String roleId;

    @ApiModelProperty(value = "菜单权限集合")
    @NotBlank(message = "菜单权限集合不能为空")
    private List<String> permissionId;
}
