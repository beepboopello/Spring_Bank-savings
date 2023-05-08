package ptit.savings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ptit.savings.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
    public List<Account> findByStk(String stk);

    Account findByCccd(String cccd);
//    public String findByStk(String stk);
}
