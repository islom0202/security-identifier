package antifraud.example.antifraud.service;

import antifraud.example.antifraud.dto.AdminLinks;
import antifraud.example.antifraud.dto.LoginReq;
import antifraud.example.antifraud.dto.SaveAdminReq;
import antifraud.example.antifraud.entity.AdminDetails;
import antifraud.example.antifraud.entity.Links;
import antifraud.example.antifraud.repo.AdminDetailsRepo;
import antifraud.example.antifraud.repo.LinkRepo;
import antifraud.example.antifraud.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final JwtUtil jwtUtil;
    private final LinkRepo linkRepo;
    private final PasswordEncoder passwordEncoder;
    private final AdminDetailsRepo adminDetailsRepo;
    private final AuthenticationManager authenticationManager;
    private static final String BASE_URI = "localhost:9091/form/";

    public ResponseEntity<String> createAdmin(SaveAdminReq req) {
        adminDetailsRepo.save(AdminDetails.builder()
                .fullname(req.fullname())
                .username(req.phone())
                .password(passwordEncoder.encode(req.password()))
                .role(req.role())
                .createdAt(LocalDateTime.now())
                .build());
        return ResponseEntity.ok("yaratildi");
    }

    public ResponseEntity<String> login(LoginReq req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AdminDetails admin = adminDetailsRepo.findByUsername(req.username());
        return admin != null
                ? ResponseEntity.ok(jwtUtil.generateToken(userDetails, admin))
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("username yoki parol no`tog`ri");
    }

    public ResponseEntity<Links> createLink(Long adminId, Integer expireTime) {
        String subLink = createSubLink();
        Links link = linkRepo.save(Links.builder()
                .adminId(adminId)
                .generatedLink(BASE_URI.concat(subLink))
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(expireTime))
                .isExpired(false)
                .build());
        return ResponseEntity.ok(link);
    }

    private String createSubLink() {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[20];
        random.nextBytes(randomBytes);
        return Base64.getUrlEncoder().encodeToString(randomBytes);
    }

    public ResponseEntity<List<AdminDetails>> adminsList() {
        return ResponseEntity.ok(adminDetailsRepo.findAll());
    }

    public ResponseEntity<List<AdminLinks>> linkList() {
        List<AdminLinks> adminLinks = linkRepo.findLinksAll()
                .stream()
                .sorted(Comparator.comparing(AdminLinks::getCreatedAt).reversed())
                .toList();
        filterLinks(adminLinks);
        return ResponseEntity.ok(adminLinks);
    }

    public ResponseEntity<List<AdminLinks>> linksByAdminId(Long adminId) {
        List<AdminLinks> adminLinks = linkRepo.findLinksAllByAdminId(adminId)
                .stream()
                .sorted(Comparator.comparing(AdminLinks::getCreatedAt).reversed())
                .toList();
        filterLinks(adminLinks);
        return ResponseEntity.ok(adminLinks);
    }

    public void filterLinks(List<AdminLinks> adminLinks) {
        List<Long> expired = new ArrayList<>();
        adminLinks.forEach(v1 -> {
            if (LocalDateTime.now().isAfter(v1.getExpiresAt().toLocalDateTime())) {
                v1.setIsExpired(TRUE);
                expired.add(v1.getId());
            }
        });

        if (!expired.isEmpty())
            linkRepo.updateLinkStatus(expired);
    }

    public ResponseEntity<AdminDetails> adminById(Long adminId) {
        return ResponseEntity.ok(adminDetailsRepo.findById(adminId).get());
    }

    public ResponseEntity<Integer> activeLinksNum() {
        return ResponseEntity.ok(linkRepo.countByIsExpired());
    }

    public ResponseEntity<String> delete(Long adminId) {
        adminDetailsRepo.deleteById(adminId);
        return ResponseEntity.ok("o`chirildi");
    }

    public ResponseEntity<String> makeActive(Long adminId, Boolean makeActive) {
        AdminDetails adminDetail = adminDetailsRepo.findById(adminId).get();
        adminDetail.setIsActive(makeActive);
        adminDetailsRepo.save(adminDetail);
        return ResponseEntity.ok("Yangilandi!");
    }
}
