package antifraud.example.antifraud.repo;

import antifraud.example.antifraud.entity.AdminDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDetailsRepo extends JpaRepository<AdminDetails, Long> {
    AdminDetails findByUsername(String username);
}
