package antifraud.example.antifraud.security;

import antifraud.example.antifraud.entity.AdminDetails;
import antifraud.example.antifraud.repo.AdminDetailsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AdminDetailsRepo authRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final AdminDetails auth = authRepo.findByUsername(username);
        return new UsersDetails(auth);
    }
}
