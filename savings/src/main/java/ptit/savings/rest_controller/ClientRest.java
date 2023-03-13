package ptit.savings.rest_controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ptit.savings.model.requestBody.verifyNewClientBody;
import ptit.savings.model.BankAccount;
import ptit.savings.model.Client;
import ptit.savings.model.OTP;
import ptit.savings.model.requestBody.addClientBody;
import ptit.savings.repository.BankAccountRepository;
import ptit.savings.repository.ClientRepository;
import ptit.savings.repository.OTPRepository;
import ptit.savings.service.EmailSender;

@Controller
@RequestMapping(path = "/api/client/")
public class ClientRest {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private EmailSender emailSender;

    @PostMapping(path = "/add") 
    public @ResponseBody Client addNewClient(
            // @RequestParam String stk,
            // @RequestParam String username,
            // @RequestParam String password //syntax for postman testing
            @RequestBody addClientBody body // syntax for temporary implementation
    ) {
        List<BankAccount> listb = bankAccountRepository.findByStk(body.getStk());
        if (listb.isEmpty()) {
            return null;
        } else {
            BankAccount bank = listb.get(0);
            Client client = new Client();
            client.setUsername(body.getUsername());
            client.setPassword(body.getPassword());
            client.setStatus(-1);
            client.setBankAccount(bank);
            clientRepository.save(client);
            Random random = new Random();

            OTP otp = new OTP();
            otp.setAction("verify_account");
            otp.setEntity(bank.getStk());
            otp.setStrValue(String.format("%06d", random.nextInt(1000000)));
            otpRepository.save(otp);
            
            emailSender.verifyNewClientEmail(bank.getEmail(), otp.getStrValue());

            return client;
        }
    }

    @PostMapping(path = "/verify")
    public @ResponseBody Client verifyNewClient(@RequestBody verifyNewClientBody body) {
        List<OTP> list = otpRepository.findByEntity(body.getStk());
        if(list.isEmpty()){
            return null;
        }
        for (OTP otp : list) {
            if(otp.getAction().compareTo("verify_account")==0&&otp.getStrValue().compareTo(body.getOtp())==0){
                List<BankAccount> bankl = bankAccountRepository.findByStk(body.getStk());
                if(bankl.isEmpty()){
                    return null;
                }
                BankAccount bank = bankl.get(0);
                List<Client> listClient = clientRepository.findAll();
                for (Client c : listClient) {
                    if(c.getBankAccount().getId() == bank.getId()){
                        c.setStatus(1);
                        clientRepository.save(c);
                        System.out.println("hfsadfsd");
                        return c;

                    }
                }
            }
            else return null;
        }
        return null;
    }

}
