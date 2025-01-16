package com.example.reviewsplash.repogitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.reviewsplash.model.EmailProvider;

@Repository
public interface EmailProviderRepository extends JpaRepository<EmailProvider, String> {
    EmailProvider findByDomain(String domain);
}
