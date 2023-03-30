package ptit.savings.rest_controller;

import ptit.savings.model.*;
import ptit.savings.model.requestBody.Account.AddNewAccountBody;
import ptit.savings.repository.AccountRepository;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.validation.Valid;

@Controller
@RequestMapping(path = "/api/staff/bankaccount")
public class AccountRest {

    @Autowired
    private AccountRepository bankAccountRepository;
    @Autowired
    private EmailSender emailSender;

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

        emailSender.newBankAccountEmail(body.getEmail(), body.getLastName() + " " + body.getFirstName(), stk);
        response.put("account", b);
        return new ResponseEntity<Object>(response , HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Account> getAllAccounts() {
        return bankAccountRepository.findAll();
    }
}