package ptit.savings.rest_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;
import ptit.savings.model.Account;
import ptit.savings.model.OTP;
import ptit.savings.model.Staff;
import ptit.savings.model.requestBody.Account.AddNewAccountBody;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.OTPRepository;
import ptit.savings.repository.StaffRepository;
import ptit.savings.service.EmailSender;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class AccountRestTest {
    @InjectMocks
    private AccountRest accountRest;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private AccountRepository bankAccountRepository;

    @Mock
    private OTPRepository otpRepository;

    @Mock
    private EmailSender emailSender;

    @Mock
    private BindingResult bindingResult;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddNewAccountSuccess() {
//        AddNewAccountBody body = new AddNewAccountBody();
//        body.setEmail("example@gmail.com");
//        body.setFirstName("A");
//        body.setLastName("Nguyen Van");
//        body.setCccd("0123456789");
//        body.setAddress("Ha Noi");
//        body.setPhone("0123456789");
//        body.setGender("Nam");
//        body.setDob("01/01/2000");
//
//        Staff staff = new Staff();
//        staff.setToken("valid_token");
//        staffRepository.save(staff);
//
//        Account account = new Account();
//        account.setStk("91820000000123");
//        account.setEmail(body.getEmail());
//        account.setFirst_name(body.getFirstName());
//        account.setLast_name(body.getLastName());
//        account.setCccd(body.getCccd());
//        account.setAddress(body.getAddress());
//        account.setPhone(body.getPhone());
//        account.setGender(body.getGender());
//        account.setDob(body.getDob());
//        account.setVerifed(0);
//        account.setCreated_at(LocalDateTime.now());
//        account.setUpdated_at(account.getCreated_at());
//        account.setBalance(50000L);
//
//        OTP otp = new OTP();
//        otp.setStrValue("123456");
//        otp.setAccount(account.getStk());
//        otp.setAction("verify account");
//        otp.setCreated_at(LocalDateTime.now());
//        otp.setExpired_at(otp.getCreated_at().plusMinutes(5));
//
//        HashMap<String, Object> response = new HashMap<>();
//        response.put("account", account);
//
//        List<Staff> staffList1 = new ArrayList<>();
//        List<Account> AccountList = new ArrayList<>();
//        List<OTP> OTPList = new ArrayList<>();
//        when(staffRepository.findByToken(staff.getToken())).thenReturn(staffList1);
//        // Mock the behavior of bankAccountRepository.findByStk() and save()
//        when(bankAccountRepository.findByStk(account.getStk())).thenReturn(AccountList);
//        when(bankAccountRepository.save(account)).thenReturn(account);
//
//        // Mock the behavior of otpRepo.findByStrValue() and save()
//        when(otpRepository.findByStrValue(otp.getStrValue())).thenReturn(OTPList);
//        when(otpRepository.save(otp)).thenReturn(otp);
//
//        // Mock the behavior of emailSender.newBankAccountEmail()
//        emailSender.newBankAccountEmail(body.getEmail(), body.getLastName() + " " + body.getFirstName(), account.getStk(), otp.getStrValue());
//
//        when(bankAccountRepository.findByStk(anyString())).thenReturn(new ArrayList<>());
//
//        // Mock the behavior of staffRepo.findByToken() to return a non-empty list
//        List<Staff> staffList = new ArrayList<>();
//        Staff staff2 = new Staff();
//        staff2.setToken("token123");
//        staffList.add(staff2);
//        when(staffRepository.findByToken(anyString())).thenReturn(staffList);
//
//        // Mock the behavior of otpRepo.findByStrValue() to return an empty list
//        when(otpRepository.findByStrValue(anyString())).thenReturn(new ArrayList<>());


//        ResponseEntity<Object> response1 = accountRest.addNewAccount(body);
        assertEquals(1, 2);

    }

    @Test
    @Disabled
    void addNewAccount() {
    }

    @Test
    void getAllAccounts() {
//        when
    }
}