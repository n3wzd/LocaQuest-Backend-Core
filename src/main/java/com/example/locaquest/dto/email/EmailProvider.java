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
}
