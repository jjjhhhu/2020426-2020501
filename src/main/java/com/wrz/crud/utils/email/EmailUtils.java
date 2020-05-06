package com.wrz.crud.utils.email;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @ClassName: EmailUtils
 * @author: zyx
 * @E-mail: 1377631190@qq.com
 * @DATE: 2019/7/27 12:19
 */
@Slf4j
@Component
public class EmailUtils {

    /**
     * 获取不到配置文件中的 mail.username
     */
    @Value("${spring.mail.username}")
    private String form;

    private JavaMailSender sender;

    public String getForm() {
        return form;
    }

    @Autowired
    public EmailUtils(JavaMailSender sender) {this.sender = sender;}

    /**
     * 发送注册确定邮件
     * @param to 接收方
     * @param title 邮件标题
     * @param html 邮件内容
     */
    public void send(String to, String title, String html) {
        MimeMessage mimeMessage = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(form);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(html, true);
            sender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("error {}", e);
            log.error("邮件发送失败 {}", e.getMessage());
        }
    }

    @Async
    public void regEmail(String to,String titleType ,String name, String content,String date) {
        String html = regTemplate.replace("${name}", name).replace("${content}", content).replace("${date}", date);
        send(to, titleType, html);
    }

    private final String regTemplate = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <meta name=\"viewport\"\n" +
            "        content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
            "  <title>管理员悄悄话</title>\n" +
            "  <style>\n" +
            "    @media screen and (min-width: 800px) {\n" +
            "      .box {\n" +
            "        width: 360px;\n" +
            "      }\n" +
            "    }\n" +
            "  </style>\n" +
            "</head>\n" +
            "<body style=\"margin: 0;\">\n" +
            "<div class=\"box\" style=\"margin: 0 auto; border: 1px solid #3d484c;\">\n" +
            "  <nav style=\"background-color: #3d484c;height: 45px;\">\n" +
            "    <div style=\"float: right;color: #ffffff;padding: 12px\">创客实验室</div>\n" +
            "  </nav>\n" +
            "  <div style=\"padding: 0 8px;\">\n" +
            "    <h3 style=\"margin: 15px 2px;\">\n" +
            "     发送给： <span style=\"color: #31bdeb;\">${name}</span>\n" +
            "    </h3>\n" +
            "    <p style=\"margin: 5px 0; font-size: 24px;color: #2389bb;\">${content}</p>\n" +
            "    <p style=\"margin: 15px 2px;\">\n" +
            "    <p style=\"margin: 5px 0; font-size: 13px;\">发送时间：${date}</p>\n" +
//            "      <a href=\"${link}\" style=\"color: #31bdeb;\">${link}</a>\n" +
            "    </p>\n" +
            "    <div style=\"font-size: 12px; color: #707070;\">\n" +
//            "      <p style=\"margin: 5px 0;\">如果以上链接无法访问，请将该网址复制并粘贴至新的浏览器窗口中，该链接仅限本次操作。</p>\n" +
//            "      <p style=\"margin: 5px 0;\">如果并非本人注册，您无需执行任何操作来取消账号！此账号将不会启动。</p>\n" +
//            "      <p style=\"margin: 5px 0;\">本链接将在5分钟后失效！</p>\n" +
            "      <p style=\"font-size: 10px; text-align: right;\">此邮件为系统邮件，请勿直接回复。</p>\n" +
            "    </div>\n" +
            "    <h4 style=\"margin: 15px 2px;\">创客实验室</h4>\n" +
            "  </div>\n" +
            "  <footer style=\"height: 5px;background-color: #3d484c;\"></footer>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>";
}
