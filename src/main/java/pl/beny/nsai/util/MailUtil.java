package pl.beny.nsai.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

    private static String url;
    private static JavaMailSender mailSender;
    private static MessageSource messageSource;

    @Autowired
    private MailUtil(JavaMailSender mailSender, MessageSource messageSource, @Value("${registration.url:http://localhost:8081/register/activate?token=}") String url) {
        MailUtil.mailSender = mailSender;
        MailUtil.messageSource = messageSource;
        MailUtil.url = url;
    }

    public static void sendActivationEmail(String email, String token) {
        String URL = url + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(messageSource.getMessage("registration.email.subject", new Object[]{URL}, LocaleContextHolder.getLocale()));
        message.setText(messageSource.getMessage("registration.email.activation", new Object[]{URL}, LocaleContextHolder.getLocale()));
        mailSender.send(message);
    }

}
