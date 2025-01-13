package com.example.reviewsplash.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reviewsplash.model.User;
import com.example.reviewsplash.repogitory.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // 이메일 중복 확인
        /*if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }*/

        // 비밀번호 해싱 (예: BCrypt)
        // String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        // user.setPassword(hashedPassword);

        return userRepository.save(user);
    }
}
