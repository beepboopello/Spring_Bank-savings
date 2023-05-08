package ptit.savings.rest_controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.objectweb.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import ptit.savings.model.Account;
import ptit.savings.model.OTP;
import ptit.savings.model.Staff;
import ptit.savings.model.requestBody.Account.AddNewAccountBody;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.OTPRepository;
import ptit.savings.repository.StaffRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class AccountControllerTest {
    @Autowired
    private AccountRest accountController;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private OTPRepository otpRepo;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Rollback
    public void testAddNewAccountSuccess() {
        // admin
        Staff admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
        staffRepo.save(admin);
        // Given
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
        ResponseEntity<Object> response = accountController.addNewAccount(body, null);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> responseBodyMap = (HashMap<String, Object>) response.getBody();
        // kiểm tra message trả về
        Assert.assertEquals("New account created successfully!", responseBodyMap.get("message"));
        // Kiêm tra account trả về có giống với body

        // Kiểm tra account có tồn tại trong csdl
        Account account = accountRepository.findByCccd("123456789");
        Assert.assertNotNull(account);
        Assert.assertEquals(body.getEmail(), account.getEmail());
        Assert.assertEquals(body.getAddress(), account.getAddress());
        Assert.assertEquals(body.getGender(), account.getGender());
        Assert.assertEquals(body.getFirstName(), account.getFirst_name());
        Assert.assertEquals(body.getLastName(), account.getLast_name());
    }

    @Test
    @Rollback
    public void testAddNewAccountNotIsAdmin() {
        // Arrange
        AddNewAccountBody body = new AddNewAccountBody();
        body.setToken("invalid_token");
        body.setEmail("test@gmail.com");
        body.setFirstName("test");
        body.setLastName("test");
        body.setCccd("123456789");
        body.setAddress("123 123");
        body.setPhone("1234567890");
        body.setGender("test");
        body.setDob("01/01/1990");

        // Act
        ResponseEntity<Object> response = accountController.addNewAccount(body, null);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        String message = (String) ((HashMap<?, ?>) response.getBody()).get("message");
        assertNotNull(message);
    }

    @Test
    @Rollback
    void getAllAccounts() {
        // tạo đối tượng test
        Account b = new Account();
        b.setId(9999L);
        b.setEmail("tes");
        b.setFirst_name("test");
        b.setLast_name("test");
        b.setCccd("test");
        b.setAddress("test");
        b.setPhone("test");
        b.setGender("test");
        b.setDob("test");
        b.setStk("12345678765");
        b.setVerifed(0);
        b.setCreated_at(LocalDateTime.now());
        b.setUpdated_at(b.getCreated_at());
        accountRepository.save(b);
        Iterable<Account> accounts = accountController.getAllAccounts();

        // kiểm tra xem đối tượng test được thêm vào có được trả về hay không
        int size = 0;
        for (Account account : accounts) {
            size++;
            if (account.getId() == 9999L) {
                Assert.assertEquals(b, account);
                Assert.assertEquals(b.getEmail(), account.getEmail());
                Assert.assertEquals(b.getGender(), account.getGender());
                Assert.assertEquals(b.getCccd(), account.getCccd());
                Assert.assertEquals(b.getStk(), account.getStk());
            }
        }
        // kiểm tra kích thước danh sách trả về
        Assert.assertTrue(size > 0);
    }
}