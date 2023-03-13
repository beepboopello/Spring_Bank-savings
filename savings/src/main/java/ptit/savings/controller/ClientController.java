package ptit.savings.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ptit.savings.model.BankAccount;
import jakarta.servlet.http.HttpSession;
import ptit.savings.model.Client;
import ptit.savings.repository.BankAccountRepository;
import ptit.savings.repository.ClientRepository;
import ptit.savings.rest_controller.BankAccountRest;

@Controller
public class ClientController{
    @Autowired
    BankAccountRepository bankAccountRepository;
    @Autowired
    ClientRepository clientRepository;
    @RequestMapping(path = "/")
    public String login(){
        // BankAccount bank = new BankAccount();
        // bank.setStk("123618238918");
        // bank.setCccd("haf17341");
        // bank.setOwner("khang");
        // bank.setBalance(Long.parseLong("12645764"));
        // bank.setEmail("ainchasetema@gmail.com");
        // bankAccountRepository.save(bank);
        return "login";
    }

    @RequestMapping(path = "/register")
    public String register(){
        return "register";
    }

    @PostMapping(path = "/login")
    public String checkLogin(ModelMap map,@RequestParam String username, @RequestParam String password,HttpSession session){
        List<Client> list = clientRepository.findByUsername(username);
        if(list.isEmpty()||list.get(0).getPassword().compareTo(password)!=0){
            map.addAttribute("error","Sai tên tài khoản hoặc mật khẩu!");
            return "login";
        }
        else{
            session.setAttribute("user", list.get(0));
            return "blank";
        }

    }
}