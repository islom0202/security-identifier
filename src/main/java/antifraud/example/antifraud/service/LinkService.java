package antifraud.example.antifraud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {
    public ResponseEntity<String> findByLink(String subLink) {
        return null;
    }
}
