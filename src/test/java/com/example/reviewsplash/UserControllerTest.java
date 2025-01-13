package com.example.reviewsplash;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.reviewsplash.model.User;
import com.example.reviewsplash.repogitory.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateUser() throws Exception {
        User mockUser = new User();
        mockUser.setName("Alice");
        mockUser.setEmail("alice@example.com");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

        String userJson = "{\"name\":\"Alice\",\"email\":\"alice@example.com\"}";
        mockMvc.perform(post("/api/users/register")
                .contentType("application/json")
                .content(userJson))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value("Alice"))
                .andExpect(jsonPath("email").value("alice@example.com"));
    }
}
