package antifraud.example.antifraud.service;

import antifraud.example.antifraud.dto.AdminLinks;
import antifraud.example.antifraud.dto.LinkedUserRes;
import antifraud.example.antifraud.dto.LinkedUsersRes;
import antifraud.example.antifraud.dto.UsersList;
import antifraud.example.antifraud.entity.AdminDetails;
import antifraud.example.antifraud.entity.LinkedUsers;
import antifraud.example.antifraud.entity.UserDetails;
import antifraud.example.antifraud.repo.AdminDetailsRepo;
import antifraud.example.antifraud.repo.LinkRepo;
import antifraud.example.antifraud.repo.LinkedUsersRepo;
import antifraud.example.antifraud.repo.UserDetailsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final LinkRepo linkRepo;
    private final AdminService adminService;
    private final UserDetailsRepo userDetailsRepo;
    private final LinkedUsersRepo linkedUsersRepo;
    private final AdminDetailsRepo adminDetailsRepo;

    public ResponseEntity<List<UserDetails>> all(Boolean isFraud) {
        return isFraud == null
                ? ResponseEntity.ok(userDetailsRepo.findAll())
                : ResponseEntity.ok(userDetailsRepo.findByIsFraud(isFraud));
    }

    public ResponseEntity<UserDetails> byUserId(Long userId) {
        return ResponseEntity.ok(
                userDetailsRepo.findById(userId)
                        .orElseThrow(() -> new RuntimeException("user id not found!")));
    }

    public ResponseEntity<List<UsersList>> byLinkId(Long linkId) {
        return ResponseEntity.ok(linkedUsersRepo.findLinksAll(linkId));
    }

    public ResponseEntity<LinkedUsersRes> getLinkedUser(Long userId) {
        return ResponseEntity.ok(linkedUsersRepo.findUser(userId));
    }

    public ResponseEntity<List<UserDetails>> byAdminId(Boolean isFraud, Long adminId) {
        return ResponseEntity.ok(userDetailsRepo.findByAdminIdAndIsFraud(adminId, isFraud));
    }

    public ResponseEntity<List<AdminLinks>> allLinksAdminId(Long adminId) {
        AdminDetails admin = adminDetailsRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("admin id not found!"));
        if (admin.getRole().equals("super_admin"))
            return adminService.linkList();
        else
            return adminService.linksByAdminId(adminId);
    }
}
