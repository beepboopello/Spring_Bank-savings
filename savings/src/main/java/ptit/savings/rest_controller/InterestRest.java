package ptit.savings.rest_controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import ptit.savings.model.Interest;
import ptit.savings.model.requestBody.Interest.AddBody;
import ptit.savings.model.requestBody.Interest.DeleteBody;
import ptit.savings.model.requestBody.Interest.EditBody;
import ptit.savings.repository.InterestRepository;
import ptit.savings.service.InterestService;

@RestController
public class InterestRest {
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
        // Lấy thông tin từ body
        // String name = body.getName();
        int months = body.getMonths();
        String name = String.valueOf(months) + " thang";
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
}
