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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.reviewsplash.dto.Location;
import com.example.reviewsplash.dto.PlaceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlaceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    final private ObjectMapper objectMapper = new ObjectMapper();
    static final private Logger logger = LoggerFactory.getLogger(PlaceControllerTest.class);

    @ParameterizedTest
    @Order(1)
    @CsvSource({
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBbGljZSIsImlhdCI6MTczNzQyMTc4MSwiZXhwIjoxNzk3NDIxNzIxfQ.Ky0wbUPT9De--Gr5kFGL1y7SqY_kUMwkIJ-yRVcit9Y, 37.868667, 126.788706, fuel, 200", 
    })
    void testSearch(String token, double latitude, double longitude, String query, int expectedStatus) throws Exception {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        PlaceRequest placeRequest = new PlaceRequest();
        placeRequest.setLocation(location);
        placeRequest.setQuery(query);

        String json = objectMapper.writeValueAsString(placeRequest);
        MvcResult result = mockMvc.perform(post("/api/places/search")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testSearch: {}", result.getResponse().getContentAsString());
    }
}
