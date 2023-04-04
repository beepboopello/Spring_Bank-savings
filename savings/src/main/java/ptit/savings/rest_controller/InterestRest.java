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
import ptit.savings.model.requestBody.Interest.CalculateBody;
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
        @RequestBody @Valid DeleteBody body, BindingResult bindingResult // Body gom id lai suat can xoa
    ){             
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();
        
        // kiem tra token tu body

        //
//        if(repo.findById(body.getId()).isEmpty()){
//            error.put("error", "Interest id doesn't exist");
//            response.put("error",error);
//            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
//        }
//        repo.deleteById(body.getId());
//        return new ResponseEntity<Object>(response, HttpStatus.OK);

        if(interestService.getInterestById(body.getId()) != null){
            interestService.deleteInterest(body.getId());
            return new ResponseEntity<>("Interest deleted successfully", HttpStatus.OK);
        } else {
            error.put("error", "Interest with given id does not exist");
            response.put("error",error);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/admin/interest/edit")
    public ResponseEntity<Object> edit(
            @RequestBody @Valid EditBody body, BindingResult bindingResult   //Body gom id lai suat can sua, ten, so thang, lai suat(theo %), token
    ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        if(bindingResult.hasErrors()){
            error.put("message", "Invalid request body");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }

        List<Staff> admin = staffRepo.findByToken(body.getToken());
        if(admin.isEmpty()){
            response.put("error", "Xác minh token thất bại");
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        };
        if(admin.get(0).getIsAdmin()==0){
            response.put("error", "Xác minh token thất bại");
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        };

        Long id = body.getId();
        // String name = body.getName();
        // int months = body.getMonths();
        double rate = body.getRate();

        // Thực hiện cập nhật thông tin lãi suất vào database
        Interest interest = interestService.getInterestById(id);
        if(interest == null){
            error.put("message", "Interest rate not found");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
        }
        // interest.setName(name);
        // interest.setMonths(months);
        interest.setRate(rate);
        interestService.updateInterest(interest);

        return new ResponseEntity<Object>(response , HttpStatus.OK);
    }

    @PostMapping("/api/admin/interest/add")
    public ResponseEntity<Object> add(
            @RequestBody @Valid AddBody body, BindingResult bindingResult    //Body gom ten, so thang, lai suat(theo %), token
    ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        if(bindingResult.hasErrors()){
            error.put("message", "Invalid request body");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }
        List<Staff> admin = staffRepo.findByToken(body.getToken());
        if(admin.isEmpty()){
            response.put("error", "Xác minh token thất bại");
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        };
        if(admin.get(0).getIsAdmin()==0){
            response.put("error", "Xác minh token thất bại");
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        };
        // Lấy thông tin từ body
        // String name = body.getName();
        int months = body.getMonths();
        String name = String.valueOf(months) + " tháng";
        double rate = body.getRate();

        if(!repo.findByMonths(months).isEmpty()){
            error.put("months","Duplicate month value");
            response.put("error", error);
            return new ResponseEntity<Object>(response,HttpStatus.BAD_REQUEST);
        }

        // Thêm thông tin lãi suất vào database
        Interest interest = new Interest(name, months, rate);
        interestService.addInterest(interest);

        // Trả về thông tin lãi suất vừa thêm
        response.put("message", "Interest rate added successfully");
        response.put("interest", interest);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @GetMapping("/calculate-interest")
    public ResponseEntity<String> calculateInterest(@RequestParam("idInterest") String idInterest, @RequestParam("amount") String amount) {
        // code here
        Interest interest = interestService.getInterestById(Long.parseLong(idInterest));
        Long result = InterestCalculator.calculate(Long.parseLong(amount), interest);

        Long interestMoney = result - Long.parseLong(amount);
        String data = interestMoney.toString() + "," + result.toString();
        return ResponseEntity.ok().body(data);
    }
}
