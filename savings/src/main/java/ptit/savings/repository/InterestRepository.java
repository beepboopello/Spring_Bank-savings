package ptit.savings.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ptit.savings.model.Interest;

public interface InterestRepository extends JpaRepository<Interest,Integer>{
    public List<Interest> findByMonths(int months);
}
