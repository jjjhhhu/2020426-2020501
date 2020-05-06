package com.wrz.crud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wrz.crud.entity.SysRole;
import com.wrz.crud.entity.SysUser;
import com.wrz.crud.entity.SysUserRole;
import com.wrz.crud.mapper.SysUserMapper;
import com.wrz.crud.mapper.SysUserRoleMapper;
import com.wrz.crud.service.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wrz
 * @since 2020-04-28
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUserRole findByUserId(Integer id) {
        LambdaQueryWrapper<SysUserRole> lambda = new QueryWrapper<SysUserRole>().lambda();
        lambda.eq(SysUserRole::getUserId, id);
        SysUserRole sysUserRole = sysUserRoleMapper.selectOne(lambda);
        if(sysUserRole != null) {
            return sysUserRole;
        }
        return null;
    }


    @Override
    public boolean findByUserNameAndInsert(String username) {
        LambdaQueryWrapper<SysUser> lambda = new QueryWrapper<SysUser>().lambda();
        lambda.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(lambda);
        if(sysUser != null) {
            SysUserRole sysUserRole = new SysUserRole(sysUser.getId(),1);
            int insert = sysUserRoleMapper.insert(sysUserRole);
            if(insert != 0) {
                return true;
            }
        }
        return false;
    }
}
