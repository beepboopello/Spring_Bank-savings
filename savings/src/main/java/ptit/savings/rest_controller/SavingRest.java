package ptit.savings.rest_controller;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ptit.savings.model.Account;
import ptit.savings.model.Saving;
import ptit.savings.model.requestBody.Interest.AddBody;
import ptit.savings.model.requestBody.Saving.AddSavingBody;
import ptit.savings.model.requestBody.Saving.PrematureWithdrawalBody;
import ptit.savings.model.requestBody.Saving.withdrawBody;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.SavingRepository;

@RestController
public class SavingRest {
    @Autowired
    private SavingRepository savingRepo;

    @Autowired
    private InterestRepository interestRepo;

    @Autowired
    private AccountRepository accountRepo;

    @PostMapping("/api/staff/saving/add")
    public ResponseEntity<Object> add(
         @RequestBody @Valid AddSavingBody body, BindingResult bindingResult
        ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        if(bindingResult.hasErrors()){
            for (Object object : bindingResult.getAllErrors()) {
                if(object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;
                    error.put(fieldError.getField().toString(), fieldError.getDefaultMessage());
                }
            }
            response.put("error",error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }
        // kiem tra token
        // if(false){
        //     error.put("token", "Invalid token");
        // }

        String stk = body.getStk();
        Long initial = body.getInitial();
        Long interestId = body.getInterestId();
        String token = body.getToken();

        if(accountRepo.findByStk(stk).isEmpty()){
            error.put("stk", "stk doesn't exist");
        }
        if(interestRepo.findById(interestId).isEmpty()){
            error.put("interestId", "Interest id doesn't exist");
        }
        if(!error.isEmpty()){
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        }
        String number;
        Random random = new Random();
        do {
            number = "1473" + String.format("%08d", random.nextInt(100000000));
        } while (!savingRepo.findByNumber(number).isEmpty());
        Saving saving = new Saving(accountRepo.findByStk(stk).get(0),initial,interestRepo.findById(interestId).get(), number);
        response.put("saving", saving);
        savingRepo.save(saving);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
    @PostMapping("/api/staff/saving/premature")
    public ResponseEntity<Object> premature(
            @RequestBody @Valid PrematureWithdrawalBody body, BindingResult bindingResult
        ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        if(bindingResult.hasErrors()){
            response.put("error",error);
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        }

        Long id = body.getId(); // Lấy ID sổ tiết kiệm
        Saving saving = savingRepo.findById(id).get();
//        Optional<Saving> optionalSaving = savingRepo.findById(id);
//        if(!optionalSaving.isPresent()){
        if(saving == null){
            error.put("id","Saving doesn't exist!");
            response.put("error",error);
            return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
        }
        if(saving.getStatus() == -1){
            error.put("status","The SavingsBook was withdrawn early!");
            response.put("error",error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }
        saving.prematureWithdrawal();
//        saving.setStatus(-1);
        savingRepo.save(saving);
        response.put("message", "Successful early withdrawal savings book!");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
//    @PostMapping("/api/staff/saving/withdraw")
//    public ResponseEntity<Object> withdraw(
//            @RequestBody @Valid withdrawBody body, BindingResult bindingResult // body gom id so tiet kiem,
//    ){
//        HashMap<String,Object> response = new HashMap<>();
//        HashMap<String,Object> error = new HashMap<>();
//
//        if(bindingResult.hasErrors()){
//            response.put("error",error);
//            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
//        }
//        Long id = body.getId(); // Lấy ID sổ tiết kiệm
//        Saving saving = savingRepo.findById(id).get();
//        if(saving == null){
//            error.put("id","Saving doesn't exist!");
//            response.put("error",error);
//            return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
//        }
//        if(saving.getStatus() == -1){
//            error.put("status","The SavingsBook was withdrawn!");
//            response.put("error",error);
//            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
//        }
//        saving.prematureWithdrawal();
////        saving.setStatus(-1);
//        savingRepo.save(saving);
//        response.put("message", "Successful withdrawal savings book!");
//        return new ResponseEntity<Object>(response, HttpStatus.OK);
//    }
}
