package ptit.savings.rest_controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InterestRestTest {
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
    public void testDeleteInterestSuccess() throws Exception {
        // Mock request body
        String requestBody = "{\"id\": 1}";

        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Interest deleted successfully");
        Staff staff = new Staff();
        staff.setToken("test_token");
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
    public void testDeleteInterestNotFoundId() throws Exception {
        // Mock request body
        String requestBody = "{\"id\": 1}";
        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Interest with given id does not exist");
        Staff staff = new Staff();
        staff.setToken("test_token");
        // Mock service method
        when(interestService.getInterestById(2L)).thenReturn(new Interest("Test", 12, 1.2));
        doNothing().when(interestService).deleteInterest(2L);

        // Send request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/interest/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        // Verify response
        String content = result.getResponse().getContentAsString();

        assertEquals(response.get("message").toString(), content);
    }

    @Test
    public void testEditInterestSuccess() throws Exception {
        // Mock request body
        String requestBody = "{\"id\": 1, \"rate\": 1.5, \"token\": \"test_token\"}";
        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Interest rate updated successfully");
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
    public void testEditInterestNotFound() throws Exception {
        // Mock request body
        String requestBody = "{\"id\": 1, \"rate\": 1.5, \"token\": \"test_token\"}";

        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Interest rate not found");
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
        when(interestService.getInterestById(2L)).thenReturn(interest);
        doNothing().when(interestService).updateInterest(interest);

        // Send request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/interest/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
        // Verify response

        String content = result.getResponse().getContentAsString();
        assertEquals(response.get("message").toString(), content);
    }

    @Test
    public void testEditInterestVerifyTokenAdminFailed() throws Exception {
        // Mock request body
        String requestBody = "{\"id\": 1, \"rate\": 1.5, \"token\": \"test_token\"}";
        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Token verification failed!");
        Staff staff = new Staff();
        staff.setId(1L);
        staff.setLastName("admin");
        staff.setFirstName("admin");
        staff.setEmail("test@gmail.com");
        staff.setToken("admin");
        staff.setIsAdmin(0);
        staff.setUsername("admin");
        List<Staff> list = new ArrayList<>();
        list.add(staff);
        when(staffRepo.findByToken(staff.getToken())).thenReturn(list);
        // Send request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/interest/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();
        // Verify response

        String content = result.getResponse().getContentAsString();
        assertEquals(response.get("message").toString(), content);
    }

    @Test
    void testAddInterestSuccess() throws Exception {
        // Mock request body
        String requestBody = "{\"months\": 36, \"rate\": 7.0,\"token\": \"test_token\"}";

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
    void testAddInterestDuplicateMonth() throws Exception {
        // Mock request body
        String requestBody = "{\"months\": 36, \"rate\": 7.0,\"token\": \"test_token\"}";

        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Duplicate month value");

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
        interest.setMonths(36);

        List<Interest> list1 = new ArrayList<>();
        list1.add(interest);
        when(interestRepo.findByMonths(interest.getMonths())).thenReturn(list1);
        doNothing().when(interestService).addInterest(interest);

        // Send request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/interest/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        // Verify response
        String content = result.getResponse().getContentAsString();
        assertEquals(response.get("message").toString(), content);
    }
    @Test
    void testAddInterestNotIsAdmin() throws Exception {
        // Mock request body
        String requestBody = "{\"months\": 36, \"rate\": 7.0,\"token\": \"test_token\"}";

        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Token verification failed!");

        Staff staff = new Staff();
        staff.setId(1L);
        staff.setLastName("admin");
        staff.setFirstName("admin");
        staff.setEmail("test@gmail.com");
        staff.setToken("token");
        staff.setIsAdmin(0);
        staff.setUsername("admin");
        List<Staff> list = new ArrayList<>();
        list.add(staff);
        when(staffRepo.findByToken(staff.getToken())).thenReturn(list);

        // Send request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/interest/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();
        // Verify response
        String content = result.getResponse().getContentAsString();
        assertEquals(response.get("message").toString(), content);
    }
    @Test
    void testCaculateInterest() throws Exception {
        // Mock request body
        String requestBody = "{\"months\": 36, \"rate\": 7.0,\"token\": \"test_token\"}";

        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "Duplicate month value");

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
        interest.setMonths(36);

        List<Interest> list1 = new ArrayList<>();
        list1.add(interest);
        when(interestRepo.findByMonths(interest.getMonths())).thenReturn(list1);
        doNothing().when(interestService).addInterest(interest);
        // Send request
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/admin/interest/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        // Verify response
        String content = result.getResponse().getContentAsString();
        assertEquals(response.get("message").toString(), content);
    }

    @Test
    void testCalculateInterest1() {
        // Arrange
        Long idInterest = 1L;
        Interest interest = new Interest();
        interest.setRate(6.9);
        interest.setMonths(18);
        when(interestService.getInterestById(idInterest)).thenReturn(interest);

        // Gọi hàm calculateInterest
        Long amount = 100670L;
        ResponseEntity<String> response = interestController.calculateInterest(idInterest.toString(), amount.toString());

        when(interestService.getInterestById(idInterest)).thenReturn(interest);
        ResponseEntity<String> responseEntity = interestController.calculateInterest(idInterest.toString(), amount.toString());
        String data = responseEntity.getBody();
        assertEquals("10419,111089", data);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCalculateInterest2() {
        // Tạo các giả định cho hàm getInterestById
        Long idInterest = 1L;
        Interest interest = new Interest();
        interest.setRate(6.0);
        interest.setMonths(6);
        when(interestService.getInterestById(idInterest)).thenReturn(interest);

        // Gọi hàm calculateInterest
        Long amount = 76248539L;
        ResponseEntity<String> response = interestController.calculateInterest(idInterest.toString(), amount.toString());

        // Kiểm tra kết quả trả về
        String responseBody = response.getBody();
        assertEquals("2287456,78535995", responseBody);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}