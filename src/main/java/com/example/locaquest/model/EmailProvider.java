package com.example.locaquest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_providers")
public class EmailProvider {

    @Id
    @Column(name = "domain", nullable = false, unique = true)
    private String domain;

    @Column(name = "provider_name", nullable = false)
    private String name;

    @Column(name = "smtp_host", nullable = false)
    private String host;

    @Column(name = "smtp_port", nullable = false)
    private int port;
    
    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
