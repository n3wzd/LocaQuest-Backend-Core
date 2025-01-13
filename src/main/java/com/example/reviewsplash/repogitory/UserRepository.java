package com.example.reviewsplash.repogitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.reviewsplash.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // 추가적인 쿼리 메서드 정의 가능
    User findByEmail(String email);
}
