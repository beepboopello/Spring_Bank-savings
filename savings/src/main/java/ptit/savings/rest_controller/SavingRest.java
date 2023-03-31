package ptit.savings.rest_controller;

import java.time.LocalDateTime;
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
import ptit.savings.model.OTP;
import ptit.savings.model.Saving;
import ptit.savings.model.requestBody.Interest.AddBody;
import ptit.savings.model.requestBody.Saving.AddSavingBody;
import ptit.savings.model.requestBody.Saving.WithdrawalBody;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.OTPRepository;
import ptit.savings.repository.SavingRepository;
import ptit.savings.service.EmailSender;

@RestController
public class SavingRest {
    @Autowired
    private SavingRepository savingRepo;

    @Autowired
    private InterestRepository interestRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private OTPRepository otpRepo;

    @Autowired
    private EmailSender emailSender;

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

        OTP otp = new OTP();
        otp.setAccount(number);
        otp.setAction("verify saving");
        otp.setCreated_at(LocalDateTime.now());
        otp.setExpired_at(otp.getCreated_at().plusMinutes(5));
        String value;
        do{
            value = String.format("%06d", random.nextInt(1000000));
        }while(!otpRepo.findByStrValue(value).isEmpty());
        otp.setStrValue(value);
        otpRepo.save(otp);

        emailSender.verifySaving(accountRepo.findByStk(stk).get(0).getEmail(), value);
        Saving saving = new Saving(accountRepo.findByStk(stk).get(0),initial,interestRepo.findById(interestId).get(), number);
        response.put("saving", saving);
        savingRepo.save(saving);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
    @PostMapping("/api/staff/saving/withdrawal")
    public ResponseEntity<Object> withdrawal(
            @RequestBody @Valid WithdrawalBody body, BindingResult bindingResult
        ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        if(bindingResult.hasErrors()){
            response.put("error",error);
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        }
        int option = body.getOption();
        String number = body.getNumber(); // Lấy ID sổ tiết kiệm
        Saving saving = savingRepo.findByNumber(number).get(0);
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
        if(option == 0){
            saving.withdrawal();
        }else{
            saving.withdrawalCash();
        }
        // saving.setStatus(-1);
        savingRepo.save(saving);
        response.put("message", "Successful early withdrawal savings book!");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

}
