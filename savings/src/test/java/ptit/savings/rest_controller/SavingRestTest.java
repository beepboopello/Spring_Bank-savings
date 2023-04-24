package ptit.savings.rest_controller;

import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ptit.savings.model.Account;
import ptit.savings.model.Interest;
import ptit.savings.model.Saving;
import ptit.savings.model.Staff;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.OTPRepository;
import ptit.savings.repository.SavingRepository;
import ptit.savings.repository.StaffRepository;
import ptit.savings.service.EmailSender;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SavingRestTest {
    private MockMvc mockMvc;

    @Mock
    private StaffRepository staffRepo;
    @Mock
    private SavingRepository savingRepo;
    @Mock
    private InterestRepository interestRepo;
    @Mock
    private AccountRepository accountRepo;
    @Mock
    private OTPRepository otpRepo;

    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private SavingRest savingRest;



    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(savingRest).build();
    }

    @Test
    public void addSavingTokenFailed() throws Exception{
        init();
        // Mock request body
        String requestBody = "{\"stk\": 918284242440, \"initial\": 500000, \"interestId\": \"223\", \"token\": \"randomtokenvalue\"}";

        // Mock response
        HashMap<String, Object> response = new HashMap<>();
        response.put("error", "Xác minh token thất bại");

        // Staff staff = new Staff();
        // staff.setToken("test_token");
        List<Staff> list = new ArrayList<>();
        when(staffRepo.findByToken("randomtokenvalue")).thenReturn(list);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/saving/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getString("error"), jsonObject2.getString("error"));

    }

    @Test
    public void addSavingNoStk() throws Exception{
        init();
        String requestBody = "{\"stk\": 918284242440, \"initial\": 500000, \"interestId\": \"223\", \"token\": \"randomtokenvalue\"}";
        HashMap<String, Object> error = new HashMap<>();
        error.put("stk", "stk doesn't exist");
        HashMap<String, Object> response = new HashMap<>();
        response.put("error", error);

        Staff staff = new Staff();
        staff.setToken("randomtokenvalue");
        List<Staff> list = new ArrayList<>();
        list.add(staff);
        when(staffRepo.findByToken("randomtokenvalue")).thenReturn(list);

        List<Account> lAccounts = new ArrayList<>();
        when(accountRepo.findByStk("918284242440")).thenReturn(lAccounts);

        Optional<Interest> lInterests = Optional.of(new Interest());

        when(interestRepo.findById(Long.valueOf("223"))).thenReturn(lInterests);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/saving/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").get("stk"),jsonObject2.getJSONObject("error").get("stk"));

    }

    @Test
    public void addSavingNoInterestId() throws Exception{
        init();
        String requestBody = "{\"stk\": 918284242440, \"initial\": 500000, \"interestId\": \"223\", \"token\": \"randomtokenvalue\"}";
        HashMap<String, Object> error = new HashMap<>();
        error.put("interestId", "Interest id doesn't exist");
        
        HashMap<String, Object> response = new HashMap<>();
        response.put("error", error);

        Staff staff = new Staff();
        staff.setToken("randomtokenvalue");
        List<Staff> list = new ArrayList<>();
        list.add(staff);
        when(staffRepo.findByToken("randomtokenvalue")).thenReturn(list);

        List<Account> lAccounts = new ArrayList<>();
        lAccounts.add(new Account());
        when(accountRepo.findByStk("918284242440")).thenReturn(lAccounts);

        Optional<Interest> lInterests = Optional.empty();

        when(interestRepo.findById(Long.valueOf("223"))).thenReturn(lInterests);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/saving/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").get("interestId"),jsonObject2.getJSONObject("error").get("interestId"));

    }

    @Test
    public void addSavingNoInterestIdNoStk() throws Exception{
        init();
        String requestBody = "{\"stk\": 918284242440, \"initial\": 500000, \"interestId\": \"223\", \"token\": \"randomtokenvalue\"}";
        HashMap<String, Object> error = new HashMap<>();
        error.put("interestId", "Interest id doesn't exist");
        error.put("stk", "stk doesn't exist");
        HashMap<String, Object> response = new HashMap<>();
        response.put("error", error);

        Staff staff = new Staff();
        staff.setToken("randomtokenvalue");
        List<Staff> list = new ArrayList<>();
        list.add(staff);
        when(staffRepo.findByToken("randomtokenvalue")).thenReturn(list);

        List<Account> lAccounts = new ArrayList<>();
        // lAccounts.add(new Account());
        when(accountRepo.findByStk("918284242440")).thenReturn(lAccounts);

        Optional<Interest> lInterests = Optional.empty();

        when(interestRepo.findById(Long.valueOf("223"))).thenReturn(lInterests);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/saving/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").get("interestId"),jsonObject2.getJSONObject("error").get("interestId"));
        assertEquals(jsonObject1.getJSONObject("error").get("stk"),jsonObject2.getJSONObject("error").get("stk"));
    }

    @Test
    public void addSavingSuccess() throws Exception{
        init();
        String requestBody = "{\"stk\": 918284242440, \"initial\": 500000, \"interestId\": \"2\", \"token\": \"randomtokenvalue\"}";
        HashMap<String, Object> error = new HashMap<>();
        error.put("interestId", "Interest id doesn't exist");
        error.put("stk", "stk doesn't exist");
        HashMap<String, Object> response = new HashMap<>();
        response.put("error", error);

        Staff staff = new Staff();
        staff.setToken("randomtokenvalue");
        List<Staff> list = new ArrayList<>();
        list.add(staff);
        when(staffRepo.findByToken("randomtokenvalue")).thenReturn(list);

        List<Account> lAccounts = new ArrayList<>();
        Account account = new Account();
        account.setStk("918284242440");
        account.setEmail("ainchasetema@gmail.com");
        lAccounts.add(account);
        when(accountRepo.findByStk("918284242440")).thenReturn(lAccounts);

        Optional<Interest> lInterests = Optional.of(new Interest(2L, "6 thang", 6, 0.032));

        when(interestRepo.findById(Long.valueOf("2"))).thenReturn(lInterests);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/saving/add")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        // assertEquals(jsonObject1, jsonObject2);

    }

    @Test
    public void withdrawalTokenFailed() throws Exception{
        init();
        String requestBody = "{\"number\": 123456, \"option\": 1, \"token\": \"randomtokenvalue\"}";

        HashMap<String, Object> response = new HashMap<>();
        response.put("error", "Xác minh token thất bại");

        List<Staff> list = new ArrayList<>();

        when(staffRepo.findByToken("randomtokenvalue")).thenReturn(list);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/saving/withdrawal")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);
        
        assertEquals(jsonObject1.getString("error"), jsonObject2.getString("error"));
    }

    @Test
    public void withdrawalTokenNoSaving() throws Exception{
        init();
        String requestBody = "{\"number\": 123456, \"option\": 1, \"token\": \"randomtokenvalue\"}";

        HashMap<String, Object> response = new HashMap<>();

        HashMap<String, Object> error = new HashMap<>();
        error.put("id","Sổ tiết kiệm không tồn tại!");
        response.put("error",error);

        List<Staff> list = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("randomtokenvalue");
        list.add(staff);
        when(staffRepo.findByToken("randomtokenvalue")).thenReturn(list);

        List<Saving> listSaving = new ArrayList<>();
        Saving saving = new Saving();
        
        Account account = new Account();
        account.setStk("918284242440");
        account.setEmail("ainchasetema@gmail.com");
        saving.setAccount(account);
        saving.setNumber("123");
        when(savingRepo.findByNumber("123456")).thenReturn(listSaving);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/saving/withdrawal")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").get("id"), jsonObject2.getJSONObject("error").get("id"));
    }

    @Test
    public void withdrawalAlreadyDone() throws Exception{
        init();
        String requestBody = "{\"number\": 123456, \"option\": 1, \"token\": \"randomtokenvalue\"}";

        HashMap<String, Object> response = new HashMap<>();

        HashMap<String, Object> error = new HashMap<>();
        error.put("status","Sổ tiết kiệm đã đuợc rút truớc đó!");
        response.put("error",error);

        List<Staff> list = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("randomtokenvalue");
        list.add(staff);
        when(staffRepo.findByToken("randomtokenvalue")).thenReturn(list);

        List<Saving> listSaving = new ArrayList<>();
        Saving saving = new Saving();
        
        Account account = new Account();
        account.setStk("918284242440");
        account.setEmail("ainchasetema@gmail.com");
        saving.setAccount(account);
        saving.setNumber("123");
        saving.setStatus(-1);
        listSaving.add(saving);
        when(savingRepo.findByNumber("123456")).thenReturn(listSaving);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/saving/withdrawal")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").get("status"), jsonObject2.getJSONObject("error").get("status"));
    }

    @Test
    public void withdrawalSuccess1() throws Exception{
        init();
        String requestBody = "{\"number\": 123456, \"option\": 1, \"token\": \"randomtokenvalue\"}";

        HashMap<String, Object> response = new HashMap<>();

        response.put("message", "Nhập mã otp để xác minh yêu cầu rút sổ tiết kiệm");

        List<Staff> list = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("randomtokenvalue");
        list.add(staff);
        when(staffRepo.findByToken("randomtokenvalue")).thenReturn(list);

        List<Saving> listSaving = new ArrayList<>();
        Saving saving = new Saving();
        
        Account account = new Account();
        account.setStk("918284242440");
        account.setEmail("ainchasetema@gmail.com");
        saving.setAccount(account);
        saving.setNumber("123");
        saving.setStatus(1);
        listSaving.add(saving);
        when(savingRepo.findByNumber("123456")).thenReturn(listSaving);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/saving/withdrawal")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);


        assertEquals(jsonObject1.getString("message"), jsonObject2.getString("message"));
    }

    @Test
    public void withdrawalSuccess0() throws Exception{
        init();
        String requestBody = "{\"number\": 123456, \"option\": 0, \"token\": \"randomtokenvalue\"}";

        HashMap<String, Object> response = new HashMap<>();

        response.put("message", "Nhập mã otp để xác minh yêu cầu rút sổ tiết kiệm");

        List<Staff> list = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("randomtokenvalue");
        list.add(staff);
        when(staffRepo.findByToken("randomtokenvalue")).thenReturn(list);

        List<Saving> listSaving = new ArrayList<>();
        Saving saving = new Saving();
        
        Account account = new Account();
        account.setStk("918284242440");
        account.setEmail("ainchasetema@gmail.com");
        saving.setAccount(account);
        saving.setNumber("123");
        saving.setStatus(1);
        listSaving.add(saving);
        when(savingRepo.findByNumber("123456")).thenReturn(listSaving);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/staff/saving/withdrawal")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getString("message"), jsonObject2.getString("message"));
    }
}
