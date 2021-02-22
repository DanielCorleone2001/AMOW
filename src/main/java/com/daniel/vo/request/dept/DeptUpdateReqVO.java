package com.daniel.vo.request.dept;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Package: com.daniel.vo.request.dept
 * @ClassName: DeptUpdateReqVO
 * @Author: daniel
 * @CreateTime: 2021/2/22 17:06
 * @Description:
 */
@Data
public class DeptUpdateReqVO {

    @ApiModelProperty(value = "部门ID")
    @NotBlank(message = "部门ID不能为空")
    private String id;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "父级部门ID")
    private String pid;

    @ApiModelProperty(value = "部门状态 1:正常; 2:禁用")
    private Integer status;

    @ApiModelProperty(value = "部门经理名字")
    private String managerName;

    @ApiModelProperty(value = "部门经理电话")
    private String phone;
}
