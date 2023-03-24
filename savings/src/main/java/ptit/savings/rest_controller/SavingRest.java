package ptit.savings.rest_controller;

import java.util.HashMap;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ptit.savings.model.Saving;
import ptit.savings.model.requestBody.Saving.AddSavingBody;
import ptit.savings.model.requestBody.Saving.PrematureWithdrawalBody;
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
        // @RequestBody @Valid AddSavingBody body, BindingResult bindingResult
        @RequestParam(name = "stk") String stk,
        @RequestParam(name = "initial") Long initial, 
        @RequestParam(name = "interestID") Long interestId,
        @RequestParam(name = "token") String token
        ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();


            
        // if(bindingResult.hasErrors()){
        //     for (Object object : bindingResult.getAllErrors()) {
        //         if(object instanceof FieldError) {
        //             FieldError fieldError = (FieldError) object;
        //             error.put(fieldError.getField().toString(), fieldError.getDefaultMessage());
        //         }
        //     }
        //     response.put("error",error);
        //     return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        // }
        
        // kiem tra token
        // if(false){
        //     error.put("token", "Invalid token");
        // }

        //
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
            @RequestBody @Valid PrematureWithdrawalBody body, BindingResult bindingResult        //body gom id so tiet kiem can rut, token
        ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        if(bindingResult.hasErrors()){
            response.put("error",error);
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        }



        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    
}
