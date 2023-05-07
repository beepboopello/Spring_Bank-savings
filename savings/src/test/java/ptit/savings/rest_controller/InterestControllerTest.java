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
import ptit.savings.model.requestBody.Interest.AddBody;
import ptit.savings.model.requestBody.Interest.DeleteBody;
import ptit.savings.repository.InterestRepository;

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
//    public void testAddInterest() {
//        // Tạo một đối tượng AddBody để thêm interest mới
//        AddBody body = new AddBody();
//        body.setMonths(6);
//        body.setRate(5);
//        body.setToken("admin_token");
//
//        // Gọi phương thức add trong InterestController
//        ResponseEntity<Object> response = interestController.add(body);
//        // Kiểm tra xem phương thức có trả về 200 OK hay không
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//
//        // Kiểm tra xem interest đã được thêm thành công hay không
//        List<Interest> interests = repo.findAll();
//        boolean interestFound = false;
//        for (Interest interest : interests) {
//            if (interest.getMonths() == 6 && interest.getRate() == 0.05) {
//                interestFound = true;
//                break;
//            }
//        }
//        Assert.assertTrue(interestFound);
//    }
}
