package com.daniel.service.impl;

import com.daniel.contains.Constant;
import com.daniel.entity.SysUser;
import com.daniel.exception.BusinessException;
import com.daniel.exception.code.BaseResponseCode;
import com.daniel.mapper.SysUserMapper;
import com.daniel.service.UserService;
import com.daniel.utils.JWToken;
import com.daniel.utils.PageUtil;
import com.daniel.utils.PasswordUtils;
import com.daniel.vo.request.LoginReqVO;
import com.daniel.vo.request.UserAddReqVO;
import com.daniel.vo.request.UserPageReqVO;
import com.daniel.vo.response.LoginRespVO;
import com.daniel.vo.response.PageVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Override
    public LoginRespVO login(LoginReqVO loginReqVO) {
        SysUser sysUser = sysUserMapper.selectByUsername(loginReqVO.getUsername());
        if ( sysUser == null ) {//用户不存在，返回错误提示
            throw new BusinessException(BaseResponseCode.ACCOUNT_ERROR);
        }

        if ( sysUser.getStatus() == 2 ) {//用户已经被锁定
            throw new BusinessException( BaseResponseCode.ACCOUNT_LOCK);
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
        List<SysUser> sysUserList = sysUserMapper.selectAll();//获取所有用户的信息，存入List
        //PageInfo<SysUser> pageInfo = new PageInfo<>(sysUserList);//构造对应的分页信息
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
