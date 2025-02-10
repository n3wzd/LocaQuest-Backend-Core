package com.example.locaquest.repogitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.locaquest.model.UserStatistic;

@Repository
public interface UserStatisticRepository extends JpaRepository<UserStatistic, String> {
    UserStatistic findByUserId(int userId);

    @Modifying
    @Query("UPDATE UserStatistic u SET u.exp = u.exp + :exp, u.steps = u.steps + :steps, u.distance = u.distance + :distance WHERE u.userId = :userId")
    int gainParam(@Param("userId") int userId, @Param("exp") int exp, @Param("steps") int steps, @Param("distance") int distance);
}
