package com.wrz.crud.service;

import com.wrz.crud.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wrz
 * @since 2020-04-28
 */
public interface ISysUserService extends IService<SysUser> {

    SysUser findByName(String username);

    boolean regUser(SysUser sysUser);

    SysUser findByPhoneNum(String phoneNum);

    SysUser changePassword(String email,String password);

    SysUser findByEmail(String email);

    List<SysUser> selectAll();
}
