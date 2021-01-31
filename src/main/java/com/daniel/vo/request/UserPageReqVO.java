package com.daniel.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.daniel.vo.request
 * @ClassName: UserPageReqVO
 * @Author: daniel
 * @CreateTime: 2021/1/31 17:16
 * @Description: 分页具有的参数
 */
@Data
public class UserPageReqVO {

    @ApiModelProperty(value = "当前的页数")
    private Integer pageNum = 1;//默认初始页数

    @ApiModelProperty(value = "当前页的行数")
    private Integer pageSize = 10;//默认页最多十个元素

}
