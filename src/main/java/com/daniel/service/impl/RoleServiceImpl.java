package com.daniel.service.impl;

import com.daniel.entity.SysRole;
import com.daniel.mapper.SysRoleMapper;
import com.daniel.service.RoleService;
import com.daniel.utils.PageUtil;
import com.daniel.vo.request.RolePageReqVO;
import com.daniel.vo.response.PageVO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Package: com.daniel.service.impl
 * @ClassName: RoleServiceImpl
 * @Author: daniel
 * @CreateTime: 2021/2/7 20:31
 * @Description:
 */

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public PageVO<SysRole> pageInfo(RolePageReqVO vo) {
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());//获取页面的配置，默认为 1/10
        List<SysRole> sysRoleList = sysRoleMapper.selectAll(vo);
        return PageUtil.getPageVO(sysRoleList);
    }
}
