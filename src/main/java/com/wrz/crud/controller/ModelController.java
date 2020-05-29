package com.wrz.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wrz
 * @data 2020/5/29-17:56
 */
@Controller
@RequestMapping("/model")
public class ModelController {


    @RequestMapping("/")
    public String toIndex() {
        return "model/view/index";
    }

}
