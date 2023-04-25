package ptit.savings.rest_controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import ptit.savings.model.OTP;
import ptit.savings.model.Saving;
import ptit.savings.model.Staff;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.OTPRepository;
import ptit.savings.repository.SavingRepository;
import ptit.savings.repository.StaffRepository;
import ptit.savings.tools.InterestCalculator;

@SpringBootTest
@AutoConfigureMockMvc
public class OTPRestTest {
    private MockMvc mockMvc;

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private SavingRepository savingRepo;

    @Mock
    private OTPRepository otpRepository;

    @Mock
    private StaffRepository staffRepo;

    @Mock
    private InterestCalculator interestCalculator;

    @InjectMocks
    private OTPRest otpRest;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(otpRest).build();
    }

    @Test
    public void verifyAccountEmptyOtp() throws Exception{
        init();
        String requestBody = "{ \"token\" : \"randomtokenvalue\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        error.put("message", "Invalid request body");
        response.put("error", error);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/account")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").get("message"), jsonObject2.getJSONObject("error").get("message"));

    }

    @Test
    public void verifyAccountEmptyToken() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        error.put("message", "Invalid request body");
        response.put("error", error);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/account")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").get("message"), jsonObject2.getJSONObject("error").get("message"));

    }


    @Test
    public void verifyAccountEmptyBody() throws Exception{
        init();
        String requestBody = "{}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        error.put("message", "Invalid request body");
        response.put("error", error);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/account")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").get("message"), jsonObject2.getJSONObject("error").get("message"));

    }

    @Test
    public void verifyAccountFailedToken() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\",\"token\" : \"12345678\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        response.put("error", "Xác minh token thất bại");
        List<Staff> listStaff = new ArrayList<>();

        when(staffRepo.findByToken("12345678")).thenReturn(listStaff);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/account")
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
    public void verifyAccountWrongOtpValue() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\",\"token\" : \"12345678\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        error.put("otp","OTP không hợp lệ");
        response.put("error", error);
        List<Staff> listStaff = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("12345678");
        listStaff.add(staff);

        when(staffRepo.findByToken("12345678")).thenReturn(listStaff);
        List<OTP> listOTP = new ArrayList<>();
        when(otpRepository.findByStrValue("512212")).thenReturn(listOTP);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/account")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").getString("otp"), jsonObject2.getJSONObject("error").getString("otp"));

    }

    @Test
    public void verifyAccountWrongOtpAction() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\",\"token\" : \"12345678\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        error.put("otp","OTP không hợp lệ");
        response.put("error", error);
        List<Staff> listStaff = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("12345678");
        listStaff.add(staff);

        when(staffRepo.findByToken("12345678")).thenReturn(listStaff);
        List<OTP> listOTP = new ArrayList<>();
        OTP otp = new OTP();
        otp.setAccount("123456");
        otp.setAction("not a valid action");
        listOTP.add(otp);
        when(otpRepository.findByStrValue("512212")).thenReturn(listOTP);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/account")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").getString("otp"), jsonObject2.getJSONObject("error").getString("otp"));

    }
    
    @Test    
    public void verifyAccountSuccess() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\",\"token\" : \"12345678\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        response.put("message", "Xác minh tài khoản thành công");

        List<Staff> listStaff = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("12345678");
        listStaff.add(staff);

        when(staffRepo.findByToken("12345678")).thenReturn(listStaff);
        List<OTP> listOTP = new ArrayList<>();
        OTP otp = new OTP();
        otp.setAccount("123456");
        otp.setAction("verify account");
        listOTP.add(otp);
        when(otpRepository.findByStrValue("512212")).thenReturn(listOTP);

        Account account = new Account();
        account.setStk("123456");
        account.setVerifed(0);
        List<Account> listAccount = new ArrayList<>();
        listAccount.add(account);
        when(accountRepo.findByStk("123456")).thenReturn(listAccount);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/account")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isAccepted())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getString("message"), jsonObject2.getString("message"));

    }
    
    @Test
    public void verifySavingEmptyOtp() throws Exception{
        init();
        String requestBody = "{ \"token\" : \"randomtokenvalue\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        error.put("message", "Invalid request body");
        response.put("error", error);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/saving")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").get("message"), jsonObject2.getJSONObject("error").get("message"));

    }

    @Test
    public void verifySavingEmptyToken() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        error.put("message", "Invalid request body");
        response.put("error", error);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/saving")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").get("message"), jsonObject2.getJSONObject("error").get("message"));

    }


    @Test
    public void verifySavingEmptyBody() throws Exception{
        init();
        String requestBody = "{}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        error.put("message", "Invalid request body");
        response.put("error", error);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/saving")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").get("message"), jsonObject2.getJSONObject("error").get("message"));

    }

    @Test
    public void verifySavingFailedToken() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\",\"token\" : \"12345678\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        response.put("error", "Xác minh token thất bại");
        List<Staff> listStaff = new ArrayList<>();

        when(staffRepo.findByToken("12345678")).thenReturn(listStaff);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/saving")
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
    public void verifySavingWrongOtpValue() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\",\"token\" : \"12345678\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        error.put("otp","OTP không hợp lệ");
        response.put("error", error);
        List<Staff> listStaff = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("12345678");
        listStaff.add(staff);

        when(staffRepo.findByToken("12345678")).thenReturn(listStaff);
        List<OTP> listOTP = new ArrayList<>();
        when(otpRepository.findByStrValue("512212")).thenReturn(listOTP);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/saving")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").getString("otp"), jsonObject2.getJSONObject("error").getString("otp"));

    }

    @Test
    public void verifySavingWrongOtpAction() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\",\"token\" : \"12345678\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        error.put("otp","OTP không hợp lệ");
        response.put("error", error);
        List<Staff> listStaff = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("12345678");
        listStaff.add(staff);

        when(staffRepo.findByToken("12345678")).thenReturn(listStaff);
        List<OTP> listOTP = new ArrayList<>();
        OTP otp = new OTP();
        otp.setAccount("123456");
        otp.setAction("not a valid action");
        listOTP.add(otp);
        when(otpRepository.findByStrValue("123456")).thenReturn(listOTP);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/saving")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getJSONObject("error").getString("otp"), jsonObject2.getJSONObject("error").getString("otp"));
    }

    @Test    
    public void verifySavingSuccess() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\",\"token\" : \"12345678\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        response.put("message", "Xác minh sổ tiết kiệm thành công");

        List<Staff> listStaff = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("12345678");
        listStaff.add(staff);
        when(staffRepo.findByToken("12345678")).thenReturn(listStaff);

        List<OTP> listOTP = new ArrayList<>();
        OTP otp = new OTP();
        otp.setAccount("123456");
        otp.setAction("verify saving");
        listOTP.add(otp);
        when(otpRepository.findByStrValue("512212")).thenReturn(listOTP);

        List<Saving> listSaving = new ArrayList<>();
        Saving saving = new Saving();
        saving.setNumber("123456");
        saving.setVerify(0);
        listSaving.add(saving);
        when(savingRepo.findByNumber("123456")).thenReturn(listSaving);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/saving")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isAccepted())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getString("message"), jsonObject2.getString("message"));

    }

    @Test    
    public void withdrawalSuccess() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\",\"token\" : \"12345678\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        response.put("message", "Rút sổ tiết kiệm thành công");

        List<Staff> listStaff = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("12345678");
        listStaff.add(staff);
        when(staffRepo.findByToken("12345678")).thenReturn(listStaff);

        List<OTP> listOTP = new ArrayList<>();
        OTP otp = new OTP();
        otp.setAccount("123456");
        otp.setAction("withdrawal");
        listOTP.add(otp);
        when(otpRepository.findByStrValue("512212")).thenReturn(listOTP);

        List<Saving> listSaving = new ArrayList<>();
        Saving saving = new Saving();
        saving.setNumber("123456");
        saving.setVerify(1);
        saving.setInitial(Long.valueOf("1000000"));
        saving.setInterest(new Interest(1L, "3 thang", 3, 0.032));
        saving.setCreated_at(LocalDateTime.now());

        Account account = new Account();
        account.setStk("123457");
        account.setVerifed(1);
        account.setBalance(Long.valueOf("10000000"));
        List<Account> listAccount = new ArrayList<>();
        listAccount.add(account);
        when(accountRepo.findByStk("123456")).thenReturn(listAccount);
        saving.setAccount(account);

        listSaving.add(saving);
        when(savingRepo.findByNumber("123456")).thenReturn(listSaving);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/saving")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isAccepted())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getString("message"), jsonObject2.getString("message"));
    }

    @Test    
    public void withdrawalCashSuccess() throws Exception{
        init();
        String requestBody = "{\"otp\" : \"512212\",\"token\" : \"12345678\"}";
        
        HashMap<String,Object> response = new HashMap<>();
        response.put("message", "Rút sổ tiết kiệm thành công");

        List<Staff> listStaff = new ArrayList<>();
        Staff staff = new Staff();
        staff.setToken("12345678");
        listStaff.add(staff);
        when(staffRepo.findByToken("12345678")).thenReturn(listStaff);

        List<OTP> listOTP = new ArrayList<>();
        OTP otp = new OTP();
        otp.setAccount("123456");
        otp.setAction("withdrawal cash");
        listOTP.add(otp);
        when(otpRepository.findByStrValue("512212")).thenReturn(listOTP);

        List<Saving> listSaving = new ArrayList<>();
        Saving saving = new Saving();
        saving.setNumber("123456");
        saving.setVerify(1);
        saving.setInitial(Long.valueOf("1000000"));
        saving.setInterest(new Interest(1L, "3 thang", 3, 0.032));
        saving.setCreated_at(LocalDateTime.now());

        Account account = new Account();
        account.setStk("123457");
        account.setVerifed(1);
        account.setBalance(Long.valueOf("10000000"));
        List<Account> listAccount = new ArrayList<>();
        listAccount.add(account);
        when(accountRepo.findByStk("123456")).thenReturn(listAccount);
        saving.setAccount(account);

        listSaving.add(saving);
        when(savingRepo.findByNumber("123456")).thenReturn(listSaving);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/otp/saving")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))
        .andExpect(MockMvcResultMatchers.status().isAccepted())
        .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JSONObject jsonObject1 = new JSONObject(response);
        JSONObject jsonObject2 = new JSONObject(content);

        assertEquals(jsonObject1.getString("message"), jsonObject2.getString("message"));

    }
}
