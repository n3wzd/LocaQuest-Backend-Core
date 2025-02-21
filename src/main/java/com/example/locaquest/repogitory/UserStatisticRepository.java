package com.example.locaquest.repogitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.model.UserStatisticKey;

@Repository
public interface UserStatisticRepository extends JpaRepository<UserStatistic, String> {
	boolean existsById(UserStatisticKey id);
	List<UserStatistic> findByIdUserId(int userId);

    @Modifying
    @Query("UPDATE UserStatistic u SET u.exp = u.exp + :exp, u.steps = u.steps + :steps, u.distance = u.distance + :distance WHERE u.id.userId = :userId and u.id.statDate = :statDate")
    int gainParam(@Param("userId") int userId, @Param("statDate") String statDate, @Param("exp") int exp, @Param("steps") int steps, @Param("distance") int distance);
}
