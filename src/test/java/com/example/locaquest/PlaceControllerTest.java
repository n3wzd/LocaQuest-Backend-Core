package com.example.locaquest;

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

import com.example.locaquest.dto.MapRouteRequest;
import com.example.locaquest.dto.PlaceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class PlaceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    final private ObjectMapper objectMapper = new ObjectMapper();
    static final private Logger logger = LoggerFactory.getLogger(PlaceControllerTest.class);
    private static final String TEST_TOKEN = "";

    @ParameterizedTest
    @CsvSource({
        TEST_TOKEN + ", 37, 126, fuel, 2000, 200", 
    })
    void testSearch(String token, double latitude, double longitude, String query, int radius, int expectedStatus) throws Exception {
        PlaceRequest placeRequest = new PlaceRequest();
        placeRequest.setLatitude(latitude);
        placeRequest.setLongitude(longitude);
        placeRequest.setQuery(query);
        placeRequest.setRadius(radius);

        String json = objectMapper.writeValueAsString(placeRequest);
        MvcResult result = mockMvc.perform(post("/api/places/search")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testSearch: {}", result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @CsvSource({
        TEST_TOKEN + ", 37, 126, 37, 126, 200", 
    })
    void testRoute(String token, double depLat, double depLng, double destLat, double destLng, int expectedStatus) throws Exception {
        MapRouteRequest mapRouteRequest = new MapRouteRequest();
        mapRouteRequest.setDepLatitude(depLat);
        mapRouteRequest.setDepLongitude(depLng);
        mapRouteRequest.setDestLatitude(destLat);
        mapRouteRequest.setDestLongitude(destLng);

        String json = objectMapper.writeValueAsString(mapRouteRequest);
        MvcResult result = mockMvc.perform(post("/api/places/route")
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .content(json))
                .andExpect(status().is(expectedStatus))
                .andReturn();
        logger.info("testRoute: {}", result.getResponse().getContentAsString());
    }
}
