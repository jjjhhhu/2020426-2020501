package com.wrz.crud.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wrz
 * @data 2020/4/28-19:16
 */
@Controller
@RequestMapping("/view")
public class ViewController {


    // 前往index页面
    @RequestMapping("/index")
    public String IndexPage() {
        return "view/index";
    }


    /**
     * 跳转word文档上传页面
     */
    @RequestMapping("/fileUpload")
    public String toFileUpload() {

        return "view/fileUpload";
    }


}
