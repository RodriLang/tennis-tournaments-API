package dev.rodrilang.tennis_tournaments.security;

import dev.rodrilang.tennis_tournaments.models.Credential;
import dev.rodrilang.tennis_tournaments.repositories.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialDetailsService implements UserDetailsService {

    private final CredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credential credential = credentialRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found: " + username));
        return new CredentialDetails(credential);
    }
}
