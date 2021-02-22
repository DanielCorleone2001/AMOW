package com.daniel.vo.request.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Package: com.daniel.vo.request
 * @ClassName: RoleAddReqVO
 * @Author: daniel
 * @CreateTime: 2021/2/9 21:06
 * @Description:
 */
@Data
public class RoleAddReqVO {

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @ApiModelProperty(value = "角色描述")
    private String description;

    @ApiModelProperty(value = "拥有的权限")
    private List<String> permissions;
}
