package com.example.reviewsplash.repogitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.reviewsplash.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    User findByUserId(String userId);
    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
}
