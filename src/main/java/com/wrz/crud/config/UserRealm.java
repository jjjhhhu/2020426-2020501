package com.wrz.crud.config;

import com.wrz.crud.entity.SysRole;
import com.wrz.crud.entity.SysUser;
import com.wrz.crud.entity.SysUserRole;
import com.wrz.crud.service.ISysRoleService;
import com.wrz.crud.service.ISysUserRoleService;
import com.wrz.crud.service.ISysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wrz
 * @data 2020/4/27-14:49
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Autowired
    private ISysRoleService roleService;

    /**
     * 执行授权逻辑
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        System.out.println("执行授权逻辑");

        // 给资源进行授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // 从数据库查询是当前用户权限
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser)subject.getPrincipal();

        SysUserRole sysUserRole = sysUserRoleService.findByUserId(sysUser.getId());
        SysRole sysRole = roleService.findByRoleId(sysUserRole.getRoleId());

        // 将对应权限加入当前用户
        info.addStringPermission(sysRole.getName());

        return info;
    }

    /**
     * 执行认证逻辑
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        System.out.println("执行认证逻辑 ->");

        // 判断用户名和密码
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
        String username = token.getUsername();

        SysUser sysUser = sysUserService.findByName(token.getUsername());

        if(sysUser == null) {
            return null;
        }

        Object principal = username;
        Object credentials = sysUser.getPassword();
        String realmName = getName();


        ByteSource salt = ByteSource.Util.bytes(username);

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(sysUser, credentials,salt,realmName);

        return info;
    }
}
