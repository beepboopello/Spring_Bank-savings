package ptit.savings.tools;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import ptit.savings.model.Interest;
import ptit.savings.model.Staff;
import ptit.savings.repository.InterestRepository;
import ptit.savings.repository.StaffRepository;

@Component
public class Dataloader implements ApplicationRunner{

    @Autowired
    private StaffRepository staffRepo;

    @Autowired
    private InterestRepository interestRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(staffRepo.findByUsername("admin").isEmpty()){
            Staff admin = new Staff("Admin", "Super User", "ptitbankapp@gmail.com", "admin", BCrypt.hashpw("admin", BCrypt.gensalt()));
            admin.setIsAdmin(1);
            admin.setVerified(1);
            admin.setCreated_at(LocalDateTime.now());
            admin.setUpdated_at(admin.getCreated_at());
            admin.setVerified_at(admin.getCreated_at());
            staffRepo.save(admin);
        }

        if(interestRepo.findByMonths(12) == null){
            Interest in = new Interest();
            in.setName("12 thang");
            in.setMonths(12);
            in.setRate(0.03);
            interestRepo.save(in);
        }

        if(interestRepo.findByMonths(0) == null){
            Interest in = new Interest();
            in.setName("Không kỳ hạn");
            in.setMonths(0);
            in.setRate(0.003);
            interestRepo.save(in);
        }
        
        
    }
    
}
