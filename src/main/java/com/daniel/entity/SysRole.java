package com.daniel.entity;

import com.daniel.vo.response.permission.PermissionRespNodeVO;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class SysRole implements Serializable {
    private String id;

    private String name;

    private String description;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;

    private static final long serialVersionUID = 1L;

    private List<PermissionRespNodeVO> permissionRespNode;//角色拥有的菜单权限的集合
}