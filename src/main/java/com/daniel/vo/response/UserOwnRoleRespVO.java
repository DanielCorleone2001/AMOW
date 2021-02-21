package com.daniel.vo.response;

import com.daniel.entity.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.daniel.vo.response
 * @ClassName: UserOwnRoleRespVO
 * @Author: daniel
 * @CreateTime: 2021/2/21 11:01
 * @Description:
 */
@Data
public class UserOwnRoleRespVO {

    @ApiModelProperty(value = "拥有角色集合")
    private List<String> ownRoles;

    @ApiModelProperty(value = "所有角色列表")
    private List<SysRole> allRole;
}
