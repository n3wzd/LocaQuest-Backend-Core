package com.example.locaquest.lib;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisTest {

    @Autowired
    private Redis redis;

    static final private Logger logger = LoggerFactory.getLogger(RedisTest.class);

    @ParameterizedTest
    @CsvSource({
        "key1, ABC, ABC, true", 
        "key2, ACD, ABD, false", 
    })
    void testRedis(String dataKey, String in, String out, String result) {
    	redis.save(dataKey, in, 10, TimeUnit.SECONDS);
        String data = redis.get(dataKey, String.class);

        logger.info("get is: {}", data);
        if(data.equals(out)) {
            assertEquals("true", result);
        } else {
            assertEquals("false", result);
        }
    }
}
