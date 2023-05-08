package ptit.savings.rest_controller;

import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.LogManager;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import ptit.savings.model.*;
import ptit.savings.model.requestBody.OTP.VerifyBody;
import ptit.savings.repository.*;
import ptit.savings.service.EmailSender;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class OTPControllerTest {
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private AccountRest accountController;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OTPRest otpController;
    Staff admin;
    Account account;
    OTP otp;
    Saving saving;
    Interest interest;

    @Autowired
    private EmailSender emailSender;
    @Autowired
    private SavingRepository savingRepository;
    @Autowired
    private SavingRest savingController;
    @Autowired
    private InterestRepository interestRepository;

    @BeforeEach
    void setUp() {
        admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
        staffRepo.save(admin);

        account = new Account();
        account.setStk("1234567890");
        account.setEmail("test@gmail.com");
        account.setFirst_name("test");
        account.setLast_name("test");
        account.setDob("1999-01-01");
        account.setCccd("123456789012");
        account.setPhone("1234567890");
        account.setGender("test");
        account.setVerifed(0);
        accountRepository.save(account);

        otp = new OTP();
        otp.setAccount("1234567890");
        otp.setAction("verify account");
        otp.setStrValue("123456");
        otp.setExpired_at(LocalDateTime.now().plusYears(1));
        otp.setCreated_at(LocalDateTime.now());
        otpRepository.save(otp);
//        OTP otp = new OTP();
//        otp.setStrValue("123456");
//        otp.setExpired_at(LocalDateTime.now().plusMinutes(5));
//        otp.setAction("verify saving");
//        otp.setAccount(saving.getNumber());
//        otpRepository.save(otp);
//        Saving saving = new Saving(account,100000L,interest,"1234567890");
//        savingRepo.save(saving);
//
        interest = new Interest();
        interest.setRate(6.7);
        interest.setMonths(10);
        interest.setName("Lãi suất 10 tháng");
        interestRepository.save(interest);
        saving = new Saving();
        saving.setNumber("123456789");
        saving.setAccount(account);
        saving.setInitial(100000L);
        saving.setStatus(0);
        saving.setVerified_at(null);
        saving.setInterest(interest);
        saving.setVerify(0);
        saving.setCreated_at(LocalDateTime.now());
        saving.setUpdated_at(LocalDateTime.now());
        saving.setStarted_at(LocalDateTime.now());
        savingRepository.save(saving);

    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void testVerifyAccountSuccess() {
        // Tạo body để test
        VerifyBody body = new VerifyBody();
        body.setToken(admin.getToken());
        body.setOtp("123456");
        ResponseEntity<Object> response = otpController.verifyAccount(body, new BeanPropertyBindingResult(body, "body"));
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertTrue(response.getBody() instanceof HashMap);
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        assertEquals("Xác minh tài khoản thành công", responseBody.get("message"));
        assertEquals(account, responseBody.get("account"));
        assertEquals(1, account.getVerifed());
        assertNotNull(account.getVerified_at());
        assertEquals(0, otpRepository.findByStrValue("123456").size());
    }

    @Test
    @Rollback
    void testVerifyAccountInvalidToken() {
        // Tạo body để test
        VerifyBody body = new VerifyBody();
        body.setToken("not is token admin");
        body.setOtp("123456");
        // Act
        ResponseEntity<Object> response = otpController.verifyAccount(body, new BeanPropertyBindingResult(body, "body"));

        // kiểm tra xem có phải là admin không?
        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        HashMap<String, Object> responseBody = (HashMap<String, Object>) response.getBody();
        Assert.assertEquals("Xác minh token thất bại", responseBody.get("error"));
    }

    @Test
    @Rollback
    void testVerifyAccountNullOTP() {
        // Tạo body để test
        VerifyBody body = new VerifyBody();
        body.setToken(admin.getToken());
//        body.setOtp("123456");
        // Act
        ResponseEntity<Object> response = otpController.verifyAccount(body, new BeanPropertyBindingResult(body, "body"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Rollback
    void testVerifyAccountActionoOTPNotEquals() {
        // Tạo body để test
        VerifyBody body = new VerifyBody();
        body.setToken(admin.getToken());
//        body.setOtp("123456");
        // Act
        ResponseEntity<Object> response = otpController.verifyAccount(body, new BeanPropertyBindingResult(body, "body"));
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Rollback
    void verifySavingSuccess() {
        // Tạo một đối tượng OTP
        OTP otp = new OTP();
        otp.setStrValue("999999");
        otp.setExpired_at(LocalDateTime.now().plusMinutes(5));
        otp.setAction("verify saving");
        otp.setCreated_at(LocalDateTime.now());
        otp.setAccount(saving.getNumber());
        otpRepository.save(otp);

        // Tạo đối tượng VerifyBody
        VerifyBody verifyBody = new VerifyBody();
        verifyBody.setToken(admin.getToken());
        verifyBody.setOtp("999999");

        ResponseEntity<Object> responseEntity = otpController.verifySaving(verifyBody, new BeanPropertyBindingResult(verifyBody, "verifyBody"));

        // Kiểm tra kết quả trả về
        Assert.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());

//         Kiểm tra dữ liệu trong database
        List<Saving> optionalSaving = savingRepository.findByNumber("123456789");
        Assert.assertTrue(optionalSaving.size() > 0);
        Saving savedSaving = optionalSaving.get(0);
        assertEquals(1, savedSaving.getStatus());
        assertNotNull(savedSaving.getVerified_at());

    }

    @Test
    @Rollback
    void verifySavingNotIsAdmin() {
        // Tạo một đối tượng OTP
        OTP otp = new OTP();
        otp.setStrValue("999999");
        otp.setExpired_at(LocalDateTime.now().plusMinutes(5));
        otp.setAction("verify saving");
        otp.setCreated_at(LocalDateTime.now());
        otp.setAccount(saving.getNumber());
        otpRepository.save(otp);

        // Tạo đối tượng VerifyBody
        VerifyBody verifyBody = new VerifyBody();
        verifyBody.setToken("admin.getToken()");
        verifyBody.setOtp("999999");

        ResponseEntity<Object> responseEntity = otpController.verifySaving(verifyBody, new BeanPropertyBindingResult(verifyBody, "verifyBody"));

        // Kiểm tra kết quả trả về
        Assert.assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());


    }

    @Test
    @Rollback
    void verifySavingInvalidOTP() {
        // Tạo một đối tượng OTP
        OTP otp = new OTP();
        otp.setStrValue("999999");
        otp.setExpired_at(LocalDateTime.now().plusMinutes(5));
        otp.setAction("verify saving");
        otp.setCreated_at(LocalDateTime.now());
        otp.setAccount(saving.getNumber());
        otpRepository.save(otp);

        // Tạo đối tượng VerifyBody
        VerifyBody verifyBody = new VerifyBody();
        verifyBody.setToken(admin.getToken());

        ResponseEntity<Object> responseEntity = otpController.verifySaving(verifyBody, new BeanPropertyBindingResult(verifyBody, "verifyBody"));

        // Kiểm tra kết quả trả về
        Assert.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());


    }
}