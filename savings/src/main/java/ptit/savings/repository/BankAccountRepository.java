package ptit.savings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ptit.savings.model.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer>{
    public List<BankAccount> findByStk(String stk);
}
