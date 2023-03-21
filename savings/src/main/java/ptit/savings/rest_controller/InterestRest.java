package ptit.savings.rest_controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ptit.savings.model.requestBody.Interest.DeleteBody;
import ptit.savings.repository.InterestRepository;

@RestController
public class InterestRest {
    @Autowired
    private InterestRepository repo;

    @PostMapping("/api/admin/interest/delete")
    public ResponseEntity<Object> delete(
        @RequestBody @Valid DeleteBody body, BindingResult bindingResult // Body gom id lai suat can xoa
    ){             

        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();
        
        // kiem tra token tu body

        //
        if(repo.findById(body.getId()).isEmpty()){
            error.put("error", "Interest id doesn't exist");
            response.put("error",error);
            return new ResponseEntity<Object>(response, HttpStatus.FORBIDDEN);
        }
        repo.deleteById(body.getId());
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping("/api/admin/interest/edit")
    public ResponseEntity<Object> edit(
        // @RequestBody @Valid EditBody body, BindingResult bindingResult   //Body gom id lai suat can sua, ten, so thang, lai suat(theo %), token
    ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        // if(bindingResult.hasErrors()){
        // response.put("error", error);
        // }

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping("/api/admin/interest/add")
    public ResponseEntity<Object> add(
        // @RequestBody @Valid AddBody body, BindingResult bindingResult    //Body gom ten, so thang, lai suat(theo %), token
    ){
        HashMap<String,Object> response = new HashMap<>();
        HashMap<String,Object> error = new HashMap<>();

        // if(bindingResult.hasErrors()){
        // response.put("error", error);
        // }

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
}
