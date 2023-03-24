package ptit.savings.service;

import ptit.savings.model.Interest;

import java.util.List;

public interface InterestService {
    List<Interest> getAllInterests();
    Interest getInterestById(Long id);
    void addInterest(Interest interest);
    void updateInterest(Interest interest);
    void deleteInterest(int id);
}
