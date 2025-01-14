package com.example.reviewsplash;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.reviewsplash.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    final private ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @CsvSource({
        "Alice, Password@123, Alice Smith, alice@example.com, 201, Alice Smith, alice@example.com", 
        "Bob, Secret@Pass1, Bob Johnson, bob@example.com, 201, Bob Johnson, bob@example.com", 
        "Bob, Secret#Pass1, Bob Johnson, bob@example.com, 400, null, null"
    })
    void testCreateUser(String userId, String password, String name, String email, int expectedStatus, String expectedName, String expectedEmail) throws Exception {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users/register")
                .contentType("application/json")
                .content(userJson))
                .andExpect(status().is(expectedStatus))
                .andExpect(jsonPath("name").value(expectedName))
                .andExpect(jsonPath("email").value(expectedEmail));
    }
}
