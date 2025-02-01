package com.example.locaquest.repogitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.locaquest.model.UserStatistic;

@Repository
public interface UserStatisticRepository extends JpaRepository<UserStatistic, String> {
    UserStatistic findByUserId(int userId);
}
