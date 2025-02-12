package com.example.locaquest.lib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.locaquest.exception.JavaMailException;
import com.example.locaquest.dto.email.EmailProvider;
import com.example.locaquest.component.EmailProviderProperties;

import jakarta.mail.internet.MimeMessage;

@Service
public class JavaMail {

    private final EmailProviderProperties emailProviderProperties;
    private final Map<String, JavaMailSenderImpl> mailSenderMap = new HashMap<>();

    public JavaMail(EmailProviderProperties emailProviderProperties,
            @Value("${spring.mail.username}") String mailUserName,
            @Value("${spring.mail.password}") String mailPassword,
            @Value("${spring.mail.properties.mail.smtp.auth}") String mailAuth,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable}") String mailStarttlsEnable) {
        this.emailProviderProperties = emailProviderProperties;

        List<EmailProvider> mailProviderList = this.emailProviderProperties.getEmailProviders();
        for (EmailProvider mailProvider : mailProviderList) {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(mailProvider.getSmtpHost());
            mailSender.setPort(mailProvider.getSmtpPort());
            mailSender.setUsername(mailUserName);
            mailSender.setPassword(mailPassword);
            this.mailSenderMap.put(mailProvider.getDomain(), mailSender);

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.smtp.auth", mailAuth);
            props.put("mail.smtp.starttls.enable", mailStarttlsEnable);
        }
    }

    public void send(String email, String subject, String htmlContents) {
        String domain = getDomain(email);
        EmailProvider emailProvider = emailProviderProperties.findEmailProviderByDomain(domain);
        if (emailProvider == null) {
            throw new JavaMailException("Email provider not found: " + domain);
        }
        try {
            JavaMailSenderImpl mailSender = this.mailSenderMap.get(domain);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContents, true);
            mailSender.send(message);
        } catch (Exception e) {
            throw new JavaMailException("Failed to send email: " + email, e);
        }
    }
    
    private String getDomain(String email) {
        String[] parts = email.split("@");
        if (parts.length == 2) {
            return parts[1];
        } else {
            throw new JavaMailException("Invalid email address: " + email);
        }
    }
}
