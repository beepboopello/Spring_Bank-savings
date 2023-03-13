package ptit.savings.rest_controller;

import ptit.savings.model.*;
import ptit.savings.repository.BankAccountRepository;
// import ptit.savings.repository.ClientRepository;
import ptit.savings.service.EmailSender;

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
public class BankAccountRest {
  // @Autowired
  // private ClientRepository clientRepo;

  @Autowired
  private BankAccountRepository bankAccountRepository;
  @Autowired
  private EmailSender emailSender;

  @PostMapping(path = "/add")
  public @ResponseBody BankAccount addNewAccount(
      @RequestParam String email,
      @RequestParam String owner,
      @RequestParam String cccd) {

    BankAccount b = new BankAccount();

    b.setEmail(email);
    b.setOwner(owner);
    b.setCccd(cccd);
    Random random = new Random();
    String stk;
    do {
      stk = "9182" + String.format("%08d", random.nextInt(100000000));
    } while (!bankAccountRepository.findByStk(stk).isEmpty());
    b.setStk(stk);
    b.setBalance(Long.parseLong("50000"));
    b.setClientAccount(null);
    bankAccountRepository.save(b);

    emailSender.newBankAccountEmail(email, owner, stk);
    return b;
  }

  @GetMapping(path = "/all")
  public @ResponseBody Iterable<BankAccount> getAllAccounts() {
    return bankAccountRepository.findAll();
  }
}