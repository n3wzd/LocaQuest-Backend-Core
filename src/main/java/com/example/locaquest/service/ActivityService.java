package com.example.locaquest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.locaquest.util.Crypto;
import com.example.locaquest.exception.ServiceException;

@Service
public class ActivityService {

    @Value("${jwt.key.login}")
    private String jwtKeyLogin;

    public String getLoginTokenKey(String publicKey) {
        try {
            return Crypto.encryptRSA(jwtKeyLogin, Crypto.stringToPublicKey(publicKey));
        } catch(Exception e) {
            throw new ServiceException("RSM Error:" + e.toString());
        }
    }
}
