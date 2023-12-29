package top.dooper.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.dooper.service.IEmailService;

import java.net.InetAddress;

@Component
public class ScheduledTask {
    private final IEmailService emailService;

    private String lastKnownIP = null;

    public ScheduledTask(IEmailService emailService) {
        this.emailService = emailService;
    }

//    @Scheduled(fixedRate = 600000)
//    @Scheduled(fixedRate = 10000)
    public void checkAndSendIP() {
        String currentIP = getCurrentIP();

        if (!currentIP.equals(lastKnownIP)) {
            String subject = "服务器IP变动通知";
            String message = "SkillsCompetition 检测到服务器IP变动，以下为新的服务器IP地址："+currentIP;
            try {
                emailService.sendSimpleMessage("xiaofan6@foxmail.com", subject, message);
            }catch (Exception ex) {
                ex.printStackTrace();
            }


            lastKnownIP = currentIP;
        }
    }

    public String getCurrentIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
