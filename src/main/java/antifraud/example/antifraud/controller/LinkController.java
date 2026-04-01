package antifraud.example.antifraud.controller;

import antifraud.example.antifraud.dto.AdminLinks;
import antifraud.example.antifraud.dto.LinkedUserRes;
import antifraud.example.antifraud.dto.UsersList;
import antifraud.example.antifraud.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/link")
@RequiredArgsConstructor
public class LinkController {
    private final UserService userService;

    @GetMapping("/users/{linkId}")
    public ResponseEntity<List<UsersList>> getLinkedUsers(
            @PathVariable(value = "linkId") Long linkId){
        return userService.byLinkId(linkId);
    }

    @GetMapping("/list/{adminId}")
    public ResponseEntity<List<AdminLinks>> allLinksByAdminId(
            @PathVariable(value = "adminId") Long adminId){
        return userService.allLinksAdminId(adminId);
    }
}
