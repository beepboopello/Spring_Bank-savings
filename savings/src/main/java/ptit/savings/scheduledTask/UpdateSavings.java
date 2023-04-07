package ptit.savings.scheduledTask;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ptit.savings.repository.AccountRepository;
import ptit.savings.repository.OTPRepository;
import ptit.savings.repository.SavingRepository;
import ptit.savings.model.Account;
import ptit.savings.model.OTP;
import ptit.savings.model.Saving;

@Component
public class UpdateSavings {
    @Autowired AccountRepository accountRepo;
    @Autowired SavingRepository savingRepo;
    @Autowired OTPRepository otpRepo;
    // @Scheduled(cron = "0 0 */1 * * *")
    @Scheduled(cron = "0 * * * * MON-FRI")
    public void hourlySavingUpdate(){
        List<Saving> list = savingRepo.findAll();
        for(Saving s:list){
            if(s.getStatus() == 1) {
                s.hourlyUpdate();
                savingRepo.save(s);
            }
        }
    }

    @Scheduled(fixedDelay=5000)
    public void updateOTP(){
        List<OTP> list = otpRepo.findAll();
        for(OTP otp:list){
            if(otp.getExpired_at().isBefore(LocalDateTime.now())){
                switch (otp.getAction()) {
                    case "verify account":
                        Account account = accountRepo.findByStk(otp.getAccount()).get(0);
                        accountRepo.delete(account);
                        otpRepo.delete(otp);
                        break;
                    case "verify saving":
                        Saving saving = savingRepo.findByNumber(otp.getAccount()).get(0);
                        savingRepo.delete(saving);
                        otpRepo.delete(otp);
                        break;
                    default:
                        otpRepo.delete(otp);
                        break;
                }
            }
        }
    }
}
