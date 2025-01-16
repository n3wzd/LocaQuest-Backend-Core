package com.example.reviewsplash.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;

import com.example.reviewsplash.model.EmailProvider;
import com.example.reviewsplash.repogitory.EmailProviderRepository;
import com.example.reviewsplash.exception.ServiceException;

@Service
public class EmailSender {

    private final EmailProviderRepository emailProviderRepository;
    private final Map<String, JavaMailSenderImpl> mailSenderMap = new HashMap<>();

    public EmailSender(EmailProviderRepository emailProviderRepository,
            @Value("${spring.mail.username}") String mailUserName,
            @Value("${spring.mail.password}") String mailPassword,
            @Value("${spring.mail.properties.mail.smtp.auth}") String mailAuth,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable}") String mailStarttlsEnable) {
        this.emailProviderRepository = emailProviderRepository;

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

    public void sendEmail(String userEmail, String title, String contents) {
        String domain = getDomain(userEmail);
        EmailProvider emailProvider = (EmailProvider)emailProviderRepository.findByDomain(domain);
        if (emailProvider == null) {
            throw new ServiceException("Email provider not found");
        }

        try {
            JavaMailSenderImpl mailSender = this.mailSenderMap.get(domain);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(userEmail);
            helper.setSubject(title);
            helper.setText(contents, false);
            mailSender.send(message);
        } catch (MailSendException | MessagingException | MailAuthenticationException e) {
            throw new ServiceException("Failed to send email", e);
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
}
