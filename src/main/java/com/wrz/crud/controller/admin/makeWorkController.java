package com.wrz.crud.controller.admin;

import com.wrz.crud.entity.SysUser;
import com.wrz.crud.service.ISysUserService;
import com.wrz.crud.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author wrz
 * @data 2020/5/10-15:46
 */
@Controller
@RequestMapping("/admin")
public class makeWorkController {

    @Autowired
    ISysUserService sysUserService;

    /**
     * 跳转到任务管理页面
     */
    @RequestMapping("/makework")
    public String toMakeWord(Model model) {

        List<SysUser> sysUserList = sysUserService.selectAll();
        model.addAttribute("userList",sysUserList);

        return "admin/work/makework";
    }

}
