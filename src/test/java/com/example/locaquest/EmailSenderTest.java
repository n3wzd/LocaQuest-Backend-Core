/*package com.example.locaquest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.locaquest.service.EmailSender;
import com.example.locaquest.exception.ServiceException;

@SpringBootTest
public class EmailSenderTest {

    @Autowired
    private EmailSender emailSender;

    static final private Logger logger = LoggerFactory.getLogger(EmailSenderTest.class);

    @ParameterizedTest
    @CsvSource({
        "test@gmail.com, Test Mail, This is Test, true", 
        "alice@notExistsMail.com, Test Mail, This is Test, false", 
    })
    void testEmailSender(String userEmail, String title, String contents, String result) {
        try {
            emailSender.sendEmail(userEmail, title, contents);
            logger.info("Response: {} - Success!", userEmail);
            assertEquals("true", result);
        } catch (ServiceException e) {
            logger.info("Response: {} - Failed!: {}", userEmail, e);
            assertEquals("false", result);
        }
    }
}*/
