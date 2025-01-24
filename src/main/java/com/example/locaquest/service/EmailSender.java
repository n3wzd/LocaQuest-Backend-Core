package com.example.locaquest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.locaquest.exception.ServiceException;
import com.example.locaquest.model.EmailProvider;
import com.example.locaquest.repogitory.EmailProviderRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSender {

    private final EmailProviderRepository emailProviderRepository;
    private final SpringTemplateEngine templateEngine;
    private final Map<String, JavaMailSenderImpl> mailSenderMap = new HashMap<>();

    public EmailSender(EmailProviderRepository emailProviderRepository, SpringTemplateEngine templateEngine,
            @Value("${spring.mail.username}") String mailUserName,
            @Value("${spring.mail.password}") String mailPassword,
            @Value("${spring.mail.properties.mail.smtp.auth}") String mailAuth,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable}") String mailStarttlsEnable) {
        this.emailProviderRepository = emailProviderRepository;
        this.templateEngine = templateEngine;

        List<EmailProvider> mailProviderList = this.emailProviderRepository.findAll();
        for (EmailProvider mailProvider : mailProviderList) {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(mailProvider.getHost());
            mailSender.setPort(mailProvider.getPort());
            mailSender.setUsername(mailUserName);
            mailSender.setPassword(mailPassword);
            this.mailSenderMap.put(mailProvider.getDomain(), mailSender);

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.smtp.auth", mailAuth);
            props.put("mail.smtp.starttls.enable", mailStarttlsEnable);
        }
    }

    private String getDomain(String email) {
        String[] parts = email.split("@");
        if (parts.length == 2) {
            return parts[1];
        } else {
            throw new ServiceException("Invalid email address");
        }
    }

    private void sendEmail(String email, String subject, String htmlContents) {
        String domain = getDomain(email);
        EmailProvider emailProvider = (EmailProvider)emailProviderRepository.findByDomain(domain);
        if (emailProvider == null) {
            throw new ServiceException("Email provider not found");
        }

        try {
            JavaMailSenderImpl mailSender = this.mailSenderMap.get(domain);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContents, true);
            mailSender.send(message);
        } catch (MailSendException | MessagingException | MailAuthenticationException e) {
            throw new ServiceException("Failed to send email: ", e);
        }
    }

    public void sendAuthMail(String email, String linkUrl) {
        Context context = new Context();
        context.setVariable("linkUrl", linkUrl);

        String htmlContents = templateEngine.process("EmailVerification", context);
        sendEmail(email, "Email Verification", htmlContents); 
    }
}
