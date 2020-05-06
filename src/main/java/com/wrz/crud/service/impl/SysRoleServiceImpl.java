package com.wrz.crud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wrz.crud.entity.SysRole;
import com.wrz.crud.entity.SysUserRole;
import com.wrz.crud.mapper.SysRoleMapper;
import com.wrz.crud.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;


    @Override
    public SysRole findByRoleId(Integer roleId) {
        LambdaQueryWrapper<SysRole> lambda = new QueryWrapper<SysRole>().lambda();
        lambda.eq(SysRole::getId, roleId);
        SysRole sysRole = sysRoleMapper.selectOne(lambda);
        if(sysRole != null) {
            return sysRole;
        }
        return null;
    }
}
