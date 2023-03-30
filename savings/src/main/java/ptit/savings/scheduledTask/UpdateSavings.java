package ptit.savings.scheduledTask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ptit.savings.repository.SavingRepository;
import ptit.savings.model.Saving;

@Component
public class UpdateSavings {
    @Autowired SavingRepository savingRepo;
    // @Scheduled(cron = "0 0 */1 * * *")
    @Scheduled(cron = "0 * * * * MON-FRI")
    public void hourlySavingUpdate(){
        List<Saving> list = savingRepo.findAll();
        for(Saving s:list){
            s.hourlyUpdate();
        }
    }
}
