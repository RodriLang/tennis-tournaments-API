package dev.rodrilang.tennis_tournaments.security;

import dev.rodrilang.tennis_tournaments.models.Credential;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
public class CredentialDetails implements UserDetails {

    private final Credential credential;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(credential.getRole().name()));
    }

    @Override
    public String getPassword() {
        return credential.getPassword();
    }

    @Override
    public String getUsername() {
        return credential.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Credential getCredential() {
        return credential;
    }
}

