package antifraud.example.antifraud.controller;

import antifraud.example.antifraud.dto.LinkedUserRes;
import antifraud.example.antifraud.dto.LinkedUsersRes;
import antifraud.example.antifraud.entity.LinkedUsers;
import antifraud.example.antifraud.entity.UserDetails;
import antifraud.example.antifraud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDetails>> getAllUsers(
            @RequestParam Boolean isFraud
    ) {
        return userService.all(isFraud);
    }

    @GetMapping("/list/{adminId}")
    public ResponseEntity<List<UserDetails>> getAllUsersByAdminId(
            @PathVariable("adminId") Long adminId,
            @RequestParam Boolean isFraud
    ) {
        return userService.byAdminId(isFraud, adminId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetails> getUserDetails(
            @PathVariable(value = "userId") Long userId) {
        return userService.byUserId(userId);
    }

    @GetMapping("/linked-user/{userId}")
    public ResponseEntity<LinkedUsersRes> getLinkedUser(
            @PathVariable(value = "userId") Long userId){
        return userService.getLinkedUser(userId);
    }

    //todo fraud user lar va non-fraud user lar jami soni
}
