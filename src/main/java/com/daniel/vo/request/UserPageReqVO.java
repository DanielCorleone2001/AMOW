package com.daniel.vo.request;

import io.swagger.annotations.Api;
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

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户账号")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户状态 1:正常; 2:锁定")
    private Integer status;

    @ApiModelProperty(value = "搜索开始时间")
    private String startTime;

    @ApiModelProperty(value = "搜索结束时间")
    private String endTime;

}
