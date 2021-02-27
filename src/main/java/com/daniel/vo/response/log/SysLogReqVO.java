package com.daniel.vo.response.log;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.daniel.vo.response.log
 * @ClassName: SysLogReqVO
 * @Author: daniel
 * @CreateTime: 2021/2/25 22:14
 * @Description: 用于接收前端日志输入的类
 */
@Data
public class SysLogReqVO {

    @ApiModelProperty(value = "当前页数")
    private int pageNum = 1;

    @ApiModelProperty(value = "分页数量")
    private int pageSize = 10;

    @ApiModelProperty(value = "用户操作动作")
    private String operation;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
