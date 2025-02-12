package com.example.locaquest.dto.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailProvider {
    private String domain;
    private String providerName;
    private String smtpHost;
    private int smtpPort;
    
    public EmailProvider(String domain, String providerName, String smtpHost, int smtpPort) {
        this.domain = domain;
        this.providerName = providerName;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }
}
