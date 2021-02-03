package com.daniel.service.impl;

import com.daniel.entity.SysPermission;
import com.daniel.mapper.SysPermissionMapper;
import com.daniel.service.PermissionService;
import com.daniel.vo.response.PermissionRespNodeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public List<PermissionRespNodeVO> selectAllMenuByTree() {
        List<SysPermission> list =selectAll();
        List<PermissionRespNodeVO> result = new ArrayList<>();

        PermissionRespNodeVO respNodeVO = new PermissionRespNodeVO();
        respNodeVO.setId("0");
        respNodeVO.setTitle("顶级菜单栏");
        respNodeVO.setSpread(true);
        respNodeVO.setChildren(getTree(list));
        result.add(respNodeVO);
        return result;
    }

    //递归获取目录和菜单
    private List<PermissionRespNodeVO> getChileMenu(String id, List<SysPermission> list) {
        List<PermissionRespNodeVO> result = new ArrayList<>();

        for ( SysPermission sysPermission : list ) {
            if ( sysPermission.getPid().equals(id) && sysPermission.getType() != 3 ) {
                PermissionRespNodeVO permissionRespNodeVO = new PermissionRespNodeVO();
                BeanUtils.copyProperties(sysPermission,permissionRespNodeVO);
                permissionRespNodeVO.setTitle(sysPermission.getName());
                permissionRespNodeVO.setChildren(getChileMenu(sysPermission.getId(),list));
                result.add(permissionRespNodeVO);
            }
        }
        return result;
    }

    //递归获取得到菜单树
    private List<PermissionRespNodeVO> getTree(List<SysPermission> list) {
        List<PermissionRespNodeVO> result = new ArrayList<>();

        if (list == null || list.isEmpty()) {
            return result;
        }

        for ( SysPermission sysPermission : list ) {
            if (sysPermission.getPid().equals("0")) {
                PermissionRespNodeVO permissionRespNodeVO = new PermissionRespNodeVO();
                BeanUtils.copyProperties(sysPermission,permissionRespNodeVO);
                permissionRespNodeVO.setTitle(sysPermission.getName());
                permissionRespNodeVO.setChildren(getChileMenu(sysPermission.getId(),list));
                result.add(permissionRespNodeVO);
            }
        }
        return result;
    }
}
