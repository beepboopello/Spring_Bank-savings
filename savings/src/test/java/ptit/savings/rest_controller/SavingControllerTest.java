package ptit.savings.rest_controller;

import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import ptit.savings.model.Account;
import ptit.savings.model.Interest;
import ptit.savings.model.Saving;
import ptit.savings.model.Staff;
import ptit.savings.model.requestBody.Saving.AddSavingBody;
import ptit.savings.model.requestBody.Saving.WithdrawalBody;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.SavingRepository;
import ptit.savings.repository.StaffRepository;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class SavingControllerTest {
    @Autowired
    private SavingRepository savingRepo;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private StaffRest staffRest;
    @Autowired
    private SavingRest savingController;

    @Test
    @Rollback(value = true)
    void testAddSuccess() {
        Staff admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
        staffRepo.save(admin);

        String token = "abcxyz";
        String stk = "918294935029";
        Long initial = 1000000L;
        Long interestId = 1L;
        // Tạo body
        AddSavingBody body = new AddSavingBody();
        body.setStk(stk);
        body.setInitial(initial);
        body.setInterestId(interestId);
        body.setToken(admin.getToken());

        ResponseEntity<Object> response = savingController.add(body, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Saving saving = savingRepo.findByAccount_Stk(stk).get(0);
        assertNotNull(saving);
        assertEquals(stk, saving.getAccount().getStk());
        assertEquals(initial, saving.getInitial());
        assertEquals(interestId, saving.getInterest().getId());
        System.out.println(saving);
    }

    @Test
    @Rollback(value = true)
    void testAddNotIsAdmin() {
        Staff admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
        staffRepo.save(admin);

        String token = "abcxyz";
        String stk = "918294935029";
        Long initial = 1000000L;
        Long interestId = 1L;
        // Tạo body
        AddSavingBody body = new AddSavingBody();
        body.setStk(stk);
        body.setInitial(initial);
        body.setInterestId(interestId);
        body.setToken("admin.getToken()");

        ResponseEntity<Object> response = savingController.add(body, null);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Rollback(value = true)
    void testAddStkNotFound() {
        Staff admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
        staffRepo.save(admin);

        String token = "abcxyz";
        String stk = "01234565423";
        Long initial = 1000000L;
        Long interestId = 1L;
        // Tạo body
        AddSavingBody body = new AddSavingBody();
        body.setStk(stk);
        body.setInitial(initial);
        body.setInterestId(interestId);
        body.setToken(admin.getToken());

        ResponseEntity<Object> response = savingController.add(body, null);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Rollback(value = true)
    void testAddInterestNotFound() {
        Staff admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
        staffRepo.save(admin);

        String token = "abcxyz";
        String stk = "918294935029";
        Long initial = 1000000L;
        Long interestId = 999999L;
        // Tạo body
        AddSavingBody body = new AddSavingBody();
        body.setStk(stk);
        body.setInitial(initial);
        body.setInterestId(interestId);
        body.setToken(admin.getToken());

        ResponseEntity<Object> response = savingController.add(body, null);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Rollback
    public void testWithdrawalSuccess() {
        Staff admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
        staffRepo.save(admin);

        Account account = accountRepository.findById(1L).get();

        Interest interest = interestRepository.findById(1L).get();
        Saving saving = new Saving(account,100000L,interest,"1234567890");
        savingRepo.save(saving);


        WithdrawalBody withdrawalBody = new WithdrawalBody();
        withdrawalBody.setToken(admin.getToken());
        withdrawalBody.setOption(0);
        withdrawalBody.setNumber(saving.getNumber());

        ResponseEntity<Object> response = savingController.withdrawal(withdrawalBody, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Nhập mã otp để xác minh yêu cầu rút sổ tiết kiệm", response.getBody());
        Saving savings = savingRepo.findByNumber("test_number").get(0);
        assertEquals(-1, savings.getStatus());
    }
    @Test
    @Rollback
    public void testWithdrawalStatus() {
        Staff admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
        staffRepo.save(admin);

        Account account = accountRepository.findById(1L).get();

        Interest interest = interestRepository.findById(1L).get();
        Saving saving = new Saving(account,100000L,interest,"1234567890");
        saving.setStatus(-1);
        savingRepo.save(saving);

        WithdrawalBody withdrawalBody = new WithdrawalBody();
        withdrawalBody.setToken(admin.getToken());
        withdrawalBody.setOption(0);
        withdrawalBody.setNumber(saving.getNumber());

        ResponseEntity<Object> response = savingController.withdrawal(withdrawalBody, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Sổ tiết kiệm đã đuợc rút truớc đó!", response.getBody());

    }
}