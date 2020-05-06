package com.wrz.crud.service;

import com.wrz.crud.entity.SysUser;
import com.wrz.crud.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wrz
 * @since 2020-04-28
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    SysUserRole findByUserId(Integer id);

    boolean findByUserNameAndInsert(String username);
}
