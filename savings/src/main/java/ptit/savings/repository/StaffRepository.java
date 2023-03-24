package ptit.savings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ptit.savings.model.Staff;

public interface StaffRepository extends JpaRepository<Staff,Long>{
    public List<Staff> findByUsername(String username);
    public List<Staff> findByVerified(int verified);
    
}
