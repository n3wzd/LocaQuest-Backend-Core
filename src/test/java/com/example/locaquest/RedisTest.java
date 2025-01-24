package com.example.locaquest;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.locaquest.service.RedisService;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisService redisService;

    static final private Logger logger = LoggerFactory.getLogger(EmailSenderTest.class);

    /*@ParameterizedTest
    @CsvSource({
        "key1, ABC, ABC, true", 
        "key2, ACD, ABD, false", 
    })
    void testRedis(String dataKey, String in, String out, String result) {
        redisService.save(dataKey, in, 10, TimeUnit.SECONDS);
        String data = redisService.get(dataKey);

        logger.info("get is: {}", data);
        if(data.equals(out)) {
            assertEquals("true", result);
        } else {
            assertEquals("false", result);
        }
    }*/
}
