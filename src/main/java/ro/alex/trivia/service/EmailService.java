package ro.alex.trivia.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender emailSender;
    private final String template;
    private final JavaMailSenderImpl mailSender;

    public EmailService(JavaMailSender emailSender, @Value("${activation.code.email.template}") String template,
                        JavaMailSenderImpl mailSender) {
        this.emailSender = emailSender;
        this.template = template;
        this.mailSender = mailSender;
    }

    public void sendActivationEmail(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setSubject("Trivia Account Activation  - Code");

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("trivia.uab@gmail.com");
            helper.setTo(to);
            helper.setText(template.replace("{{code}}", code), true);

            emailSender.send(message);
        } catch (MessagingException e) {
            logger.error(e.getMessage(), e);
        }
    }
}