package com.daniel.shiro;

import com.daniel.contains.Constant;
import com.daniel.service.permission.PermissionService;
import com.daniel.service.redis.RedisService;
import com.daniel.service.role.RoleService;
import com.daniel.utils.jwt.JWToken;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Package: com.daniel.shiro
 * @ClassName: MyRealm
 * @Author: daniel
 * @CreateTime: 2021/1/31 21:52
 * @Description:
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof MyPasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String accessToken= (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        String userId=JWToken.getUserId(accessToken);
        /**
         * 通过剩余的过期时间比较如果token的剩余过期时间大与标记key的剩余过期时间
         * 就说明这个tokne是在这个标记key之后生成的
         */
        if(redisService.hasKey(Constant.JWT_REFRESH_KEY+userId)&&redisService.getExpire(Constant.JWT_REFRESH_KEY+userId,
                TimeUnit.MILLISECONDS)>JWToken.getRemainingTime(accessToken)){
            List<String> roleNames = roleService.getRoleNameListByUserId(userId);
            if(roleNames!=null&&!roleNames.isEmpty()){
                info.addRoles(roleNames);
            }
            List<String> permissions=permissionService.getPermissionListByUserId(userId);
            if(permissions!=null){
                info.addStringPermissions(permissions);
            }
        }else {
            Claims claims= JWToken.getClaimsFromToken(accessToken);
            /**
             * 返回该用户的角色信息给授权期
             */
            if(claims.get(Constant.ROLES_INFOS_KEY)!=null){
                info.addRoles((Collection<String>) claims.get(Constant.ROLES_INFOS_KEY));
            }
            /**
             * 返回该用户的权限信息给授权器
             */
            if(claims.get(Constant.PERMISSIONS_INFOS_KEY)!=null){
                info.addStringPermissions((Collection<String>)
                        claims.get(Constant.PERMISSIONS_INFOS_KEY));
            }
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        MyPasswordToken passwordToken = (MyPasswordToken) authenticationToken;
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(passwordToken.getPrincipal(),passwordToken.getCredentials(),this.getName());
        return info;
    }
}
