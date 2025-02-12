package com.example.locaquest;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.example.locaquest.dto.user.LoginRequest;
import com.example.locaquest.dto.user.EmailRequest;
import com.example.locaquest.dto.user.PasswordRequest;
import com.example.locaquest.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    final private ObjectMapper objectMapper = new ObjectMapper();
    static final private Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    private static final String TEST_AUTH_TOKEN1 = "";
    private static final String TEST_AUTH_TOKEN2 = "";
    private static final String TEST_TOKEN1 = "";
    private static final String TEST_TOKEN2 = "";

    @ParameterizedTest
    @Order(1)
    @CsvSource({
        "alice@example.com, Password@123, Alice Smith, 010-1234-5678, 202", 
        "bob@example.com, Secret@Pass1, Bob Johnson, 010-1234-6547, 202", 
        "Secret, Power!, testexample, 1234, 400"
    })
    void testCreateUser(String email, String password, String name, String phone, int expectedStatus) throws Exception {
        User user = new User();
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);

        String json = objectMapper.writeValueAsString(user);
        MvcResult result = mockMvc.perform(post("/users/register/send-auth-mail")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testCreateUser: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(2)
    @CsvSource({
        "alice@example.com, Password@123, 200", 
        "bob@example.com, asdfqwer, 400", 
        "Bob, Secret@Pass2, 400", 
        "bob@example.com, Secret@Pass1, 200", 
    })
    void testLoginUser(String email, String password, int expectedStatus) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        String json = objectMapper.writeValueAsString(loginRequest);
        MvcResult result = mockMvc.perform(post("/users/login")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testLoginUser: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(3)
    @CsvSource({
        TEST_AUTH_TOKEN1 + ", 200"
    })
    void testRegisterUser(String token, int expectedStatus) throws Exception {
        MvcResult result = mockMvc.perform(get("/email/register/accept")
                .param("token", token))
                .andReturn();
        logger.info("testRegisterUser: {}", result.getResponse().getContentAsString());
    }
    
    @ParameterizedTest
    @Order(4)
    @CsvSource({
        "testman@notExistsCom, 400", 
        "avail@mail.com, 202", 
    })
    void testChangePasswordSendAuthMail(String email, int expectedStatus) throws Exception {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(email);

        String json = objectMapper.writeValueAsString(emailRequest);
        MvcResult result = mockMvc.perform(post("/users/update-password/send-auth-email")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testChangePasswordSendAuthMail: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(5)
    @CsvSource({
        TEST_AUTH_TOKEN2 + ", 200"
    })
    void testChangePasswordVerifyMail(String token, int expectedStatus) throws Exception {
        MvcResult result = mockMvc.perform(get("/email/update-password/accept")
                .param("token", token))
                .andReturn();
        logger.info("testChangePasswordVerifyMail: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(6)
    @CsvSource({
        "testman@notExistsCom, false, 200", 
        "avail@mail.com, true, 200", 
    })
    void testChangePasswordCheckVerified(String email, String output, int expectedStatus) throws Exception {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(email);

        String json = objectMapper.writeValueAsString(emailRequest);
        MvcResult result = mockMvc.perform(post("/users/update-password/check-verified")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), output);
        logger.info("testChangePasswordCheckVerified: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(7)
    @CsvSource({
        "alice@example.com, Password@1234, 200", 
        "bob@example.com, asdfqwer, 400", 
        "bob@example.com, Secret@Pass2, 200", 
    })
    void testUpdatePassword(String email, String password, int expectedStatus) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        String json = objectMapper.writeValueAsString(loginRequest);
        MvcResult result = mockMvc.perform(post("/users/update-password")
                .contentType("application/json")
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testUpdatePassword: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(8)
    @CsvSource({
        TEST_TOKEN1 + ", Password@123, Alice Master, 200",
        TEST_TOKEN2 + ", asdfqwer, Bob Bob, 400",
        TEST_TOKEN2 + ", Secret@Pass1, Bob Bob, 200",
    })
    void testUpdateUser(String token, String password, String name, int expectedStatus) throws Exception {
        User user = new User();
        user.setPassword(password);
        user.setName(name);

        String json = objectMapper.writeValueAsString(user);
        MvcResult result = mockMvc.perform(post("/users/update")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testUpdateUser: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @Order(9)
    @CsvSource({
        TEST_TOKEN1 + ", Password@123, 200",
        TEST_TOKEN2 + ", asdfqwer, 400",
        TEST_TOKEN2 + ", Secret@Pass1, 200",
    })
    void testDeleteUser(String token, String password, int expectedStatus) throws Exception {
        PasswordRequest passwordRequest = new PasswordRequest();
        passwordRequest.setPassword(password);

        String json = objectMapper.writeValueAsString(passwordRequest);
        MvcResult result = mockMvc.perform(post("/users/delete")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testDeleteUser: {}", result.getResponse().getContentAsString());
    }
}
