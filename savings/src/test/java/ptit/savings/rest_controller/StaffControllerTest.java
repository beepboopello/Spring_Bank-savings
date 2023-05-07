package ptit.savings.rest_controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import ptit.savings.model.Staff;
import ptit.savings.model.requestBody.Staff.StaffRegisterBody;
import ptit.savings.model.requestBody.Staff.StaffVerifyBody;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.StaffRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class StaffControllerTest {
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private StaffRest staffRest;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Rollback
    public void testRegister() throws Exception {

        // Tạo đối tượng staff mới
        StaffRegisterBody body = new StaffRegisterBody();
        body.setFirstName("Nguyen");
        body.setLastName("Van A");
        body.setEmail("nguyenvana@gmail.com");
        body.setUsername("nguyenvana");
        body.setPassword("password");

        ResponseEntity<Object> responseEntity = staffRest.register(body, null);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        // Lấy thông tin staff từ database để kiểm tra
        List<Staff> staffList = staffRepo.findByUsername("nguyenvana");
        assertEquals(1, staffList.size());
        Staff staff = staffList.get(0);
        assertEquals(body.getFirstName(), staff.getFirstName());
        assertEquals(body.getLastName(), staff.getLastName());
        assertEquals(body.getEmail(), staff.getEmail());
        assertEquals(body.getUsername(), staff.getUsername());
        assertTrue(BCrypt.checkpw(body.getPassword(), staff.getPassword()));
        System.out.println(staffList);

        // kiểm tra có sự trùng lặp User
        ResponseEntity<Object> response = staffRest.register(body, null);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Rollback(true)
    public void testVerify() {
        // Tạo một đối tượng admin
        Staff admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
        staffRepo.save(admin);

        // Create a new staff member
        Staff newStaff = new Staff("John", "Doe", "john.doe@test.com", "johndoe", "password");
        staffRepo.save(newStaff);

        // Tạo body cho request
        StaffVerifyBody body = new StaffVerifyBody();
        body.setId(999999L);
        body.setToken("test token");
        // Kiểm tra xem người verify có phải là admin hay không, nếu không thì trả về lỗi FORBIDDEN
        ResponseEntity<Object> responseEntity = staffRest.verify(body, null);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

        // Nếu là admin, kiểm tra xem staff có null hay không, nếu có thì trả về lỗi NOT_FOUND
        body.setToken(admin.getToken());
        ResponseEntity<Object> responseEntity1 = staffRest.verify(body, null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity1.getStatusCode());

        body.setId(newStaff.getId());
        // Kiểm tra xem staff đã được verify
        Staff updatedStaff = staffRepo.findById(newStaff.getId()).orElse(null);
        ResponseEntity<Object> responseEntity2 = staffRest.verify(body, null);
        assertEquals(HttpStatus.OK, responseEntity2.getStatusCode());
        assertNotNull(updatedStaff);
        updatedStaff.setVerified(1);
        assertEquals(1, updatedStaff.getVerified());


        // Kiểm tra xem verify đã được cập nhật chưa
        ResponseEntity<Object> responseEntity3 = staffRest.verify(body, null);
        Staff staff = staffRepo.findById(newStaff.getId()).orElse(null);
        assertNotNull(updatedStaff);
        assertEquals(1, staff.getVerified());
        assertEquals(HttpStatus.OK, responseEntity3.getStatusCode());

    }
}