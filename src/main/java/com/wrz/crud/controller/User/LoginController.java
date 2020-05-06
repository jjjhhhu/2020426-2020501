package com.wrz.crud.controller.User;

import com.wrz.crud.entity.SysUser;
import com.wrz.crud.utils.Msg;
import com.wrz.crud.controller.VerifyCode.VerifyCodeController;
import com.wrz.crud.service.ISysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author wrz
 * @data 2020/4/26-22:23
 */
@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    ISysUserService iSysUserService;


    @RequestMapping("/doLogin")
    @ResponseBody
    public Msg DoLogin(@RequestParam("username") String name,
                       @RequestParam("password") String password,
                       @RequestParam("vercode") String verifycode,
                       HttpServletRequest request) {

        // 验证码 检测
        HttpSession session = request.getSession();
        String sessionCode = (String) session.getAttribute(VerifyCodeController.SESSION_KEY);
        if (null == sessionCode) {
            return Msg.fail().add("mc_msg", "验证码失效");
        }
        if (!sessionCode.equalsIgnoreCase(verifycode)) {
            return Msg.fail().add("mc_msg", "验证码错误");
        }
        session.removeAttribute(VerifyCodeController.SESSION_KEY);


        // 检测 登陆信息
        Boolean b = false;

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name,password);
        try {
            subject.login(token);
            session.setAttribute("user", SecurityUtils.getSubject().getPrincipal());
            //登陆成功
            return Msg.success();
        } catch (UnknownAccountException e) {
            return Msg.fail().add("mc_msg","用户名不存在");
        } catch (IncorrectCredentialsException e) {
            return Msg.fail().add("mc_msg","用户名或密码错误");
        } catch (Exception e){
            return Msg.fail().add("mc_msg","出现未知错误!");
        }

    }

    // 跳转login页面
    @RequestMapping("/")
    public String LoginPage() {
        return "login";
    }

    @RequestMapping("/regPage")
    public String RegPage() {
        return "reg";
    }

    @RequestMapping("/forgetPage")
    public String ForgetPage() {
        return "forget";
    }

}
