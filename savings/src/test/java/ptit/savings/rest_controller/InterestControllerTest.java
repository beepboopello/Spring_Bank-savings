package ptit.savings.rest_controller;

import jakarta.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import ptit.savings.model.Interest;
import ptit.savings.model.Staff;
import ptit.savings.model.requestBody.Interest.AddBody;
import ptit.savings.model.requestBody.Interest.DeleteBody;
import ptit.savings.model.requestBody.Interest.EditBody;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.StaffRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class InterestControllerTest {
    @Autowired
    private InterestRepository repo;
    @Autowired
    private InterestRest interestController;
    @Autowired
    private StaffRepository staffRepo;

    @Test
    public void testDeleteInterest() {
        // Tạo một interest mới để test
        Interest interest = new Interest();
        interest.setName("Test interest");
//        interest.setId(52L);
        interest.setMonths(6);
        interest.setRate(6.5);
        repo.save(interest);
        // Tạo một đối tượng DeleteBody để xóa interest vừa tạo
        DeleteBody body = new DeleteBody();
        body.setId(interest.getId());
        ResponseEntity<Object> response = interestController.delete(body);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Kiểm tra xem interest đã được xóa thành công hay không
        Interest interest1 = repo.findById(interest.getId()).orElse(null);
        Assert.assertEquals(interest1, null);

        // Tạo một đối tượng không có trong csdl để xóa
        DeleteBody body1 = new DeleteBody();
        body1.setId(3L);
        ResponseEntity<Object> response1 = interestController.delete(body);
        assertEquals(HttpStatus.NOT_FOUND, response1.getStatusCode());


    }
//    @Test
//    public void testEditInterest() {
//        // Tạo một đối tượng admin
//        Staff admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
//        staffRepo.save(admin);
//
//        EditBody body = new EditBody();
//        body.setMonths(30);
//        body.setRate(8);
//        body.setToken("");
//        // nếu admin không tồn tại thì trả về mã lỗi FORBIDDEN
//        ResponseEntity<Object> response = interestController.add(body);
//        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
//        //nếu là admin và không bị trùng số tháng kỳ hạn thì trả về OK
//        body.setToken(admin.getToken());
//        ResponseEntity<Object> response1 = interestController.add(body);
//        Assert.assertEquals(HttpStatus.OK, response1.getStatusCode());
//        // Kiểm tra xem interest đã được thêm vào csdl hay chưa
//        List<Interest> interests = repo.findAll();
//        boolean interestFound = false;
//        for (Interest interest : interests) {
//            if (interest.getMonths() == body.getMonths() && interest.getRate() == body.getRate()) {
//                interestFound = true;
//                break;
//            }
//        }
//        Assert.assertEquals(interestFound, true);
//        // nếu số tháng kỳ hạn bị trùng thì trả về mã lỗi BAD_REQUEST
//        body.setMonths(30);
//        ResponseEntity<Object> response2 = interestController.add(body);
//        Assert.assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
//    }
    @Test
    public void testAddInterest() {
        // Tạo một đối tượng admin
        Staff admin = new Staff(3L, "first name", "last name", "email@gmail.com", "username", "password", 1);
        staffRepo.save(admin);

        AddBody body = new AddBody();
        body.setMonths(30);
        body.setRate(8);
        body.setToken("");
        // nếu admin không tồn tại thì trả về mã lỗi FORBIDDEN
        ResponseEntity<Object> response = interestController.add(body);
        Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        //nếu là admin và không bị trùng số tháng kỳ hạn thì trả về OK
        body.setToken(admin.getToken());
        ResponseEntity<Object> response1 = interestController.add(body);
        Assert.assertEquals(HttpStatus.OK, response1.getStatusCode());
        // Kiểm tra xem interest đã được thêm vào csdl hay chưa
        List<Interest> interests = repo.findAll();
        boolean interestFound = false;
        for (Interest interest : interests) {
            if (interest.getMonths() == body.getMonths() && interest.getRate() == body.getRate()) {
                interestFound = true;
                break;
            }
        }
        Assert.assertEquals(interestFound, true);
        // nếu số tháng kỳ hạn bị trùng thì trả về mã lỗi BAD_REQUEST
        body.setMonths(30);
        ResponseEntity<Object> response2 = interestController.add(body);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
    }
}
