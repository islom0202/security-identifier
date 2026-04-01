package antifraud.example.antifraud.service;

import antifraud.example.antifraud.contraints.ResponseMsg;
import antifraud.example.antifraud.dto.FormReq;
import antifraud.example.antifraud.dto.LocationStats;
import antifraud.example.antifraud.entity.LinkedUsers;
import antifraud.example.antifraud.entity.Links;
import antifraud.example.antifraud.entity.UserDetails;
import antifraud.example.antifraud.repo.LinkRepo;
import antifraud.example.antifraud.repo.LinkUsersRepo;
import antifraud.example.antifraud.repo.UserDetailsRepo;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormService {
    private final LinkRepo linkRepo;
    private final LinkUsersRepo linkUsersRepo;
    private final DatabaseReader databaseReader;
    private final UserDetailsRepo userDetailsRepo;
    private final FraudLoggingService fraudLoggingService;
    private static final String BASE_URI = "localhost:9091/form/";

    @Transactional
    public ResponseEntity<String> submitRegistration(FormReq dto, String ipAddress) {
        Links link = getLink(BASE_URI.concat(dto.getAdminId()));
        LocationStats ipStats = getIpLocation(ipAddress);
        verifyLocationConsistency(dto, ipAddress, ipStats, link.getAdminId());
        // 3. Save User Details
        UserDetails user = new UserDetails();
        user.setUserPhone(dto.getPhone());
        user.setUserIp(ipAddress);
        user.setLatitude(dto.getLatitude());
        user.setLongitude(dto.getLongitude());
        user.setIpLatitude(ipStats.getLatitude());
        user.setIpLongitude(ipStats.getLongitude());
        user.setUserDeviceId(dto.getDeviceId());
        user.setIsFraud(false);
        user.setAdminId(link.getAdminId());
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user = userDetailsRepo.save(user);

        // 4. Link User to the Admin's Link
        LinkedUsers linkRelation = new LinkedUsers();
        linkRelation.setLinkId(link.getId());
        linkRelation.setUserId(user.getId());
        linkRelation.setUserCode(dto.getCode());
        linkRelation.setSentAt(Timestamp.valueOf(LocalDateTime.now()));
        linkRelation.setClickedAt(Timestamp.valueOf(LocalDateTime.now()));
        linkUsersRepo.save(linkRelation);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseMsg.SUCCESS.getMessage());
    }

    private LocationStats getIpLocation(String ip) {
        try {
            CityResponse res = databaseReader.city(InetAddress.getByName(ip));
            return new LocationStats(res.getLocation().getLatitude(), res.getLocation().getLongitude());
        } catch (Exception e) {
            return new LocationStats(0, 0);
        }
    }

    public void verifyLocationConsistency(FormReq dto, String ipAddress, LocationStats ipStats, Long adminId) {
        double distanceBetweenGpsAndIp = calculateDistance(dto.getLatitude(), dto.getLongitude(),
                ipStats.getLatitude(), ipStats.getLongitude());

        boolean isSuspicious = distanceBetweenGpsAndIp >= 100; // Over 10km is flagged

        // 2. Check for Fraud/Duplicates in DB
        if (userDetailsRepo.existsByUserPhone(dto.getPhone())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ResponseMsg.PHONE_ALREADY_REGISTERED.getMessage()
            );
        }

        if (userDetailsRepo.existsByUserDeviceId(dto.getDeviceId())) {
            fraudLoggingService.logFraud(dto, ipAddress, ResponseMsg.DEVICE_DUPLICATED.getMessage(), ipStats, adminId);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    ResponseMsg.DEVICE_DUPLICATED.getMessage()
            );
        }

        if (userDetailsRepo.isAreaOccupied(dto.getLatitude(), dto.getLongitude())) {
            fraudLoggingService.logFraud(dto, ipAddress, ResponseMsg.AREA_ALREADY_OCCUPIED_LOG.getMessage(), ipStats, adminId);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    ResponseMsg.AREA_ALREADY_OCCUPIED.getMessage()
            );
        }

        if (isSuspicious) {
            fraudLoggingService.logFraud(dto, ipAddress, String.format(ResponseMsg.LOCATION_MISMATCH_LOG.getMessage(), distanceBetweenGpsAndIp), ipStats, adminId);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    ResponseMsg.LOCATION_MISMATCH.getMessage()
            );
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in KM
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private Links getLink(String fullLink) {
        Links links = linkRepo.findByGeneratedLink(fullLink);
        if (LocalDateTime.now().isAfter(links.getExpiresAt())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    ResponseMsg.LINK_EXPIRED.getMessage()
            );
        }
        return links;
    }
}
