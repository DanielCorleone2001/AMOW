package com.daniel.service.impl;

import com.daniel.contains.Constant;
import com.daniel.entity.SysPermission;
import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.mapper.SysPermissionMapper;
import com.daniel.service.PermissionService;
import com.daniel.service.RedisService;
import com.daniel.service.RolePermissionService;
import com.daniel.service.UserRoleService;
import com.daniel.utils.TokenSettings;
import com.daniel.vo.request.permission.PermissionAddReqVO;
import com.daniel.vo.request.permission.PermissionUpdateReqVO;
import com.daniel.vo.response.permission.PermissionRespNodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.daniel.service.impl
 * @ClassName: PermissionServiceImpl
 * @Author: daniel
 * @CreateTime: 2021/2/2 16:47
 * @Description:
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenSettings tokenSettings;

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


    @Override
    public SysPermission addPermission(PermissionAddReqVO vo) {
        SysPermission sysPermission=new SysPermission();
        BeanUtils.copyProperties(vo,sysPermission);
        verifyForm(sysPermission);
        sysPermission.setId(UUID.randomUUID().toString());
        sysPermission.setCreateTime(new Date());
        int insert = sysPermissionMapper.insertSelective(sysPermission);
        if(insert!=1){
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        return sysPermission;
    }
    /**
     * - 操作后的菜单类型是目录的时候 父级必须为目录
     * - 操作后的菜单类型是菜单的时候，父类必须为目录类型
     * - 操作后的菜单类型是按钮的时候 父类必须为菜单类型
     * @Author:     daniel
     * @UpdateUser:
     * @Version:     0.0.1
     * @param sysPermission
     * @return       void
     * @throws
     */
    private void verifyForm(SysPermission sysPermission){

        SysPermission parent=sysPermissionMapper.selectByPrimaryKey(sysPermission.getPid());
        switch (sysPermission.getType()){
            case 1:
                if(parent!=null){
                    if(parent.getType()!=1){
                        throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                    }
                }else if (!sysPermission.getPid().equals("0")){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                }
                break;
            case 2:
                if(parent==null||parent.getType()!=1){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_MENU_ERROR);
                }
                if(StringUtils.isEmpty(sysPermission.getUrl())){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }
                break;
            case 3:
                if(parent==null||parent.getType()!=2){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_BTN_ERROR);
                }
                if(StringUtils.isEmpty(sysPermission.getPerms())){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_PERMS_NULL);
                }
                if(StringUtils.isEmpty(sysPermission.getUrl())){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }
                if(StringUtils.isEmpty(sysPermission.getMethod())){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_METHOD_NULL);
                }
                if(StringUtils.isEmpty(sysPermission.getCode())){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_CODE_NULL);
                }
                break;
        }
    }

    @Override
    public List<PermissionRespNodeVO> permissionTreeList(String userId) {
        return getTree(selectAll());
    }

    @Override
    public List<PermissionRespNodeVO> selectAllByTree() {
        List<SysPermission> list=selectAll();
        return getTree(list,false);

    }


    @Override
    public void updatePermission(PermissionUpdateReqVO permissionUpdateReqVO) {

        //校验数据
        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(permissionUpdateReqVO,permission);
        verifyForm(permission);

        //非空校验
        SysPermission sysPermission = sysPermissionMapper.selectByPrimaryKey(permissionUpdateReqVO.getId());
        if ( sysPermission == null ) {
            log.info("传入的数据无法在数据库中找到");
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        if ( !sysPermission.getPid().equals(permissionUpdateReqVO.getPid()) ||
                sysPermission.getStatus() != permissionUpdateReqVO.getStatus() ) {
            //所属菜单发生了变化，或是权限状态发生了变化，就需要校验该权限是否有子集
            List<SysPermission> sysPermissionList = sysPermissionMapper.selectAllChild(permissionUpdateReqVO.getId());
            if ( sysPermissionList.isEmpty() ) {
                throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_UPDATE);
            }
        }

        permission.setUpdateTime(new Date());

        if ( sysPermissionMapper.updateByPrimaryKeySelective(permission) != 1 ) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }

        //判断权限标识符是否发生了变化
        if ( ! sysPermission.getPerms().equals(permissionUpdateReqVO.getPerms()) ||
                sysPermission.getStatus() != permissionUpdateReqVO.getStatus() ) {
            List<String> roleIdsByPermissionId = rolePermissionService.getRolesByPermissionId(permissionUpdateReqVO.getId());
            if ( !roleIdsByPermissionId.isEmpty() ) {
                List<String> userIdsByRoleIds = userRoleService.getUserIdsByRoleIdList(roleIdsByPermissionId);
                if ( !userIdsByRoleIds.isEmpty() ) {
                    for ( String userId: userIdsByRoleIds ) {
                        redisService.set(Constant.JWT_REFRESH_KEY+userId,userId,
                                tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                        redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
                    }
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePermission(String permissionId) {
        SysPermission sysPermission = sysPermissionMapper.selectByPrimaryKey(permissionId);
        if ( sysPermission == null ) {
            log.error("传入的ID:{} 不合法",permissionId);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        List<SysPermission> childList = sysPermissionMapper.selectAllChild(permissionId);//获取子集权限
        if ( !childList.isEmpty() ) {
            throw new BusinessException(BaseResponseCode.ROLE_PERMISSION_RELATION);
        }

        sysPermission.setDeleted(0);
        sysPermission.setUpdateTime(new Date());
        if ( sysPermissionMapper.updateByPrimaryKeySelective(sysPermission) != 1 ) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }

        List<String> roleIDs = rolePermissionService.getRolesByPermissionId(permissionId);//获取角色ID
        rolePermissionService.removeByPermissionId(permissionId);//通过权限ID删除和角色关联

        if ( !roleIDs.isEmpty() ) {
            List<String> userIDs = userRoleService.getUserIdsByRoleIdList(roleIDs);
            if ( !userIDs.isEmpty() ) {
                for ( String userID : userIDs ) {
                    redisService.set(Constant.JWT_REFRESH_KEY+userID,userID,
                                    tokenSettings.getAccessTokenExpireTime().toMillis(),TimeUnit.MILLISECONDS);
                }
            }
        }
    }


    //修改菜单权限树递归方法，新增一个参数type：true(只查询目录和菜单) false(查询目录、菜单、按钮权限)
    /**
     * 递归获取菜单树
     * @Author:     daniel
     * @UpdateUser:
     * @Version:     0.0.1
     * @param all
    • * @param type true:查询到菜单；false:查询到按钮
     * @return
     * @throws
     */
    private List<PermissionRespNodeVO> getTree(List<SysPermission> all,boolean type){
        List<PermissionRespNodeVO> list=new ArrayList<>();
        if (all==null||all.isEmpty()){
            return list;
        }
        for(SysPermission sysPermission:all){
            if(sysPermission.getPid().equals("0")){
                PermissionRespNodeVO permissionRespNode=new PermissionRespNodeVO();
                BeanUtils.copyProperties(sysPermission,permissionRespNode);
                permissionRespNode.setTitle(sysPermission.getName());
                if(type){
                    permissionRespNode.setChildren(getChildExBtn(sysPermission.getId(),all));
                }else {
                    permissionRespNode.setChildren(getChildAll(sysPermission.getId(),all));
                }
                list.add(permissionRespNode);
            }
        }
        return list;
    }
    /**
     * 递归遍历所有
     * @Author:     daniel
     * @UpdateUser:
     * @Version:     0.0.1
     * @param id
    • * @param all
     * @return
     * @throws
     */
    private List<PermissionRespNodeVO>getChildAll(String id,List<SysPermission> all){
        List<PermissionRespNodeVO> list=new ArrayList<>();
        for(SysPermission sysPermission:all){
            if(sysPermission.getPid().equals(id)){
                PermissionRespNodeVO permissionRespNode=new PermissionRespNodeVO();
                BeanUtils.copyProperties(sysPermission,permissionRespNode);
                permissionRespNode.setTitle(sysPermission.getName());
                permissionRespNode.setChildren(getChildAll(sysPermission.getId(),all));
                list.add(permissionRespNode);
            }
        }
        return list;
    }

    /**
     * 只递归到菜单
     * @Author:      daniel
     * @UpdateUser:
     * @Version:     0.0.1
     * @param id
     * @param all
     * @return       java.util.List<.vo.resp.PermissionRespNodeVO>
     * @throws
     */
    private List<PermissionRespNodeVO> getChildExBtn(String id,List<SysPermission> all){
        List<PermissionRespNodeVO> list=new ArrayList<>();
        for (SysPermission s:
                all) {
            if(s.getPid().equals(id)&&s.getType()!=3){
                PermissionRespNodeVO respNodeVO=new PermissionRespNodeVO();
                BeanUtils.copyProperties(s,respNodeVO);
                respNodeVO.setTitle(s.getName());
                respNodeVO.setChildren(getChildExBtn(s.getId(),all));
                list.add(respNodeVO);
            }
        }
        return list;
    }
}
