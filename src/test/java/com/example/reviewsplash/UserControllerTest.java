package com.example.reviewsplash;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import jakarta.transaction.Transactional;

import com.example.reviewsplash.dto.LoginRequest;
import com.example.reviewsplash.dto.AuthEmailRequest;
import com.example.reviewsplash.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
// @Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    final private ObjectMapper objectMapper = new ObjectMapper();
    static final private Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    /*@ParameterizedTest
    @Order(1)
    @CsvSource({
        "Alice, Password@123, Alice Smith, alice@example.com, 201", 
        "Bob, Secret@Pass1, Bob Johnson, bob@example.com, 201", 
        "Power, Secret, Power!, testexample, 400", 
    })
    void testCreateUser(String userId, String password, String name, String email, int expectedStatus) throws Exception {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);

        String json = objectMapper.writeValueAsString(user);
        MvcResult result = mockMvc.perform(post("/api/users/register")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testCreateUser: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(2)
    @CsvSource({
        "Alice, Password@123, 200", 
        "Tom, asdfqwer, 401", 
        "Bob, Secret@Pass2, 401", 
        "Bob, Secret@Pass1, 200", 
    })
    void testLoginUser(String userId, String password, int expectedStatus) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId(userId);
        loginRequest.setPassword(password);

        String json = objectMapper.writeValueAsString(loginRequest);
        MvcResult result = mockMvc.perform(post("/api/users/login")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testLoginUser: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(3)
    @CsvSource({
        "testman@notExistsCom, 400",
        "availMail@123gmail.com, 202",
    })
    void testFindUserId(String email, int expectedStatus) throws Exception {
        MvcResult result = mockMvc.perform(get("/api/users/find-id")
                .param("email", email))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testFindUserId: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(4)
    @CsvSource({
        "testman@notExistsCom, https://www.google.com/, 400", 
        "availMail@123gmail.com, https://www.google.com/, 202", 
    })
    void testSendAuthMail(String email, String redirectUrl, int expectedStatus) throws Exception {
        AuthEmailRequest authEmailRequest = new AuthEmailRequest();
        authEmailRequest.setEmail(email);
        authEmailRequest.setRedirectUrl(redirectUrl);

        String json = objectMapper.writeValueAsString(authEmailRequest);
        MvcResult result = mockMvc.perform(post("/api/users/send-auth-email")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testSendAuthMail: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(5)
    @CsvSource({
        "testmanNotExistID, https://www.google.com/, 400",
        "Alice, https://www.google.com/, 202",
    })
    void testSendAuthMailUserId(String userId, String redirectUrl, int expectedStatus) throws Exception {
        AuthEmailRequest authEmailRequest = new AuthEmailRequest();
        authEmailRequest.setUserId(userId);
        authEmailRequest.setRedirectUrl(redirectUrl);

        String json = objectMapper.writeValueAsString(authEmailRequest);
        MvcResult result = mockMvc.perform(post("/api/users/send-auth-email-userid")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testSendAuthMailUserId: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(6)
    @CsvSource({
        "Alice, Password@1234, 200", 
        "Bob, asdfqwer, 400", 
        "Bob, Secret@Pass2, 200", 
    })
    void testUpdatePassword(String userId, String password, int expectedStatus) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId(userId);
        loginRequest.setPassword(password);

        String json = objectMapper.writeValueAsString(loginRequest);
        MvcResult result = mockMvc.perform(post("/api/users/update-password")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testUpdatePassword: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(7)
    @CsvSource({
        "Alice(Token), Password@123, Alice Master, alice@example2.com, 200",
        "Bob(Token), asdfqwer, Bob Bob, bob, 400",
        "Bob(Token),, Secret@Pass1, Bob Bob, bob@example2.com, 200",
    })
    void testUpdateUser(String token, String password, String name, String email, int expectedStatus) throws Exception {
        User user = new User();
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);

        String json = objectMapper.writeValueAsString(user);
        MvcResult result = mockMvc.perform(post("/api/users/update")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testUpdateUser: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(8)
    @CsvSource({
        "Alice(Token), Password@123, 200",
        "Bob(Token), asdfqwer, 400",
        "Bob(Token), Secret@Pass1, 200",
    })
    void testDeleteUser(String token, String password, int expectedStatus) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/users/delete")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .content(password))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testDeleteUser: {}", result.getResponse().getContentAsString());
    }*/
}
