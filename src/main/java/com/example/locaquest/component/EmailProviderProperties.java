package com.example.locaquest.component;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.example.locaquest.dto.email.EmailProvider;

@ConfigurationProperties(prefix = "email")
@Component
public class EmailProviderProperties {
	private List<EmailProvider> emailProviders;

    public List<EmailProvider> getEmailProviders() {
        return emailProviders;
    }

    public void setEmailProviders(List<EmailProvider> providers) {
        this.emailProviders = providers;
    }
    
    public EmailProvider findEmailProviderByDomain(String domain) {
        Optional<EmailProvider> result = emailProviders.stream()
                .filter(provider -> provider.getDomain().equals(domain))
                .findFirst();
        return result.orElse(null);
    }
}
