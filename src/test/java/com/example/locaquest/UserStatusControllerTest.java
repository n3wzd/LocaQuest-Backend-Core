package com.example.locaquest;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.dto.status.AchievementData;
import com.example.locaquest.service.UserStatusService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserStatusControllerTest {
    @Autowired
    private UserStatusService userStatusService;

    static final private Logger logger = LoggerFactory.getLogger(UserStatusControllerTest.class);

    @ParameterizedTest
    @CsvSource({
        "13", 
    })
    void testFindAll(int userId) throws Exception {
        List<AchievementData> res = userStatusService.getAchievedUserAchievements(userId);
        logger.info("testFindAll: {}, {}", userId, res.toString());
    }

    @ParameterizedTest
    @CsvSource({
        "13, 5000, 15000, 200000",
    })
    void testScan(int userId, int exp, int steps, int dist) throws Exception {
        UserStatistic userStatistic = new UserStatistic();
        userStatistic.setExp(exp);
        userStatistic.setSteps(steps);
        userStatistic.setDistance(dist);

        userStatusService.updateUserAchievementByUserStatistic(userId, userStatistic);
        logger.info("testScan: {}", userId);
    }
}
