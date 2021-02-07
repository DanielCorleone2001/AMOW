package com.daniel.service;

import com.daniel.entity.SysRole;
import com.daniel.vo.request.RolePageReqVO;
import com.daniel.vo.response.PageVO;

/**
 * @Package: com.daniel.service
 * @ClassName: RoleService
 * @Author: daniel
 * @CreateTime: 2021/2/7 20:30
 * @Description:
 */
public interface RoleService {
    PageVO<SysRole> pageInfo(RolePageReqVO vo);
}
