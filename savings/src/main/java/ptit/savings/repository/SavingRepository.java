package ptit.savings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ptit.savings.model.Saving;

public interface SavingRepository extends JpaRepository<Saving,Long>{
    public  List<Saving> findByNumber(String number);
}
