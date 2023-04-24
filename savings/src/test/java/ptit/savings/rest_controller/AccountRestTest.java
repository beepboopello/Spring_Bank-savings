package ptit.savings.rest_controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

class AccountRestTest {
    @Mock
    private InterestServiceImpl interestService;
    @Mock
    private StaffRepository staffRepo;
    @Mock
    private InterestRepository interestRepo;

    @Mock
    private InterestCalculator interestCalculator;
    @InjectMocks
    private InterestRest interestController;

    private MockMvc mockMvc;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(interestController).build();
    }
    @Test
    void addNewAccountSuccess() throws Exception {

        String requestBody = "{\"email\": 36, \"firstName\": 7.0,\"lastName\": \"test_token\", \"cccd\": 36, \"address\": 36, \"phone\": 36, \"dob\": 36, \"gender\": 36, \"token\": 36, \"gender\": 36,}";

        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Interest rate added successfully");

        Staff staff = new Staff();
        staff.setId(1L);
        staff.setLastName("admin");
        staff.setFirstName("admin");
        staff.setEmail("test@gmail.com");
        staff.setToken("test_token");
        staff.setIsAdmin(1);
        staff.setUsername("admin");
        List<Staff> list = new ArrayList<>();
        list.add(staff);
        when(staffRepo.findByToken(staff.getToken())).thenReturn(list);
        // Mock service method
        Interest interest = new Interest();
        interest.setRate(1.2);
        interest.setMonths(32);

        List<Interest> list1 = new ArrayList<>();
        list1.add(interest);
        when(interestRepo.findByMonths(interest.getMonths())).thenReturn(list1);
        doNothing().when(interestService).addInterest(interest);

        // Send request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/interest/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // Verify response

        String content = result.getResponse().getContentAsString();

        assertEquals(response.get("message").toString(), content);
    }

    @Test
    void getAllAccounts() {
    }
}