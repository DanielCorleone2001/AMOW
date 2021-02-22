package com.daniel.vo.response.dept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.daniel.vo.response
 * @ClassName: DeptRespVO
 * @Author: daniel
 * @CreateTime: 2021/2/20 17:45
 * @Description:
 */

@Data
public class DeptRespVO {

    @ApiModelProperty(value = "部门ID")
    private String id;

    @ApiModelProperty(value = "部门名称")
    private String title;

    @ApiModelProperty(value = "默认展开")
    private boolean spread = true;

    @ApiModelProperty(value = "部门子集")
    private List<?> children;
}
