package ptit.savings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ptit.savings.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{
    public List<Account> findByStk(String stk);
}
