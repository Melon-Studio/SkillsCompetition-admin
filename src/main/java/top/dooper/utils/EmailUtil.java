package top.dooper.utils;

import top.dooper.service.IEmailService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
public class EmailUtil {
    IEmailService emailService;
    public EmailUtil(IEmailService emailService){
        this.emailService = emailService;
    }
    public String codeGenerator(){
        String code = "";
        for (int i = 0; i < 6; i++){
            code +=  new Random().nextInt(10) + "";
        }
        return code;
    }
    public String sendCaptchaMail(String email) throws MessagingException, UnsupportedEncodingException {
        String code = codeGenerator();
        emailService.sendVerifyCode(email, code);
        return code;
    }

    public void sendMessage(String email, String score, String rank) throws MessagingException, UnsupportedEncodingException {
        emailService.sendMessage(email, score, rank);
    }
}
