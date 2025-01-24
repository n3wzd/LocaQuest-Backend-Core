package com.example.locaquest.repogitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.locaquest.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    User findByUserId(int userId);
    boolean existsByEmail(String email);
    boolean existsByUserId(int userId);
    int deleteByUserId(int userId);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    int updatePassword(@Param("password") String password, @Param("email") String email);
}
