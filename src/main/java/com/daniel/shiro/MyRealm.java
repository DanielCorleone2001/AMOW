package com.daniel.shiro;

import com.daniel.contains.Constant;
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

import java.util.Collection;

/**
 * @Package: com.daniel.shiro
 * @ClassName: MyRealm
 * @Author: daniel
 * @CreateTime: 2021/1/31 21:52
 * @Description:
 */
public class MyRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof MyPasswordToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String accessToken = (String)principalCollection.getPrimaryPrincipal();
        Claims claimsFromToken = JWToken.getClaimsFromToken(accessToken);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        if ( claimsFromToken.get(Constant.PERMISSIONS_INFOS_KEY) != null) {
            info.addStringPermissions((Collection<String>) claimsFromToken.get(Constant.PERMISSIONS_INFOS_KEY));
        }

        if ( claimsFromToken.get(Constant.ROLES_INFOS_KEY) != null ) {
            info.addRoles((Collection<String>) claimsFromToken.get(Constant.ROLES_INFOS_KEY));
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
