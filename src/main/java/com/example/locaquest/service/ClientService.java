package com.example.locaquest.service;

import org.springframework.stereotype.Service;

import com.example.locaquest.exception.ServiceException;
import com.example.locaquest.util.Crypto;

@Service
public class ClientService {
    public String getRSAPublicKey() {
    	try {
    		return Crypto.getPublicKey();
        } catch(Exception e) {
            throw new ServiceException("RSM Error:" + e.toString());
        }
    }
}
