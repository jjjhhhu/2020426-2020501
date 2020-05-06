package com.wrz.crud.service;

import com.wrz.crud.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wrz
 * @since 2020-04-28
 */
public interface ISysRoleService extends IService<SysRole> {

    SysRole findByRoleId(Integer roleId);
}
