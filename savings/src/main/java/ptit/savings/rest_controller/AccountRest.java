package ptit.savings.rest_controller;

import ptit.savings.model.*;
import ptit.savings.repository.AccountRepository;
import ptit.savings.service.EmailSender;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/api/staff/bankaccount")
public class AccountRest {

  @Autowired
  private AccountRepository bankAccountRepository;
  @Autowired
  private EmailSender emailSender;

  @PostMapping(path = "/add")
  public @ResponseBody Account addNewAccount(
      @RequestParam String email,
      @RequestParam String firstName,
      @RequestParam String lastName,
      @RequestParam String cccd) {

    Account b = new Account();

    b.setEmail(email);
    b.setFirst_name(firstName);
    b.setLast_name(lastName);
    b.setCccd(cccd);

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

    emailSender.newBankAccountEmail(email, lastName + " " + firstName, stk);
    return b;
  }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Account> getAllAccounts() {
      return bankAccountRepository.findAll();
    }
}