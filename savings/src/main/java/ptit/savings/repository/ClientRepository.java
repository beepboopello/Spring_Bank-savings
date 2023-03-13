package ptit.savings.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ptit.savings.model.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    public List<Client> findByUsername(String username);
}