package com.daniel.vo.request.related;

import com.daniel.entity.SysRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Package: com.daniel.vo.request
 * @ClassName: UserOwnRoleRespVO
 * @Author: daniel
 * @CreateTime: 2021/2/21 9:33
 * @Description:
 */

@Data
public class UserOwnRoleReqVO {

    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "用户所拥有的角色集合")
    private List<String> roleIds;

}
