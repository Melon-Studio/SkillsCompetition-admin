package top.dooper.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;


public interface IEmailService {
    public void sendSimpleMessage(String to, String title, String content);
    public void sendVerifyCode(String to, String code) throws MessagingException, UnsupportedEncodingException;
    public void sendMessage(String to, String score, String rank) throws MessagingException, UnsupportedEncodingException;
}
