package com.daniel.service.impl;

import com.daniel.contains.Constant;
import com.daniel.entity.SysRole;
import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.mapper.SysRoleMapper;
import com.daniel.mapper.SysUserRoleMapper;
import com.daniel.service.*;
import com.daniel.utils.PageUtil;
import com.daniel.utils.TokenSettings;
import com.daniel.vo.request.role.RoleAddReqVO;
import com.daniel.vo.request.role.RolePageReqVO;
import com.daniel.vo.request.related.RolePermissionOperationReqVO;
import com.daniel.vo.request.role.RoleUpdateReqVO;
import com.daniel.vo.response.page.PageVO;
import com.daniel.vo.response.permission.PermissionRespNodeVO;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.daniel.service.impl
 * @ClassName: RoleServiceImpl
 * @Author: daniel
 * @CreateTime: 2021/2/7 20:31
 * @Description:
 */

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenSettings tokenSettings;

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

    /**
     * 获取角色相关的信息，比如菜单权限
     * @param roleID
     * @return
     */
    @Override
    public SysRole detailInfo(String roleID) {
        SysRole sysRole = sysRoleMapper.selectByPrimaryKey(roleID);//获取当前的角色

        //角色非空检验
        if ( sysRole == null ) {
            log.error("传入的ID {}不合法",roleID);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        //获取所有权限菜单权限树
        List<PermissionRespNodeVO> permissionRespNodeVOList = permissionService.selectAllByTree();
        //获取该角色拥有的菜单权限
        List<String> permissionList = rolePermissionService.getPermissionListByRoleId(roleID);
        Set<String> checkList = new HashSet<>(permissionList);
        //遍历菜单权限树的数据
        setChecked(permissionRespNodeVOList,checkList);
        sysRole.setPermissionRespNode(permissionRespNodeVOList);
        return sysRole;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(RoleUpdateReqVO roleUpdateReqVO) {
        //获取需要更新的角色
        SysRole updatedRole = sysRoleMapper.selectByPrimaryKey(roleUpdateReqVO.getId());
        //角色非空校验
        if ( updatedRole == null ) {
            log.error("传入的角色ID: {} 不合法",roleUpdateReqVO.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        //角色的信息更新
        BeanUtils.copyProperties(roleUpdateReqVO,updatedRole);
        updatedRole.setUpdateTime(new Date());

        //是否成功更新
        if ( sysRoleMapper.updateByPrimaryKeySelective(updatedRole) != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }

        //配置角色的菜单权限集合
        RolePermissionOperationReqVO reqVO = new RolePermissionOperationReqVO();
        reqVO.setRoleId(updatedRole.getId());
        reqVO.setPermissionId(roleUpdateReqVO.getPermissionList());
        rolePermissionService.addRolePermission(reqVO);

        List<String> userIDList = userRoleService.getUserIdsByRoleId(roleUpdateReqVO.getId());
        if ( !userIDList.isEmpty() ) {
            for (String userID : userIDList ) {
                //标记用户,在用户认证的时候判断这个是否主动刷过
                redisService.set(Constant.JWT_REFRESH_KEY+userID,userID,
                                    tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                //清除用户授权数据缓存
                redisService.delete(Constant.IDENTIFY_CACHE_KEY+userID);
            }
        }
    }

    @Override
    public void deleteRole(String roleID) {
        SysRole targetRole = new SysRole();
        targetRole.setId(roleID);//获取需要删除的ID
        targetRole.setUpdateTime(new Date());
        targetRole.setDeleted(0);

        //更新数据的检验
        if ( sysRoleMapper.updateByPrimaryKeySelective(targetRole) != 1 ) {
            log.error("传入的角色ID {} 不合法",roleID);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        List<String> userIDList = sysUserRoleMapper.getUserIdsByRoleId(roleID);//获取拥有该角色的用户ID列表
        //删除掉含有角色权限 对应的用户和菜单权限
        sysUserRoleMapper.removeByUserId(roleID);
        rolePermissionService.removeRoleByID(roleID);

        //刷新token
        if ( !userIDList.isEmpty() ) {
            for (String userID : userIDList ) {
                /**
                 * 标记用户 在用户认证的时候判断这个是否主动刷过
                 */
                redisService.set(Constant.JWT_REFRESH_KEY+userID,userID,tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                /**
                 * 清楚用户授权数据缓存
                 */
                redisService.delete(Constant.IDENTIFY_CACHE_KEY+userID);
            }
        }
    }

    private void setChecked(List<PermissionRespNodeVO> list, Set<String> checkList){

        for(PermissionRespNodeVO node:list){
            /**
             * 子集选中从它往上到跟目录都被选中，父级选中从它到它所有的叶子节点都会被选中
             * 这样我们就直接遍历最底层及就可以了
             */
            if(checkList.contains(node.getId())&&(node.getChildren()==null||node.getChildren().isEmpty())){
                node.setChecked(true);
            }
            setChecked((List<PermissionRespNodeVO>) node.getChildren(),checkList);

        }
    }
}
