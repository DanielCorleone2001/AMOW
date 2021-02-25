package com.daniel.vo.response.log;

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
}
