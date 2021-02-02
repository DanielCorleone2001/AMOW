package com.daniel.service.impl;

import com.daniel.entity.SysPermission;
import com.daniel.mapper.SysPermissionMapper;
import com.daniel.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Package: com.daniel.service.impl
 * @ClassName: PermissionServiceImpl
 * @Author: daniel
 * @CreateTime: 2021/2/2 16:47
 * @Description:
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<SysPermission> selectAll() {
        List<SysPermission> result = sysPermissionMapper.selectAll();

        if ( result != null ) {
            for ( SysPermission sysPermission : result) {
                SysPermission parent = sysPermissionMapper.selectByPrimaryKey(sysPermission.getPid());

                if ( parent != null ) {
                    sysPermission.setPidName(parent.getName());
                }
            }
        }
        return result;
    }
}
