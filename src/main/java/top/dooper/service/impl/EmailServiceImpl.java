package top.dooper.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.dooper.service.IEmailService;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements IEmailService {
    private final JavaMailSender javaMailSender;

    @Value("${WebApplication.name}")
    private String WebName;
    @Value("${WebApplication.url}")
    private String WebUrl;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    @Override
    public void sendSimpleMessage(String to, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(title);
        message.setText(content);
        javaMailSender.send(message);
    }

    @Async
    @Override
    public void sendVerifyCode(String to, String code) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(new InternetAddress(fromEmail, WebName, "UTF-8"));
        helper.setTo(to);
        helper.setSubject("请查收你的邮箱验证码");
        helper.setText("<table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr><td><div style=\"background:#fff\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><thead><tr><td valign=\"middle\" style=\"padding-left:30px;background-color:#415A94;color:#fff;padding:20px 40px;font-size: 21px;\">"+WebName+"</td></tr></thead><tbody><tr style=\"padding:40px 40px 0 40px;display:table-cell\"><td style=\"font-size:24px;line-height:1.5;color:#000;margin-top:40px\">邮箱验证码</td></tr><tr><td style=\"font-size:14px;color:#333;padding:24px 40px 0 40px\"><p>尊敬的用户你好！</p><p>你的验证码是：</p><p style=\"border:1px dashed #000000; padding: 0 10px 0 10px; border-radius: 3px; font-weight: 400; font-size: 40px; margin: 10px 0 10px 0; width: fit-content;\">"+code+"</p><p>验证码 5 分钟内有效，请及时进行验证。</p><p>若此邮件不是您请求的，请忽略并删除！</p><p>请注意：本邮件为系统自动发送，请勿回复。</p><p>如有疑问请联系网站管理员。</p></td></tr><tr style=\"padding:40px;display:table-cell\"></tr></tbody></table></div><div><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr><td style=\"padding:20px 40px;font-size:12px;color:#999;line-height:20px;background:#f7f7f7\"><a href=\""+WebUrl+"\" style=\"font-size:14px;color:#929292\" target=\"_blank\" rel=\"noopener\">返回"+WebName+"</a></td></tr></tbody></table></div></td></tr></tbody></table>");
        javaMailSender.send(message);
    }

    @Override
    public void sendMessage(String to, String score, String rank) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(new InternetAddress(fromEmail, WebName, "UTF-8"));
        helper.setTo(to);
        helper.setSubject("比赛成绩公布通知");
        helper.setText("<div style=\"background: #eee\"><table width=\"600\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"><tbody><tr><td><div style=\"background:#fff\"><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><thead><tr><td valign=\"middle\" style=\"padding-left:30px;background-color:#415A94;color:#fff;padding:20px 40px;font-size: 21px;\">"+WebName+"</td></tr></thead><tbody><tr style=\"padding:40px 40px 0 40px;display:table-cell\"><td style=\"font-size:24px;line-height:1.5;color:#000;margin-top:40px; font-weight: 900;\">成绩公布</td></tr><tr><td style=\"font-size:14px;color:#333;padding:24px 40px 0 40px\"><p>尊敬的参赛选手，你好！</p><p>大赛成绩已公布，你的成绩是：<span style=\"font-size: 16px; font-weight: 800;\">"+score+"</span>，你的本次大赛排名是：<span style=\"font-size: 16px; font-weight: 800;\">"+rank+"</span></p><p>比赛成绩和将于近期在学院钉钉群公布，奖品和证书的发放通知将于近期在学院钉钉群公布，请耐心等待！</p><p>如果你对比赛结果有任何疑问，请联系大赛组委会，地址：信息技术学院(鼎新楼)5楼510办公室</p></td></tr><tr style=\"padding:40px;display:table-cell\"></tr></tbody></table></div><div><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tbody><tr><td style=\"padding:20px 40px;font-size:12px;color:#999;line-height:20px;background:#f7f7f7\"><a href=\"${WebUrl}\" style=\"font-size:14px;color:#929292\" target=\"_blank\" rel=\"noopener\">返回"+WebUrl+"</a></td></tr></tbody></table></div></td></tr></tbody></table></div>");
        javaMailSender.send(message);
    }
}
