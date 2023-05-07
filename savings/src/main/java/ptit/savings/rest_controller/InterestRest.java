package ptit.savings.rest_controller;

import java.util.HashMap;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ptit.savings.model.Interest;
import ptit.savings.model.Staff;
import ptit.savings.model.requestBody.Interest.AddBody;
import ptit.savings.model.requestBody.Interest.DeleteBody;
import ptit.savings.model.requestBody.Interest.EditBody;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.StaffRepository;
import ptit.savings.service.InterestService;
import ptit.savings.tools.InterestCalculator;

@RestController
public class InterestRest {
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private InterestRepository repo;

    @Autowired
    private InterestService interestService;

    @PostMapping("/api/admin/interest/delete")
    public ResponseEntity<Object> delete(
            @RequestBody @Valid DeleteBody body // Body gom id lai suat can xoa
    ) {
        if (interestService.getInterestById(body.getId()) != null) {
            interestService.deleteInterest(body.getId());
            return new ResponseEntity<>("Interest deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Interest with given id does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/admin/interest/edit")
    public ResponseEntity<Object> edit(
            @RequestBody @Valid EditBody body, BindingResult bindingResult) {
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> error = new HashMap<>();
        List<Staff> admin = staffRepo.findByToken(body.getToken());
        if (admin.isEmpty() || admin.get(0).getIsAdmin() == 0) {
            return new ResponseEntity<Object>("Token verification failed!", HttpStatus.FORBIDDEN);
        }

        Long id = body.getId();
        double rate = body.getRate();

        Interest interest = interestService.getInterestById(id);
        if (interest == null) {
            return new ResponseEntity<Object>("Interest rate not found", HttpStatus.NOT_FOUND);
        }
        interest.setRate(rate);
        interestService.updateInterest(interest);
        return new ResponseEntity<Object>("Interest rate updated successfully", HttpStatus.OK);
    }

    @PostMapping("/api/admin/interest/add")
    public ResponseEntity<Object> add(
            @RequestBody @Valid AddBody body    //Body gom ten, so thang, lai suat(theo %), token
    ) {
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> error = new HashMap<>();
        List<Staff> admin = staffRepo.findByToken(body.getToken());
        if (admin.isEmpty() || admin.get(0).getIsAdmin() == 0) {
            return new ResponseEntity<Object>("Token verification failed!", HttpStatus.FORBIDDEN);
        }
        int months = body.getMonths();
        String name = String.valueOf(months) + " tháng";
        double rate = body.getRate();
        if (!repo.findByMonths(months).isEmpty()) {
            return new ResponseEntity<Object>("Duplicate month value", HttpStatus.BAD_REQUEST);
        }
        System.out.println("name: " + name + ", months: " + months + ", rate: " + rate);
        // Thêm thông tin lãi suất vào database
        Interest interest = new Interest(name, months, rate);
        interestService.addInterest(interest);
        return new ResponseEntity<Object>("Interest rate added successfully", HttpStatus.OK);
    }
    @GetMapping("/calculate-interest")
    public ResponseEntity<String> calculateInterest(@RequestParam("idInterest") String idInterest, @RequestParam("amount") String amount) {
        Interest interest = interestService.getInterestById(Long.parseLong(idInterest));
        Long result = InterestCalculator.calculate(Long.parseLong(amount), interest);
        Long interestMoney = result - Long.parseLong(amount);
        String data = interestMoney.toString() + "," + result.toString();
        return ResponseEntity.ok().body(data);
    }
}
