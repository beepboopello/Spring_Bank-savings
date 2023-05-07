package ptit.savings.rest_controller;

import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import ptit.savings.model.Account;
import ptit.savings.model.Staff;
import ptit.savings.model.requestBody.Account.AddNewAccountBody;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.StaffRepository;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AccountControllerTest {
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountRest accountRest;

    @Test
    @Rollback(value = true)
    public void testAddNewAccountSuccess() throws Exception {
        Staff admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
        staffRepo.save(admin);


        AddNewAccountBody body = new AddNewAccountBody();
        body.setFirstName("John");
        body.setLastName("Doe");
        body.setEmail("johndoe@example.com");
        body.setCccd("123456789");
        body.setAddress("123 Main St");
        body.setPhone("555-1234");
        body.setGender("Male");
        body.setDob(LocalDate.of(1990, 1, 1).toString());
        body.setToken(admin.getToken());

        // When
        ResponseEntity<Object> response = accountRest.addNewAccount(body, null);

        // Then
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
//        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
//
////        Assert.assertThat(responseBody.get("message")).isEqualTo("New account created successfully!");
//        Account account = (Account) responseBody.get("account");
//        Assert.assertEquals(account, accountRepository.findByStk(account.getStk()));
//        Assert.assertThat(account.getId()).isNotNull();
//        Assert.assertThat(account.getStk()).isNotNull();
//        Assert.assertThat(account.getEmail()).isEqualTo(body.getEmail());
//        Assert.assertThat(account.getFirst_name()).isEqualTo(body.getFirstName());
//        assertThat(account.getLast_name()).isEqualTo(body.getLastName());
//        assertThat(account.getCccd()).isEqualTo(body.getCccd());
//        assertThat(account.getAddress()).isEqualTo(body.getAddress());
//        assertThat(account.getPhone()).isEqualTo(body.getPhone());
//        assertThat(account.getGender()).isEqualTo(body.getGender());
//        assertThat(account.getDob()).isEqualTo(body.getDob());
//        assertThat(account.getVerifed()).isEqualTo(0);
//        assertThat(account.getBalance()).isEqualTo(50000L);

        // Verify OTP email was sent
//        List<MimeMessage> messages = GreenMailUtil.getReceivedMessages();
//        assertThat(messages).hasSize(1);
//        MimeMessage message = messages.get(0);
//        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(body.getEmail());
//        assertThat(message.getSubject()).isEqualTo("Bank Account Verification");
//        assertThat(message.getContent().toString()).contains("Dear " + body.getLastName() + " " + body.getFirstName());
//        assertThat(message.getContent().toString()).contains("Your OTP code is " + otpRepo.findByAccount(account.getStk()).get(0).getStrValue());
    }
}