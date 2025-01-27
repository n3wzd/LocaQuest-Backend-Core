package com.example.locaquest;

import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import jakarta.transaction.Transactional;

import com.example.locaquest.model.UserStatistic;
import com.example.locaquest.model.Achievement;
import com.example.locaquest.service.AchievementService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
// @Transactional
public class AchievementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AchievementService achievementService;

    final private ObjectMapper objectMapper = new ObjectMapper();
    static final private Logger logger = LoggerFactory.getLogger(AchievementControllerTest.class);

    @ParameterizedTest
    @CsvSource({
        "12", 
    })
    void testFind(int userId) throws Exception {
        List<Achievement> res = achievementService.getUserAchievementList(userId);
        logger.info("testFind: {}, {}", userId, res.toString());
    }

    @ParameterizedTest
    @CsvSource({
        "12, 5000, 15000, 200000",
    })
    void testScan(int userId, int exp, int steps, int dist) throws Exception {
        UserStatistic userStatistic = new UserStatistic();
        userStatistic.setUserId(userId);
        userStatistic.setTotalExperience(exp);
        userStatistic.setTotalSteps(steps);
        userStatistic.setTotalDistance(dist);

        achievementService.updateUserAchievementByUserStatistic(userStatistic);
        logger.info("testScan: {}", userId);
    }
}
