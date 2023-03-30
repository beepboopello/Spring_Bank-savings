package ptit.savings.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;
import ptit.savings.model.Account;
import ptit.savings.model.Staff;
import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.SavingRepository;
import ptit.savings.repository.StaffRepository;
import ptit.savings.tools.Token;

@Controller
public class StaffController {
    @Autowired
    private StaffRepository staffRepo;
    @Autowired
    private InterestRepository interestRepo;
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private SavingRepository savingRepo;

    @GetMapping("/")
    public ModelAndView getLogin() {
        ModelAndView loginPage = new ModelAndView("login");
        String token = Token.generateToken();
        loginPage.addObject("token", token);
        return loginPage;
    }

    @RequestMapping(path = "/register")
    public String register(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("staff");
        if (staff == null)
            return "register";
        else if (staff.getIsAdmin() == 0)
            return "redirect:/dashboard";
        else return "redirect:/admin";
    }

    @RequestMapping(path = "/logout")
    public String logout(HttpSession session, Model model) {
        session.removeAttribute("staff");
        session.removeAttribute("token");
        return "redirect:/";
    }

    @GetMapping("/addSaving")
    public String addSaving(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("staff");
        if (staff == null)
            return "redirect:/";
        else if (staff.getIsAdmin() == 1)
            return "redirect:/admin";
        else {
            model.addAttribute("interestList", interestRepo.findAll(Sort.by(Sort.Direction.ASC, "months")));
            model.addAttribute("accountList", accountRepo.findAll());
            return "addSaving";
        }
    }

    @GetMapping("/findstk")
    public String findstk(HttpSession session, Model model, @RequestParam String stk) {
        Staff staff = (Staff) session.getAttribute("staff");
        if (staff == null)
            return "redirect:/";
        else if (staff.getIsAdmin() == 1)
            return "redirect:/admin";
        else {
            System.out.println(stk);
            model.addAttribute("interestList", interestRepo.findAll(Sort.by(Sort.Direction.ASC, "months")));
            model.addAttribute("accountList", accountRepo.findByStk(stk));
            return "addSaving";
        }
    }


    @GetMapping("/searchSaving")
    public String searchSaving(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("staff");
        if (staff == null)
            return "redirect:/";
        else if (staff.getIsAdmin() == 1)
            return "redirect:/admin";

        else {
//            model.addAttribute("savingList", savingRepo.findAll());
            return "searchSaving";
        }
    }
    @GetMapping("/findSavingBook")
    public String findSavingBook(HttpSession session, Model model, @RequestParam String number) {
        Staff staff = (Staff) session.getAttribute("staff");
        if (staff == null)
            return "redirect:/";
        else if (staff.getIsAdmin() == 1)
            return "redirect:/admin";
        else {
            System.out.println(number);
            model.addAttribute("savingList", savingRepo.findByNumber(number));
            return "searchSaving";
        }
    }

    @GetMapping("/calculate")
    public String calculate(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("staff");
        if (staff == null)
            return "redirect:/";
        else if (staff.getIsAdmin() == 1)
            return "redirect:/admin";
        else {
            model.addAttribute("interestList", interestRepo.findAll(Sort.by(Sort.Direction.ASC, "months")));
            return "calculate";
        }
    }

    @GetMapping("/admin")
    public String getAdmin(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("staff");
        if (staff == null)
            return "redirect:/";
        else if (staff.getIsAdmin() == 0)
            return "redirect:/dashboard";
        model.addAttribute("num", Double.valueOf("2.12"));
        model.addAttribute("unverifiedList", staffRepo.findByVerified(0));
        model.addAttribute("interestList", interestRepo.findAll(Sort.by(Sort.Direction.ASC, "months")));
        return "admin/admin-dashboard";
    }

    @GetMapping("/dashboard")
    public String getDashboard(HttpSession session, Model model) {
        Staff staff = (Staff) session.getAttribute("staff");
        if (staff == null)
            return "redirect:/";
        else if (staff.getIsAdmin() == 1)
            return "redirect:/admin";
        return "dashboard";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam("token") String token,
                        Model model,
                        HttpSession session) {
        List<Staff> list = staffRepo.findByUsername(username);
        if (list.isEmpty()) {
            model.addAttribute("error", "User not found");
            return "blank";
        }
        Staff staff = list.get(0);
        if (staff.getVerified() != 1) {
            model.addAttribute("error", "User is not verified, please contact your administrator for support");
            return "blank";
        } else if (!BCrypt.checkpw(password, staff.getPassword())) {
            model.addAttribute("error", "Invalid password");
            return "blank";
        }
        session.setAttribute("staff", staff);
        session.setAttribute("token", token);
        if (staff.getIsAdmin() == 1) {
            return "redirect:/admin";
        }
        return "redirect:/dashboard";
    }


    @GetMapping("/register")
    public ModelAndView getRegister() {
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
