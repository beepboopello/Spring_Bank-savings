package ptit.savings.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import ptit.savings.model.Staff;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.StaffRepository;
import ptit.savings.tools.Token;

@Controller
public class StaffController {
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private InterestRepository interestRepo;

    @GetMapping("/")
    public ModelAndView getLogin(){
        ModelAndView loginPage = new ModelAndView("login");
        String token = Token.generateToken();
        loginPage.addObject("token", token);
        return loginPage;
    }

    @RequestMapping(path = "/register")
    public String register(){
        return "register";
    }

    @GetMapping("/admin")
    public String getAdmin(HttpSession session, Model model){
        Staff staff = (Staff) session.getAttribute("staff");
        if(staff==null)
            return "redirect:/";
        else if(staff.getIsAdmin()==0) 
            return "redirect:/dashboard";
        model.addAttribute("num", Double.valueOf("2.12"));
        model.addAttribute("unverifiedList",staffRepo.findByVerified(0));
        model.addAttribute("interestList", interestRepo.findAll());
        return "admin/admin-dashboard";
    }

    @GetMapping("/dashboard")
    public String getDashboard(HttpSession session, Model model){
        Staff staff = (Staff) session.getAttribute("staff");
        if(staff==null)
            return "redirect:/";
        else if(staff.getIsAdmin()==1) 
            return "redirect:/admin";  
        return "dashboard";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("token") String token,
                        Model model,
                        HttpSession session){
        List<Staff> list = staffRepo.findByUsername(username);
        if(list.isEmpty()){
            model.addAttribute("error", "User not found");
            return "blank";
        }
        Staff staff = list.get(0);
        if (staff.getVerified()!=1){
            model.addAttribute("error", "User is not verified, please contact your administrator for support");
            return "blank";
        }
        else if(!BCrypt.checkpw(password, staff.getPassword())){
            model.addAttribute("error", "Invalid password");
            return "blank";
        }
        session.setAttribute("staff", staff);
        session.setAttribute("token", token);
        if(staff.getIsAdmin()==1){
            return "redirect:/admin";
        }
        return "redirect:/dashboard";
    }


    @GetMapping("/register")
    public ModelAndView getRegister(){
        ModelAndView page = new ModelAndView("register");
        return page;
    }

    // @PostMapping("/register")
    // public ResponseEntity<Object> register(){
    //     HashMap<String,Object> response = new HashMap<>();
    //     response.put("name", "true");
    //     return new ResponseEntity<Object>(response, HttpStatus.OK);
    // }
}
