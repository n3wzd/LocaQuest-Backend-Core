package com.example.locaquest.service;

import javax.crypto.Cipher;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.locaquest.exception.ServiceException;

@Service
public class ActivityService {

    @Value("${jwt.key.login}")
    private String jwtKeyLogin;

    public ActivityService() {
        
    }

    public String getLoginTokenKey(String publicKey) {
        try {
            return encryptRSA(jwtKeyLogin, stringToPublicKey(publicKey));
        } catch(Exception e) {
            throw new ServiceException("RSM Error:" + e.toString());
        }
    }

    private PublicKey stringToPublicKey(String publicKey) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    private String encryptRSA(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}
