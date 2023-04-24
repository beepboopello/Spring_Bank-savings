package ptit.savings.rest_controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ptit.savings.model.Interest;
import ptit.savings.model.Staff;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.StaffRepository;
import ptit.savings.service.implement.InterestServiceImpl;
import ptit.savings.tools.InterestCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class StaffRestTest {
    @Mock
    private StaffRepository staffRepo;
    @InjectMocks
    private StaffRest staffRest;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(staffRest).build();
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        String requestBody = "{\"firstName\": \"Kien\", \"lastName\": \"Quach Dinh\",\"username\": \"user01\", \"email\": \"user01@gmail.com\", \"password\": \"123456789\"}";

        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Register successfully");
        List<Staff> list = new ArrayList<>();
        Staff staff = new Staff("fname", "lname", "email@gmail.com", "username", "password");
        list.add(staff);
        when(staffRepo.findByUsername(staff.getUsername())).thenReturn(list);
        when(staffRepo.save(staff)).thenReturn(staff);
        // Send request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String content = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(content);
        String message = rootNode.get("message").asText();
        assertEquals(response.get("message").toString(), message);
    }

    @Test
    public void testRegisterDuplicateUsername() throws Exception {
        String requestBody = "{\"firstName\": \"Kien\", \"lastName\": \"Quach Dinh\",\"username\": \"user01\", \"email\": \"user01@gmail.com\", \"password\": \"123456789\"}";

        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Duplicate username");
        List<Staff> list = new ArrayList<>();
        Staff staff = new Staff("fname", "lname", "email@gmail.com", "user01", "password");
        list.add(staff);
        when(staffRepo.findByUsername(staff.getUsername())).thenReturn(list);
        when(staffRepo.save(staff)).thenReturn(staff);
        // Send request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String content = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(content);
        String message = rootNode.get("message").asText();
        assertEquals(response.get("message").toString(), message);
    }

    @Test
    void verify() {
    }
}