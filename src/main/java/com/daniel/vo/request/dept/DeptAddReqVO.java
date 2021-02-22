package com.daniel.vo.request.dept;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.daniel.vo.request
 * @ClassName: DeptAddVO
 * @Author: daniel
 * @CreateTime: 2021/2/20 18:49
 * @Description: 新增部门类
 */

@Data
public class DeptAddReqVO {

    @ApiModelProperty(value = "新增部门名称")
    private String name;

    @ApiModelProperty(value = "部门父级ID")
    private String pid;

    @ApiModelProperty(value = "部门经理名字")
    private String managerName;

    @ApiModelProperty(value = "部门经理电话")
    private String managerPhoneNum;

    @ApiModelProperty(value = "部门状态，0为弃用，1为正常")
    private Integer status;
}
