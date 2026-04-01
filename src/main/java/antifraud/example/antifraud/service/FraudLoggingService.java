package antifraud.example.antifraud.service;

import antifraud.example.antifraud.dto.FormReq;
import antifraud.example.antifraud.dto.LocationStats;
import antifraud.example.antifraud.entity.UserDetails;
import antifraud.example.antifraud.repo.UserDetailsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FraudLoggingService {
    private final UserDetailsRepo userDetailsRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFraud(FormReq dto, String ip, String reason, LocationStats ipStats, Long  adminId) {
        UserDetails fraudUser = new UserDetails();

        if (userDetailsRepo.existsByUserDeviceId(dto.getDeviceId())) {
            fraudUser = userDetailsRepo.findByUserDeviceId(dto.getDeviceId());
        } else {
            fraudUser.setUserPhone(dto.getPhone());
            fraudUser.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        }

        fraudUser.setUserIp(ip);
        fraudUser.setIsFraud(true);
        fraudUser.setLogMessage(reason);
        fraudUser.setLatitude(dto.getLatitude());
        fraudUser.setLongitude(dto.getLongitude());
        fraudUser.setIpLatitude(ipStats.getLatitude());
        fraudUser.setIpLongitude(ipStats.getLongitude());
        fraudUser.setUserDeviceId(dto.getDeviceId());
        fraudUser.setAdminId(adminId);
        userDetailsRepo.save(fraudUser);
    }
}
