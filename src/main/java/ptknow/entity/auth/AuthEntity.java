package ptknow.entity.auth;

import ptknow.entity.course.CourseEntity;
import ptknow.entity.profile.ProfileEntity;
import ptknow.exception.credentials.InvalidCredentialsException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "auth_data")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Getter
    UUID id;

    @Column(updatable = false, unique = true)
    @Getter
    String email;

    @Setter
    @Getter
    String password;

    @Column(nullable = false, updatable = false)
    @Getter
    Instant registeredAt;

    @Enumerated(EnumType.STRING)
    @Getter
    @Column(nullable = false)
    Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    @Getter
    AuthProvider authProvider;

    @Column(updatable = false, unique = true)
    @Getter
    String providerId;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Getter
    private ProfileEntity profile;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    Set<CourseEntity> ownedCourses = new HashSet<>();

    @ManyToMany(mappedBy = "editors", fetch = FetchType.LAZY)
    Set<CourseEntity> editCourses = new HashSet<>();

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
            role = Role.GUEST;

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

    public Set<CourseEntity> getOwnedCourses() {
        return Collections.unmodifiableSet(ownedCourses);
    }

    public boolean addOwnedCourse(CourseEntity e) {
       return ownedCourses.add(e);
    }

    public boolean removeOwnedCourse(CourseEntity e) {
       return ownedCourses.remove(e);
    }

    public Set<CourseEntity> getEditCourses() {
        return Collections.unmodifiableSet(editCourses);
    }

    public boolean addEditCourse(CourseEntity e) {
        return editCourses.add(e);
    }

    public boolean removeEditCourse(CourseEntity e) {
        return editCourses.remove(e);
    }
}
