package com.daniel.service.impl;

import com.daniel.entity.SysRole;
import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.mapper.SysRoleMapper;
import com.daniel.mapper.SysUserRoleMapper;
import com.daniel.service.RolePermissionService;
import com.daniel.service.RoleService;
import com.daniel.utils.PageUtil;
import com.daniel.vo.request.RoleAddReqVO;
import com.daniel.vo.request.RolePageReqVO;
import com.daniel.vo.request.RolePermissionOperationReqVO;
import com.daniel.vo.response.PageVO;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public PageVO<SysRole> pageInfo(RolePageReqVO vo) {
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());//获取页面的配置，默认为 1/10
        List<SysRole> sysRoleList = sysRoleMapper.selectAll(vo);
        return PageUtil.getPageVO(sysRoleList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysRole addRole(RoleAddReqVO vo) {
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(vo,sysRole);
        sysRole.setId(UUID.randomUUID().toString());
        sysRole.setCreateTime(new Date());

        if ( sysRoleMapper.insertSelective(sysRole) != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }

        if ( vo.getPermissions() != null && !vo.getPermissions().isEmpty()) {
            RolePermissionOperationReqVO reqVO =new RolePermissionOperationReqVO();
            reqVO.setRoleId(sysRole.getId());
            reqVO.setPermissionId(vo.getPermissions());
            rolePermissionService.addRolePermission(reqVO);
        }

        return sysRole;
    }

    /**
     * 获取所有的角色
     * 实现方法： 调用mapper底层的sql查询来实现
     * @return 所有角色的集合
     */
    @Override
    public List<SysRole> selectAllRoles() {
        return sysRoleMapper.selectAll(new RolePageReqVO());
    }
}
