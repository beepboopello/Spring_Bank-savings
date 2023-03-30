package ptit.savings.rest_controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ptit.savings.model.OTP;
import ptit.savings.model.requestBody.OTP.VerifyAccountBody;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.OTPRepository;
import ptit.savings.repository.SavingRepository;

@RestController
public class OTPRest {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private SavingRepository savingRepo;

    @Autowired
    private OTPRepository otpRepository;

    @PostMapping()
    public ResponseEntity<Object> verifyAccount(
        @RequestBody @Valid VerifyAccountBody body, BindingResult bindingResult
    ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        if(bindingResult.hasErrors()){
            error.put("message", "Invalid request body");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }
        if(otpRepository.findByStrValue(body.getOtp()).isEmpty()){
            error.put("otp","OTP không hợp lệ");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }
        OTP otp = otpRepository.findByStrValue(body.getOtp()).get(0);
        if(otp.getAction().compareTo("verify account")!=0){
            error.put("otp","OTP không hợp lệ");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<Object>(response, HttpStatus.ACCEPTED);
    }
}
