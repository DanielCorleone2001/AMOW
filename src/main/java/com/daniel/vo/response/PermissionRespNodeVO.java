package com.daniel.vo.response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.daniel.vo.response
 * @ClassName: PermissionRespNodeVO
 * @Author: daniel
 * @CreateTime: 2021/2/1 22:35
 * @Description:
 */
@Data
public class PermissionRespNodeVO {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "跳转的地址")
    private String url;

    @ApiModelProperty(value = "菜单权限的名称")
    private String title;

    @ApiModelProperty(value = "子集集合")
    private List<?> children;

    @ApiModelProperty(value = "是否展开，默认展开")
    private boolean spread = true;

    @ApiModelProperty(value = "节点是否被选中")
    private boolean checked;
}
