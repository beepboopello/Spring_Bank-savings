package ptit.savings.rest_controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ptit.savings.model.Staff;
import ptit.savings.model.requestBody.Staff.StaffRegisterBody;
import ptit.savings.model.requestBody.Staff.StaffVerifyBody;
import ptit.savings.repository.StaffRepository;
import ptit.savings.tools.JsonUtil;

@RestController
public class StaffRest {

    @Autowired
    private StaffRepository repository;

    @PostMapping("/api/register")
    public ResponseEntity<Object> register(@RequestBody @Valid StaffRegisterBody body, BindingResult bindingResult) {
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> error = new HashMap<>();
        if (bindingResult.hasErrors()) {
            error = new HashMap<>();
            for (Object object : bindingResult.getAllErrors()) {
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;
                    error.put(fieldError.getField().toString(), fieldError.getDefaultMessage());
                }
            }
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        }
        if (!repository.findByUsername(body.getUsername()).isEmpty()) {
            error.put("username", "Duplicate username");
            response.put("error", error);
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        }
        Staff newStaff = new Staff(body.getFirstName(),
                body.getLastName(),
                body.getEmail(),
                body.getUsername(),
                BCrypt.hashpw(body.getPassword(), BCrypt.gensalt()));
        newStaff.setVerified(0);
        newStaff.setCreated_at(LocalDateTime.now());
        newStaff.setUpdated_at(newStaff.getCreated_at());
        repository.save(newStaff);
        response.put("staff", newStaff);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping("/api/admin/verify")
    public ResponseEntity<Object> verify(
            @RequestBody @Valid StaffVerifyBody body, BindingResult bindingResult      //Body gom id staff can xac minh, token
    ) {
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> error = new HashMap<>();
         if(bindingResult.hasErrors()){
             error = new HashMap<>();
             for (Object object : bindingResult.getAllErrors()) {
                 if(object instanceof FieldError) {
                     FieldError fieldError = (FieldError) object;
                     error.put(fieldError.getField().toString(), fieldError.getDefaultMessage());
                 }
             }
             response.put("error",error);
             return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
         }

        Staff staff = repository.findById(body.getId()).orElse(null);
        if (staff == null) {
            response.put("error", "Couldn't find employee with id: " + body.getId());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if(staff.getVerified() == 1){
            response.put("error", "Employee with id: " + body.getId() + " has already been verified");
        }else{
            staff.setVerified(1);
            staff.setVerified_at(LocalDateTime.now());
            repository.save(staff);
            response.put("staff", staff);
        }
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }


}
