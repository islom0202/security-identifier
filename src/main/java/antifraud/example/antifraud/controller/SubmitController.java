package antifraud.example.antifraud.controller;

import antifraud.example.antifraud.dto.FormReq;
import antifraud.example.antifraud.service.FormService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/form")
@RequiredArgsConstructor
public class SubmitController {

    private final FormService formService;

    @PostMapping("/submit")
    @ResponseBody
    public ResponseEntity<String> handleSubmit(@RequestBody FormReq form, HttpServletRequest request){
        log.debug(form.toString());
        // 1. Get IP from Headers (Handle Proxies/Load Balancers)
        String remoteAddr = request.getHeader("X-Forwarded-For");
        if (remoteAddr == null) {
            remoteAddr = request.getRemoteAddr();
        }

        if ("0:0:0:0:0:0:0:1".equals(remoteAddr) || "::1".equals(remoteAddr)) {
            remoteAddr = "213.230.64.1";
        }

        log.debug(remoteAddr);
        return formService.submitRegistration(form, remoteAddr);
    }
}
