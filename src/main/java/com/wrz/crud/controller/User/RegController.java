package com.wrz.crud.controller.User;

import com.wrz.crud.controller.VerifyCode.VerifyCodeController;
import com.wrz.crud.entity.SysUser;
import com.wrz.crud.service.ISysUserRoleService;
import com.wrz.crud.service.ISysUserService;
import com.wrz.crud.utils.Msg;
import com.wrz.crud.utils.email.EmailUtils;
import com.wrz.crud.utils.random.RandomSelf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wrz
 * @data 2020/4/28-14:54
 */
@Controller
@RequestMapping("/user")
public class RegController {

    public static final String SESSION_KEY = "PASS_CODE";

    @Autowired
    ISysUserService iSysUserService;

    @Autowired
    ISysUserRoleService iSysUserRoleService;

    @Autowired
    private JavaMailSender sender;

    @Autowired
    EmailUtils emailUtils;

    @Autowired
    RandomSelf randomSelf;

    /**
     * 通过手机号码，发送验证码至邮箱
     */
    @RequestMapping("/userForget")
    @ResponseBody
    public Msg DoForget(@RequestParam("cellphone") String phoneNum,
                        HttpServletRequest request) {


        // 通过手机号码找到邮箱 并且向邮箱发送一条邮件
        SysUser sysUser = iSysUserService.findByPhoneNum(phoneNum);

        // 向对方邮箱发送一条信息，获取验证码 进行 比对
        // 验证码为随机字符串，并且放入session中用于 检测

        String emailCode = randomSelf.getCode(6);
        HttpSession session = request.getSession();
        session.removeAttribute(SESSION_KEY);
        session.setAttribute(SESSION_KEY, emailCode);

        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dayTime = df.format(date);

        emailUtils.regEmail(sysUser.getEmail(),"找回密码", sysUser.getUsername(),emailCode,dayTime);

        return Msg.success().add("msg","邮件发送成功");
    }

    /**
     *  修改密码
     * @param request
     * @param password
     * @param email
     * @param phoneNum
     * @param emailCode
     * @return
     */
    @RequestMapping("/doForget")
    @ResponseBody
    public Msg doForget(HttpServletRequest request,
                        @RequestParam("password") String password,
                        @RequestParam("email") String email,
                        @RequestParam("cellphone") String phoneNum,
                        @RequestParam("vercode") String emailCode) {

        // 验证码 检测
        HttpSession session = request.getSession();
        String sessionCode = (String) session.getAttribute(RegController.SESSION_KEY);
        if (null == sessionCode) {
            return Msg.fail().add("msg", "邮箱发送失败");
        }
        if (!sessionCode.equalsIgnoreCase(emailCode)) {
            return Msg.fail().add("msg", "验证码错误");
        }
        session.removeAttribute(RegController.SESSION_KEY);

        // 检测成功后 将执行修改方法
        SysUser sysUser = iSysUserService.changePassword(email,password);

        if(sysUser != null){
            return Msg.success();
        }

        return Msg.fail().add("msg","发生错误");
    }


    /**
     * 注册方法
     */
    @RequestMapping("/userReg")
    @ResponseBody
    public Msg doReg(@RequestParam("cellphone") String phoneNum,
                     @RequestParam("username") String username,
                     @RequestParam("password") String password,
                     @RequestParam("email") String email,
                     @RequestParam("code") String verifycode,
                     HttpServletRequest request) {

        System.out.println(" ajax -> " + this.getClass());

        System.out.println("输入的验证码 -> "+verifycode);
        // 验证码 检测
        HttpSession session = request.getSession();
        String sessionCode = (String) session.getAttribute(VerifyCodeController.SESSION_KEY);
        System.out.println("正确的验证码 -> " + sessionCode);
        if (null == sessionCode) {
            return Msg.fail().add("mc_msg", "验证码失效");
        }
        if (!sessionCode.equalsIgnoreCase(verifycode)) {
            return Msg.fail().add("mc_msg", "验证码错误");
        }
        session.removeAttribute(VerifyCodeController.SESSION_KEY);

        // 检测 用户名是否重复 邮箱是否重复 电话是否重复
        SysUser byName = iSysUserService.findByName(username);
        if(byName != null) {
            return Msg.fail().add("mc_msg","该用户名已经被注册!");
        }
        SysUser byEmail = iSysUserService.findByEmail(email);
        if(byEmail != null) {
            return Msg.fail().add("mc_msg","该邮箱已经被注册!");
        }
        SysUser byPhoneNum = iSysUserService.findByPhoneNum(phoneNum);
        if(byPhoneNum != null) {
            return Msg.fail().add("mc_msg","该电话号码已经被注册!");
        }
        SysUser sysUser = new SysUser(username,password,phoneNum,email);
        // 将用户信息保存进入数据库
        boolean b = iSysUserService.regUser(sysUser);

        if(b) {
            // 如果加入成功，那么将通过 username 查出 id 再将 sys_user_role 表赋值
            boolean byUserNameAndInsert = iSysUserRoleService.findByUserNameAndInsert(username);

            if(byUserNameAndInsert){
                return Msg.success();
            }

        }
        return Msg.fail().add("mc_msg","发生未知错误!");
    }

}
