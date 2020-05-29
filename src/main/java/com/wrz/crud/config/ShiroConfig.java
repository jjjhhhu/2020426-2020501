package com.wrz.crud.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro 的配置类
 * @author wrz
 * @data 2020/4/27-14:47
 */
@Configuration
public class ShiroConfig {


    /**
     * 创建ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManger") DefaultWebSecurityManager securityManger) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManger);

        // 添加shiro内置过滤器
        /**
         * shiro内置过滤器，可以实现权限相关的拦截器
         *      常用过滤器：
         *          anon : 无需认证，可以访问
         *          authc: 必须进过授权
         *          user: 如果使用rememberMe的功能可以直接访问
         *          perms: 该资源必须得到资源权限才可以访问
         *          role: 该资源必须的到角色权限才可以访问
         */
        // 写map集合
        Map<String, String> filterMap = new LinkedHashMap<String, String>();

        // 静态资源放行
        filterMap.put("/assets/**", "anon");
        filterMap.put("/css/**","anon");
        filterMap.put("/js/**", "anon");
        filterMap.put("/layuiadmin/**", "anon");

        // 放行页面：登陆、执行登录、注册、执行注册、找回密码相关
        filterMap.put("/user/**", "anon");

        // jquery模板放行
        filterMap.put("/model/**", "anon");

        // 后台放行
        filterMap.put("/admin/**", "anon");


        // 获取验证码放行
        filterMap.put("/verifycode/get", "anon");

        // 授权过滤器
//        filterMap.put("/admin/**", "perms[admin]");


        // 退出登录
        filterMap.put("/logout", "logout");


        // 修改调整的登陆页面
        shiroFilterFactoryBean.setLoginUrl("/user/");
        // 未授权跳转页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unAuth");

        filterMap.put("/**","authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);


        return shiroFilterFactoryBean;
    }


    /**
     * 创建 DefaultWebSecurityManager
     */
    @Bean(name = "securityManger")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 关联realm
        securityManager.setRealm(userRealm);

        return securityManager;
    }

    /**
     * 创建Realm
     */
    @Bean(name = "userRealm")
    public UserRealm getRealm() {
        UserRealm userRealm = new UserRealm();
        //shiroRealm.setCacheManager(cacheManager());
        //加入密码管理
        //shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());//Shiro自带密码管理器
        userRealm.setCredentialsMatcher(new CustomCredentialsMatcher());//自定义密码管理器
        return userRealm;
    }

    /**
     * 配置shiroDialect，用于thymleaf和shiro标签配合使用
     */
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }

    /**
     * Shiro自带密码管理器
     *
     * @return HashedCredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        //Shiro自带加密
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //散列算法使用md5
        credentialsMatcher.setHashAlgorithmName("md5");
        //散列次数，2表示md5加密两次
        credentialsMatcher.setHashIterations(1024);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }


    /**
     * 自定义认证加密方式
     */
    public static class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
        @Override
        public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
            UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
            //加密类型，密码，盐值，迭代次数
            Object tokenCredentials = new SimpleHash("md5", token.getPassword(), token.getUsername(), 1024).toHex();
            //数据库存储密码
            Object accountCredentials = getCredentials(info);
            //将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
            return equals(tokenCredentials, accountCredentials);
        }
    }


}
