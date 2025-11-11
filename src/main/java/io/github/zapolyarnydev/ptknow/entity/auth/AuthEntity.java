package io.github.zapolyarnydev.ptknow.entity.auth;

import io.github.zapolyarnydev.ptknow.entity.profile.ProfileEntity;
import io.github.zapolyarnydev.ptknow.exception.credentials.InvalidCredentialsException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "auth_data")
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(updatable = false, unique = true)
    String email;

    @Setter
    String password;

    @Column(nullable = false, updatable = false)
    Instant registeredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    AuthProvider authProvider;

    @Column(updatable = false, unique = true)
    String providerId;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private ProfileEntity profile;

    @Builder
    public AuthEntity(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.authProvider = AuthProvider.LOCAL;
    }

    @Builder(builderMethodName = "provideVK")
    public AuthEntity(String providerId, Role role) {
        this.role = role;
        this.authProvider = AuthProvider.VK;
        this.providerId = providerId;
    }

    @PrePersist
    @PreUpdate
    public void validateFields() {
        if(registeredAt == null)
            registeredAt = Instant.now();

        if(role == null)
            role = Role.USER;

        checkProvidingCredentials();
    }

    private void checkProvidingCredentials() {
        if(authProvider == AuthProvider.LOCAL){
            if(email == null || password == null)
                throw new InvalidCredentialsException("Local auth provider require email and password");
            if(email.isBlank() || password.isBlank())
                    throw new InvalidCredentialsException("Email and password cannot be empty in local auth provider");
        }
        else if(authProvider == AuthProvider.VK && providerId == null)
            throw new InvalidCredentialsException("VK auth provider require providerId");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(role.authorityName())
        );
    }

    @Override
    public String getUsername() {
        return email != null ? email : (authProvider.name().toLowerCase() + ":" + providerId);
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
}
