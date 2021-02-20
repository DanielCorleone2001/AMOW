package com.daniel.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.daniel.vo.request
 * @ClassName: RolePageReqVO
 * @Author: daniel
 * @CreateTime: 2021/2/7 20:10
 * @Description:
 */
@Data
public class RolePageReqVO {

    @ApiModelProperty(value = "当前为第几页，默认为第一页")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize = 10;

    @ApiModelProperty(value = "角色ID")
    private String roleId;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "状态")
    private Integer status;
}
