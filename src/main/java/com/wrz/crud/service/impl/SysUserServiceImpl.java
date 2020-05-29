package com.wrz.crud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wrz.crud.entity.SysRole;
import com.wrz.crud.entity.SysUser;
import com.wrz.crud.mapper.SysUserMapper;
import com.wrz.crud.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wrz
 * @since 2020-04-28
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;


    /**
     *  保存用户信息
     * @param sysUser
     * @return
     */
    @Override
    public boolean regUser(SysUser sysUser) {

        String hashAlgorithmName = "MD5";
        Object credentials = sysUser.getPassword();
        Object salt = ByteSource.Util.bytes(sysUser.getUsername());
        int hasIterations = 1024;
        Object result = new SimpleHash(hashAlgorithmName,credentials,salt,hasIterations);
        System.out.println("加密后的密码为 -> " + result);

        String phoneStr = sysUser.getPassword();

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String trueTime = df.format(date);
        sysUser.setGender(trueTime);
        sysUser.setPassword(String.valueOf(result));

        LambdaQueryWrapper<SysUser> lambda = new QueryWrapper<SysUser>().lambda();
        lambda.eq(SysUser::getUsername,sysUser.getUsername()).eq(SysUser::getPassword,sysUser.getPassword())
                .eq(SysUser::getPhone,phoneStr).eq(SysUser::getEmail,sysUser.getEmail()).eq(SysUser::getGender,sysUser.getGender());
        int insert = sysUserMapper.insert(sysUser);

        if(insert != 0) {
            return true;
        }

        return false;

    }

    /**
     * 通过邮箱找到邮箱地址
     * @param email
     * @return
     */
    @Override
    public SysUser findByEmail(String email) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new QueryWrapper<SysUser>().lambda();
        lambdaQueryWrapper.eq(SysUser::getEmail, email);
        SysUser sysUser = sysUserMapper.selectOne(lambdaQueryWrapper);
        if(sysUser != null) {
            return sysUser;
        }
        return null;
    }

    /**
     * 查询所有人员信息
     */
    @Override
    public List<SysUser> selectAll() {

        List<SysUser> sysUsersList = sysUserMapper.selectList(null);

        return sysUsersList;
    }


    /**
     * 通过手机号码找到邮箱地址
     * @param phoneNum
     * @return
     */
    @Override
    public SysUser findByPhoneNum(String phoneNum) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new QueryWrapper<SysUser>().lambda();
        lambdaQueryWrapper.eq(SysUser::getPhone, phoneNum);
        SysUser sysUser = sysUserMapper.selectOne(lambdaQueryWrapper);
        if(sysUser != null) {
            return sysUser;
        }
        return null;
    }

    /**
     * 修改密码
     * @param email
     * @return
     */
    @Override
    public SysUser changePassword(String email,String password) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new QueryWrapper<SysUser>().lambda();
        lambdaQueryWrapper.eq(SysUser::getEmail, email);
        SysUser sysUser = sysUserMapper.selectOne(lambdaQueryWrapper);
        if(sysUser != null) {
            String hashAlgorithmName = "MD5";
            Object credentials = password;
            Object salt = ByteSource.Util.bytes(sysUser.getUsername());
            int hasIterations = 1024;
            Object result = new SimpleHash(hashAlgorithmName,credentials,salt,hasIterations);
            sysUser.setPassword(String.valueOf(result));

            sysUserMapper.updateById(sysUser);

            return sysUser;
        }
        return null;
    }

    /**
     * 通过用户名查找id
     * @param username
     * @return
     */
    @Override
    public SysUser findByName(String username) {
        LambdaQueryWrapper<SysUser> lambda = new QueryWrapper<SysUser>().lambda();
        lambda.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(lambda);
        if(sysUser != null) {
            return sysUser;
        }
        return null;
    }

}
