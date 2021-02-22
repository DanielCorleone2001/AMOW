package com.daniel.service.impl;

import com.daniel.entity.SysUserRole;
import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.mapper.SysUserRoleMapper;
import com.daniel.service.UserRoleService;
import com.daniel.vo.request.related.UserOwnRoleReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Package: com.daniel.service.impl
 * @ClassName: UserRoleServiceImpl
 * @Author: daniel
 * @CreateTime: 2021/2/21 9:42
 * @Description:
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 通过用户的ID来获取用户所拥有的角色
     * 实现方法：通过调用mapper底层的sql语句来实现
     * @param userId
     * @return
     */
    @Override
    public List<String> getRoleIdsByUserId(String userId) {
        return sysUserRoleMapper.getRoleIdsByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUserRoleInfo(UserOwnRoleReqVO vo) {
        sysUserRoleMapper.removeByUserId(vo.getUserId());//删除掉原来的数据

        if ( vo.getRoleId() == null || vo.getRoleId().isEmpty() ) return;//非空检验

        Date createTime = new Date();

        List<SysUserRole> list = new ArrayList<>();
        //遍历 新建角色来注入vo中的值 从而达到新用户角色信息的功能
        for (String roleId : vo.getRoleId() ) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setId(UUID.randomUUID().toString());
            sysUserRole.setCreateTime(createTime);
            sysUserRole.setUserId(vo.getUserId());
            sysUserRole.setRoleId(roleId);
            list.add(sysUserRole);
        }

        if ( sysUserRoleMapper.batchInsertUserRole(list) == 0 )
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
    }

    @Override
    public List<String> getUserIdsByRoleId(String roleIds) {
        return sysUserRoleMapper.getUserIdsByRoleId(roleIds);
    }

    @Override
    public List<String> getUserIdsByRoleIdList(List<String> roleIdList) {
        return sysUserRoleMapper.getUserIdsByRoleIdList(roleIdList);
    }
}
