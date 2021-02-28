package com.daniel.service.impl;

import com.daniel.contains.Constant;
import com.daniel.entity.SysDept;
import com.daniel.entity.SysRole;
import com.daniel.entity.SysUser;
import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.mapper.SysDeptMapper;
import com.daniel.mapper.SysUserMapper;
import com.daniel.service.redis.RedisService;
import com.daniel.service.role.RoleService;
import com.daniel.service.user.UserRoleService;
import com.daniel.service.user.UserService;
import com.daniel.utils.jwt.JWToken;
import com.daniel.utils.page.PageUtil;
import com.daniel.utils.pwd.PasswordUtils;
import com.daniel.utils.token.TokenSettings;
import com.daniel.vo.request.login.LoginReqVO;
import com.daniel.vo.request.related.UserOwnRoleReqVO;
import com.daniel.vo.request.user.*;
import com.daniel.vo.response.login.LoginRespVO;
import com.daniel.vo.response.page.PageVO;
import com.daniel.vo.response.related.UserOwnRoleRespVO;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.daniel.service.impl
 * @ClassName: UserServiceImpl
 * @Author: daniel
 * @CreateTime: 2021/1/30 23:30
 * @Description:
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenSettings tokenSettings;

    @Override
    public LoginRespVO login(LoginReqVO loginReqVO) {
        SysUser sysUser = sysUserMapper.selectByUsername(loginReqVO.getUsername());
        if ( sysUser == null ) {//用户不存在，返回错误提示
            throw new BusinessException(BaseResponseCode.ACCOUNT_ERROR);
        }

        if ( sysUser.getStatus() == 2 ) {//用户已经被锁定
            throw new BusinessException( BaseResponseCode.ACCOUNT_LOCK_TIP);
        }

        //检查密码是否正确
        if ( !PasswordUtils.matches(sysUser.getSalt(),loginReqVO.getPassword(),sysUser.getPassword())) {
            throw new BusinessException(BaseResponseCode.ACCOUNT_PASSWORD_ERROR);
        }

        Map<String,Object> claims = new HashMap<>();//负载用mapper来存
        claims.put(Constant.JWT_USER_NAME,sysUser.getUsername());//用户名映射
        claims.put(Constant.ROLES_INFOS_KEY,getRoleByUserId(sysUser.getId()));//角色名称映射
        claims.put(Constant.PERMISSIONS_INFOS_KEY, getPermissionByUserId(sysUser.getId()));//权限映射
        String accessToken = JWToken.getAccessToken(sysUser.getId(),claims);//生成token
        log.info("accessToken={}",accessToken);//输出token

        Map<String,Object> refreshTokenCLaims = new HashMap<>();
        refreshTokenCLaims.put(Constant.JWT_USER_NAME,sysUser.getUsername());

        //判断登录的方式 1为PC端,2为移动端
        String refreshToken = null;
        if ( loginReqVO.getType().equals("1")) {
            refreshToken = JWToken.getRefreshToken(sysUser.getId(),refreshTokenCLaims);//PC端
        } else {
            refreshToken = JWToken.getRefreshAppToken(sysUser.getId(),refreshTokenCLaims);//移动端
        }

        log.info("refreshToken={}",refreshToken);
        LoginRespVO loginRespVO = new LoginRespVO();
        loginRespVO.setUserId(sysUser.getId());
        loginRespVO.setRefreshToken(refreshToken);
        loginRespVO.setAccessToken(accessToken);
        return loginRespVO;
    }

    @Override
    public PageVO<SysUser> pageInfo(UserPageReqVO userPageReqVO) {
        PageHelper.startPage(userPageReqVO.getPageNum(),userPageReqVO.getPageSize());//开始分页
        List<SysUser> sysUserList = sysUserMapper.selectAll(userPageReqVO);//获取所有用户的信息，存入List
        //PageInfo<SysUser> pageInfo = new PageInfo<>(sysUserList);//构造对应的分页信息
        for (SysUser user : sysUserList) {
            SysDept sysDept = sysDeptMapper.selectByPrimaryKey(user.getDeptId());
            if(sysDept!=null){
                user.setDeptName(sysDept.getName());
            }
        }
        return PageUtil.getPageVO(sysUserList);//返回分页信息
    }

    @Override
    public void addUser(UserAddReqVO userAddReqVO) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userAddReqVO,sysUser);
        //配置基本信息
        sysUser.setSalt(PasswordUtils.getSalt());
        String encode = PasswordUtils.encode(userAddReqVO.getPassword(),sysUser.getSalt());
        sysUser.setPassword(encode);
        sysUser.setId(UUID.randomUUID().toString());
        sysUser.setCreateTime(new Date());
        if ( sysUserMapper.insertSelective(sysUser) != 1) {//判断是否插入成功
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
    }

    @Override
    public UserOwnRoleRespVO getUserOwnRole(String userId) {
        List<String> roleIdsByUserId =userRoleService.getRoleIdsByUserId(userId);//先获取用户的角色
        List<SysRole> roleList = roleService.selectAllRoles();//获取所有角色

        //配置属性
        UserOwnRoleRespVO userOwnRoleRespVO = new UserOwnRoleRespVO();
        userOwnRoleRespVO.setAllRole(roleList);
        userOwnRoleRespVO.setOwnRoles(roleIdsByUserId);
        return userOwnRoleRespVO;
    }

    /**
     * @note 配置用户所拥有的角色
     * @param vo
     */
    @Override
    public void setUserOwnRole(UserOwnRoleReqVO vo) {
        userRoleService.addUserRoleInfo(vo);
        redisService.set(Constant.JWT_REFRESH_KEY+vo.getUserId(),vo.getUserId(),
                tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public String refreshToken(String refreshToken) {
        /**
         * 判断token是否过期，是否被加入黑名单
         */
        if ( !JWToken.validateToken(refreshToken) || redisService.hasKey(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken)) {
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }

        String userId = JWToken.getUserId(refreshToken);
        String userName = JWToken.getUserName(refreshToken);

        log.info("userID = {}",userId);

        //通过刷新的token来进行相关的属性配置
        Map<String,Object> claims = new HashMap<>();
        claims.put(Constant.ROLES_INFOS_KEY,getRoleByUserId(userId));
        claims.put(Constant.PERMISSIONS_INFOS_KEY,getPermissionByUserId(userId));
        claims.put(Constant.JWT_USER_NAME,userName);

        String newAccessToken = JWToken.getAccessToken(userId,claims);
        return newAccessToken;
    }

    /**
     * 更新用户信息
     * @param userUpdateReqVO 前端更新的数据
     * @param operationId 操作人的ID
     */
    @Override
    public void updateUserInfo(UserUpdateReqVO userUpdateReqVO, String operationId) {
        SysUser user = sysUserMapper.selectByPrimaryKey(userUpdateReqVO.getId());

        //非空判断
        if ( user == null) {
            log.info("传入的ID: {}不合法",userUpdateReqVO.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        //基本属性的更新
        BeanUtils.copyProperties(userUpdateReqVO,user);
        user.setUpdateTime(new Date());//时间
        //密码
        if ( !StringUtils.isEmpty(userUpdateReqVO.getPassword()) ) {
            String newPassword = PasswordUtils.encode(userUpdateReqVO.getPassword(),user.getSalt());
            user.setPassword(newPassword);
        } else {
            user.setPassword(null);
        }
        //操作人员
        user.setUpdateId(operationId);
        if ( sysUserMapper.updateByPrimaryKeySelective(user) != 1 ) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }

        if ( userUpdateReqVO.getStatus() == 2 ) {
            redisService.set(Constant.ACCOUNT_LOCK_KEY+user.getId(),user.getId());
        } else {
            redisService.delete(Constant.ACCOUNT_LOCK_KEY+user.getId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteUsers(List<String> userIds, String operationId) {
        SysUser user = new SysUser();
        user.setUpdateId(operationId);
        user.setUpdateTime(new Date());
        user.setDeleted(0);

        if ( sysUserMapper.deleteUsers(user,userIds) == 0) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }

        //将用户ID标记为已经删除
        for ( String userId : userIds ) {
            redisService.set(Constant.DELETED_USER_KEY+userId,userId,
                    tokenSettings.getRefreshTokenExpireAppTime().toMillis(),TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        //token非空判断
        if ( StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken) ) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        Subject subject = SecurityUtils.getSubject();
        log.info("subject.getPrincipals() = {}",subject.getPrincipals());

        if ( subject.isAuthenticated() ) {
            subject.logout();
        }

        String userId = JWToken.getUserId(accessToken);
        //将token加入黑名单 禁止再登录
        redisService.set(Constant.JWT_ACCESS_TOKEN_BLACKLIST+accessToken,
                        userId,JWToken.getRemainingTime(accessToken),TimeUnit.MILLISECONDS);
        //将refreshToken加入黑名单，防止重新拿来刷新
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken,
                        userId,JWToken.getRemainingTime(refreshToken),TimeUnit.MILLISECONDS);

    }

    @Override
    public SysUser getUserDetailInfo(String userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void updateUserDetailInfo(String userId, UserDetailINfoReqVO userDetailINfoReqVO) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userDetailINfoReqVO,user);
        user.setId(userId);
        user.setUpdateTime(new Date());
        user.setUpdateId(userId);

        if ( sysUserMapper.updateByPrimaryKeySelective(user) != 1 ) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
    }

    /**
     * 更新用户密码
     * @param userId
     * @param userEditPasswordReqVO
     * @param refreshToken
     * @param accessToken
     */
    @Override
    public void updateUserPwd(String userId, UserEditPasswordReqVO userEditPasswordReqVO, String refreshToken, String accessToken) {
        SysUser user = sysUserMapper.selectByPrimaryKey(userId);
        if ( user == null ) {
            log.error("传入的ID: {} 不合法",userId);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }

        //检验输入的旧密码是否匹配
        if ( !PasswordUtils.matches(user.getSalt(),userEditPasswordReqVO.getOldPwd(),user.getPassword()) ) {
            throw new BusinessException(BaseResponseCode.OLD_PASSWORD_ERROR);
        }

        //更新密码与操作时间
        user.setUpdateTime(new Date());
        user.setPassword(PasswordUtils.encode(userEditPasswordReqVO.getNewPwd(),user.getSalt()));

        //更新操作检验
        if ( sysUserMapper.updateByPrimaryKeySelective(user) != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }

        /**
         * 将token加入黑名单，禁止再用此token进行登录
         */
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+accessToken,userId,
                        JWToken.getRemainingTime(accessToken),TimeUnit.MILLISECONDS);

        /**
         * 将refreshToken加入黑名单
         */
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken,userId,
                        JWToken.getRemainingTime(refreshToken),TimeUnit.MILLISECONDS);
    }

    /**
     * 通过用户ID来查找用户所拥有的权限
     * @param userId
     * @return
     */
    private List<String> getRoleByUserId(String userId) {
        List<String> list = new ArrayList<>();
        if ( userId.equals("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8")) {
            list.add("admin");
        } else {
            list.add("dev");
        }
        return list;
    }

    private List<String> getPermissionByUserId( String userID) {
        List<String> list = new ArrayList<>();
         if ( userID.equals("9a26f5f1-cbd2-473d-82db-1d6dcf4598f8")) {
             list.add("sys:user:add");
             list.add("sys:user:update");
             list.add("sys:user:delete");
             list.add("sys:user:list");
         } else {
             list.add("sys:user:list");
         }
         return list;
    }
}
