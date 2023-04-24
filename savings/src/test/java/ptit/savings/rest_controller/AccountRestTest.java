package ptit.savings.rest_controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.objectweb.asm.TypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ptit.savings.model.Account;
import ptit.savings.model.Interest;
import ptit.savings.model.OTP;
import ptit.savings.model.Staff;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.OTPRepository;
import ptit.savings.repository.StaffRepository;
import ptit.savings.service.implement.EmailSenderImpl;
import ptit.savings.service.implement.InterestServiceImpl;
import ptit.savings.tools.InterestCalculator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class AccountRestTest {
    @Mock
    private StaffRepository staffRepo;
    @Mock
    private AccountRepository accountRepo;

    @Mock
    private OTPRepository otpRepo;

    @Mock
    private EmailSenderImpl emailSender;

    @Mock
    private InterestCalculator interestCalculator;
    @InjectMocks
    private AccountRest accountRest;

    private MockMvc mockMvc;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountRest).build();
    }

    @Test
    void addNewAccountSuccess() throws Exception {
        String requestBody = "{\"email\": 36, \"firstName\": \"kien\",\"lastName\": \"Quach\", " +
                "\"cccd\": 1243221321, \"address\": \"Thai Nguyen\", \"phone\": 32342342346, \"dob\": \"27/07/1999\", \"gender\": \"Nu\", " +
                "\"token\": \"test_token\"}";
        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", "New account created successfully!");

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

        Account account = new Account();
        account.setEmail("kienquach812@gmail.com");
        account.setFirst_name("kien");
        account.setLast_name("quach");
        account.setCccd("123456789");
        account.setAddress("123");
        account.setPhone("123334544");
        account.setGender("Nam");
        account.setDob("12/12/1999");
        account.setVerifed(0);
        account.setCreated_at(LocalDateTime.now());
        account.setUpdated_at(LocalDateTime.now());
        account.setStk("123456789");
        account.setBalance(4546553L);

        List<Account> list1 = new ArrayList<>();
        list1.add(account);
        when(accountRepo.findByStk("999999999")).thenReturn(list1);
        when(accountRepo.save(account)).thenReturn(account);

        OTP otp = new OTP();
        otp.setAccount(account.getStk());
        otp.setAction("verify account");
//        otp.setCreated_at(LocalDateTime.now());
//        otp.setExpired_at(otp.getCreated_at().plusMinutes(5));
        // Send request

        List<OTP> listOTP = new ArrayList<>();
        listOTP.add(otp);
        when(otpRepo.findByStrValue("455677")).thenReturn(listOTP);
        when(otpRepo.save(otp)).thenReturn(otp);

        doNothing().when(emailSender).newBankAccountEmail(account.getEmail(), account.getLast_name() + " " + account.getFirst_name(), account.getStk(), otp.getStrValue());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/bankaccount/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // Verify response
        ObjectMapper objectMapper = new ObjectMapper();
        String content = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(content);
        String message = rootNode.get("message").asText();
        assertEquals(response.get("message").toString(), message);
    }

    @Test
    void addNewAccountVerifyFailed() throws Exception {
        String requestBody = "{\"email\": 36, \"firstName\": \"kien\",\"lastName\": \"Quach\", " +
                "\"cccd\": 1243221321, \"address\": \"Thai Nguyen\", \"phone\": 32342342346, \"dob\": \"27/07/1999\", \"gender\": \"Nu\", " +
                "\"token\": \"test_token\"}";
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
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/bankaccount/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andReturn();
        // Verify response
        ObjectMapper objectMapper = new ObjectMapper();
        String content = result.getResponse().getContentAsString();
        JsonNode rootNode = objectMapper.readTree(content);
        String message = rootNode.get("message").asText();

        assertEquals(response.get("message").toString(), message);
    }

    @Test
    void getAllAccounts() {

        Account account1 = new Account();
        account1.setEmail("john.doe@example.com");
        account1.setFirst_name("John");
        account1.setLast_name("Doe");
        account1.setCccd("123456789");
        account1.setAddress("456 Main St");
        account1.setPhone("555-1234");
        account1.setGender("Male");
        account1.setDob("01/01/1990");
        account1.setVerifed(0);
        account1.setCreated_at(LocalDateTime.now());
        account1.setUpdated_at(LocalDateTime.now());
        account1.setStk("987654321");
        account1.setBalance(100000L);

        Account account2 = new Account();
        account2.setEmail("jane.doe@example.com");
        account2.setFirst_name("Jane");
        account2.setLast_name("Doe");
        account2.setCccd("987654321");
        account2.setAddress("789 Main St");
        account2.setPhone("555-5678");
        account2.setGender("Female");
        account2.setDob("02/02/1995");
        account2.setVerifed(1);
        account2.setCreated_at(LocalDateTime.now());
        account2.setUpdated_at(LocalDateTime.now());
        account2.setStk("456789123");
        account2.setBalance(50000L);

        Account account3 = new Account();
        account3.setEmail("bob.smith@example.com");
        account3.setFirst_name("Bob");
        account3.setLast_name("Smith");
        account3.setCccd("135792468");
        account3.setAddress("123 Elm St");
        account3.setPhone("555-7890");
        account3.setGender("Male");
        account3.setDob("03/03/1985");
        account3.setVerifed(1);
        account3.setCreated_at(LocalDateTime.now());
        account3.setUpdated_at(LocalDateTime.now());
        account3.setStk("369258147");
        account3.setBalance(75000L);

        List<Account> accounts = List.of(account1, account2, account3);
        when(accountRepo.findAll()).thenReturn(accounts);
        Iterable<Account> result = accountRest.getAllAccounts();
        // Then
        assertEquals(accounts, result);
    }
}