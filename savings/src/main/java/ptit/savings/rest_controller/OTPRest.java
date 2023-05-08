package ptit.savings.rest_controller;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ptit.savings.model.Account;
import ptit.savings.model.OTP;
import ptit.savings.model.Saving;
import ptit.savings.model.requestBody.OTP.VerifyBody;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.OTPRepository;
import ptit.savings.repository.SavingRepository;
import ptit.savings.repository.StaffRepository;
import ptit.savings.service.EmailSender;

@RestController
public class OTPRest {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private SavingRepository savingRepo;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    private EmailSender emailSender;

    @PostMapping("/api/otp/account")
    public ResponseEntity<Object> verifyAccount(
        @RequestBody @Valid VerifyBody body, BindingResult bindingResult
    ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();
        
        if(staffRepo.findByToken(body.getToken()).isEmpty()){
            response.put("error", "Xác minh token thất bại");
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        };

        if(otpRepository.findByStrValue(body.getOtp()).isEmpty()){
            error.put("otp","OTP không hợp lệ");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }
        body.setOtp(body.getOtp().trim());
        OTP otp = otpRepository.findByStrValue(body.getOtp()).get(0);
        if(otp.getAction().compareTo("verify account")!=0){
            error.put("otp","OTP không hợp lệ");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }

        Account account = accountRepo.findByStk(otp.getAccount()).get(0);
        account.setVerifed(1);
        account.setVerified_at(LocalDateTime.now());
        accountRepo.save(account);
        response.put("message", "Xác minh tài khoản thành công");
        response.put("account", account);     
        emailSender.newBankAccountVerifiedEmail(account);   
        otpRepository.delete(otp);
        return new ResponseEntity<Object>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/api/otp/saving")
    public ResponseEntity<Object> verifySaving(
        @RequestBody @Valid VerifyBody body, BindingResult bindingResult
    ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();


        if(staffRepo.findByToken(body.getToken()).isEmpty()){
            response.put("error", "Xác minh token thất bại");
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        };
        
        if(otpRepository.findByStrValue(body.getOtp()).isEmpty()){
            error.put("otp","OTP không hợp lệ");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }
        body.setOtp(body.getOtp().trim());
        OTP otp = otpRepository.findByStrValue(body.getOtp()).get(0);
        Saving saving = savingRepo.findByNumber(otp.getAccount()).get(0);

        if(otp.getAction().compareTo("withdrawal")==0){
            saving.withdrawal();
            savingRepo.save(saving);
            otpRepository.delete(otp);
            response.put("message", "Rút sổ tiết kiệm thành công");
            emailSender.withdrawalVerified(saving);
            response.put("saving", saving);    
            return new ResponseEntity<Object>(response, HttpStatus.ACCEPTED);
        }

        if(otp.getAction().compareTo("withdrawal cash")==0){
            saving.withdrawalCash();
            savingRepo.save(saving);
            otpRepository.delete(otp);
            response.put("message", "Rút sổ tiết kiệm thành công");
            emailSender.withdrawalVerified(saving);
            response.put("saving", saving);    
            return new ResponseEntity<Object>(response, HttpStatus.ACCEPTED);
        }


        if(otp.getAction().compareTo("verify saving")!=0){
            error.put("otp","OTP không hợp lệ");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }


//        saving.setVerify(1);
        saving.setStatus(1);
        saving.setVerified_at(LocalDateTime.now());
        savingRepo.save(saving);
        response.put("message", "Xác minh sổ tiết kiệm thành công");
        response.put("saving", saving);    
        emailSender.savingVerified(saving);
        otpRepository.delete(otp);
        return new ResponseEntity<Object>(response, HttpStatus.ACCEPTED);
    }
}
