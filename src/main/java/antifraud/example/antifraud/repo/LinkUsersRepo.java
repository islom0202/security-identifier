package antifraud.example.antifraud.repo;

import antifraud.example.antifraud.entity.LinkedUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkUsersRepo extends JpaRepository<LinkedUsers, Long> {
}
