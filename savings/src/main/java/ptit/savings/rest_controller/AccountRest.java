package ptit.savings.rest_controller;

import ptit.savings.model.*;
import ptit.savings.model.requestBody.Account.AddNewAccountBody;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.OTPRepository;
import ptit.savings.service.EmailSender;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;

@Controller
@RequestMapping(path = "/api/staff/bankaccount")
public class AccountRest {

    @Autowired
    private AccountRepository bankAccountRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired 
    private OTPRepository otpRepo;

    @PostMapping(path = "/add")
    public ResponseEntity<Object> addNewAccount(
            @RequestBody @Valid AddNewAccountBody body, BindingResult bindingResult) {
        // @RequestParam String email,
        // @RequestParam String firstName,
        // @RequestParam String lastName,
        // @RequestParam String cccd) {
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> error = new HashMap<>();
        if(bindingResult.hasErrors()){
            error.put("message", "Invalid request body");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.BAD_REQUEST);
        }
        Account b = new Account();

        b.setEmail(body.getEmail());
        b.setFirst_name(body.getFirstName());
        b.setLast_name(body.getLastName());
        b.setCccd(body.getCccd());
        b.setVerifed(0);

        b.setCreated_at(LocalDateTime.now());
        b.setUpdated_at(b.getCreated_at());
        Random random = new Random();
        String stk;
        do {
            stk = "9182" + String.format("%08d", random.nextInt(100000000));
        } while (!bankAccountRepository.findByStk(stk).isEmpty());
        b.setStk(stk);
        b.setBalance(Long.parseLong("50000"));
        bankAccountRepository.save(b);

        OTP otp = new OTP();
        otp.setAccount(stk);
        otp.setAction("verify account");
        otp.setCreated_at(LocalDateTime.now());
        otp.setExpired_at(otp.getCreated_at().plusMinutes(5));
        String value;
        do{
            value = String.format("%06d", random.nextInt(1000000));
        }while(!otpRepo.findByStrValue(value).isEmpty());
        otp.setStrValue(value);
        otpRepo.save(otp);

        emailSender.newBankAccountEmail(body.getEmail(), body.getLastName() + " " + body.getFirstName(), stk, otp.getStrValue());
        response.put("account", b);
        return new ResponseEntity<Object>(response , HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Account> getAllAccounts() {
        return bankAccountRepository.findAll();
    }
}