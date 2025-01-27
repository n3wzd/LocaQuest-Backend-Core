package com.example.locaquest.repogitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.locaquest.model.Achievement;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, String> {
    
}
