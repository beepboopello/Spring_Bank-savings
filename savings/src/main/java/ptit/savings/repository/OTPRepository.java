package ptit.savings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ptit.savings.model.OTP;

public interface OTPRepository extends JpaRepository<OTP,Integer>{
    public List<OTP> findByEntity(String stk);
    
}
