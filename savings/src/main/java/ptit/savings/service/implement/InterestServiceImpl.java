package ptit.savings.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.savings.model.Interest;
import ptit.savings.repository.InterestRepository;
import ptit.savings.service.InterestService;

import java.util.List;
@Service
public class InterestServiceImpl implements InterestService {

    @Autowired
    private InterestRepository interestRepo;

    @Override
    public List<Interest> getAllInterests() {
        return null;
    }

    @Override
    public Interest getInterestById(Long id) {
        return interestRepo.findById(id).orElse(null);
    }

    @Override
    public void addInterest(Interest interest) {
        interestRepo.save(interest);
    }

    @Override
    public void updateInterest(Interest interest) {
        interestRepo.save(interest);
    }

    @Override
        public void deleteInterest(Long id) {
        interestRepo.deleteById(id);
    }
}
