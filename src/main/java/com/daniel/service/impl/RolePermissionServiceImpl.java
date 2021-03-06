package com.daniel.service.impl;

import com.daniel.entity.SysRolePermission;
import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.mapper.SysRolePermissionMapper;
import com.daniel.service.role.RolePermissionService;
import com.daniel.vo.request.related.RolePermissionOperationReqVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Package: com.daniel.service.impl
 * @ClassName: RolePermissionServiceImpl
 * @Author: daniel
 * @CreateTime: 2021/2/9 20:09
 * @Description:
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    public void addRolePermission(RolePermissionOperationReqVO vo) {

        sysRolePermissionMapper.removeByRoleId(vo.getRoleId());

        if ( vo.getPermissionIds() == null || vo.getPermissionIds().isEmpty()) return;

        Date createTime = new Date();

        List<SysRolePermission> list = new ArrayList<>();

        for ( String permissionId: vo.getPermissionIds()) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setId(UUID.randomUUID().toString());
            sysRolePermission.setCreateTime(createTime);
            sysRolePermission.setPermissionId(permissionId);
            sysRolePermission.setRoleId(vo.getRoleId());
            list.add(sysRolePermission);
        }

        if ( sysRolePermissionMapper.batchInsertRolePermission(list) == 0 ) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
    }

    /**
     * 通过权限ID来获取对应的角色ID
     * @param permissionId
     * @return
     */
    @Override
    public List<String> getRolesByPermissionId(String permissionId) {
        return sysRolePermissionMapper.getRoleIdsByPermissionId(permissionId);
    }

    @Override
    public int removeByPermissionId(String permissionId) {
        return sysRolePermissionMapper.removeByPermissionId(permissionId);
    }

    @Override
    public List<String> getPermissionListByRoleId(String roleId) {
        return sysRolePermissionMapper.getPermissionIdsByRoleId(roleId);
    }

    @Override
    public int removeRoleByID(String roleID) {
        return sysRolePermissionMapper.removeRoleByID(roleID);
    }

    @Override
    public List<String> getPermissionIdListByRoleIdList(List<String> roleIdList) {
        return sysRolePermissionMapper.getPermissionIdListByRoleIdList(roleIdList);
    }

}
