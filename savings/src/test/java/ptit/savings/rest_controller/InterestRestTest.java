package ptit.savings.rest_controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ptit.savings.model.Interest;
import ptit.savings.repository.InterestRepository;
import ptit.savings.service.implement.InterestServiceImpl;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class InterestRestTest {
    @Mock
    private InterestServiceImpl interestService;

    @InjectMocks
    private InterestRest interestController;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(interestController).build();
    }

    @Test
    public void testDeleteInterest() throws Exception {
        // Mock request body
        String requestBody = "{\"id\": 1}";

        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Interest deleted successfully");

        // Mock service method
        when(interestService.getInterestById(1L)).thenReturn(new Interest("Test", 12, 1.2));
        doNothing().when(interestService).deleteInterest(1L);

        // Send request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/interest/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verify response
        String content = result.getResponse().getContentAsString();
        assertEquals(response.get("message").toString(), content);
    }

    @Test
    public void testEditInterest() throws Exception {
        // Mock request body
        String requestBody = "{\"id\": 1, \"rate\": 1.5, \"token\": \"test_token\"}";

        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Interest rate updated successfully");

        // Mock service method
        Interest interest = new Interest("Test", 12, 1.2);
        when(interestService.getInterestById(1L)).thenReturn(interest);
        doNothing().when(interestService).updateInterest(interest);

        // Send request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/interest/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verify response
        String content = result.getResponse().getContentAsString();
        assertEquals(response.get("message").toString(), content);
    }

    @Test
    void testAdd() {
    }

    @Test
    void calculateInterest() {
    }
}