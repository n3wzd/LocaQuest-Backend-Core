package com.example.locaquest.constant;

import java.util.Arrays;
import java.util.List;

import com.example.locaquest.dto.email.EmailProvider;

public class EmailConfig {

    public static final EmailProvider GMAIL = new EmailProvider("gmail.com", "Gmail", "smtp.gmail.com", 587);
    public static final EmailProvider OUTLOOK = new EmailProvider("outlook.com", "Outlook", "smtp-mail.outlook.com", 587);
    public static final EmailProvider YAHOO = new EmailProvider("yahoo.com", "Yahoo", "smtp.mail.yahoo.com", 587);
    public static final EmailProvider NAVER = new EmailProvider("naver.com", "Naver", "smtp.naver.com", 587);

    public static final List<EmailProvider> EMAIL_PROVIDERS = Arrays.asList(GMAIL, OUTLOOK, YAHOO, NAVER);
    
    public static EmailProvider findEmailProviderByDomain(String domain) {
        for (EmailProvider provider : EMAIL_PROVIDERS) {
            if (provider.getDomain().equalsIgnoreCase(domain)) {
                return provider;
            }
        }
        return null;
    }
}
