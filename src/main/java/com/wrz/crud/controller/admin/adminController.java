package com.wrz.crud.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wrz
 * @data 2020/5/3-16:04
 */
@Controller
@RequestMapping("/admin")
public class adminController {


    /**
     * 跳转管理员后台文档上传页面
     */
    @RequestMapping("/console")
    public String toConsole() {

        return "admin/console";
    }

    /**
     * 跳转管理员后台文档上传页面
     */
    @RequestMapping("/console/ifream")
    public String toControllerIfream() {

        return "admin/console/consolePage";
    }

    /**
     * 跳转到任务管理页面
     */
    @RequestMapping("/makeword")
    public String toMakeWord() {

        return "admin/work/makework";
    }

}
