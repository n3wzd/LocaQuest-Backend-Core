package com.example.locaquest.repogitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.locaquest.model.UserAchievement;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, String> {
    List<UserAchievement> findByIdUserId(int userId);
}
