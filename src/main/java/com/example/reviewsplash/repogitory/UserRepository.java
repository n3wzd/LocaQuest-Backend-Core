package com.example.reviewsplash.repogitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.reviewsplash.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    User findByUserId(String userId);
    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
    int deleteByUserId(String userId);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.userId = :userId")
    int updatePassword(@Param("password") String password, @Param("userId") String userId);
}
