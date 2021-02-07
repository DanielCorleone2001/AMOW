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
}
