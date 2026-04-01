package antifraud.example.antifraud.repo;

import antifraud.example.antifraud.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetails, Long> {
    Boolean existsByUserPhoneOrUserDeviceId(
            String userPhone, String userDeviceId);

    List<UserDetails> findByIsFraud(Boolean isFraud);

    boolean existsByUserPhone(String phone);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM user_details u " +
            "WHERE (6371000 * acos(cos(radians(:lat)) * cos(radians(u.latitude)) * " +
            "cos(radians(u.longitude) - radians(:lon)) + " +
            "sin(radians(:lat)) * sin(radians(u.latitude)))) <= 500)",
            nativeQuery = true)
    boolean isAreaOccupied(@Param("lat") double lat, @Param("lon") double lon);

    boolean existsByUserDeviceId(String deviceId);

    UserDetails findByUserDeviceId(String userDeviceId);

    List<UserDetails> findByAdminIdAndIsFraud(Long adminId, Boolean isExpired);
}
