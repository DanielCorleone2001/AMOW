package com.daniel.vo.response.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.daniel.vo.response
 * @ClassName: UserInfoRespVO
 * @Author: daniel
 * @CreateTime: 2021/2/1 21:52
 * @Description:
 */
@Data
public class UserInfoRespVO {

    @ApiModelProperty("用户ID")
    private  String id;

    @ApiModelProperty("账号")
    private String username;

    @ApiModelProperty("部门ID")
    private String deptId;

    @ApiModelProperty("部门名称")
    private String deptName;
}
