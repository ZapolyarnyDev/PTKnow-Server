package ptknow.service.auth;

import ptknow.dto.auth.LoginDTO;
import ptknow.dto.auth.RegistrationDTO;
import ptknow.entity.auth.AuthEntity;
import ptknow.exception.email.EmailAlreadyUsedException;
import ptknow.exception.email.EmailNotFoundException;
import ptknow.exception.credentials.InvalidCredentialsException;
import ptknow.repository.auth.AuthRepository;
import ptknow.service.profile.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService implements UserDetailsService {

    AuthRepository repository;
    PasswordEncoder passwordEncoder;
    ProfileService profileService;

    @Transactional
    public AuthEntity register(String fullName, String email, String password) {
        if(repository.existsByEmail(email))
            throw new EmailAlreadyUsedException(email);

        String hashedPassword = passwordEncoder.encode(password);

        var entity = AuthEntity.builder()
                .email(email)
                .password(hashedPassword)
                .build();

        repository.save(entity);
        profileService.createProfile(fullName, entity);
        log.info("User registered successfully. Full name: {}; Email: {}", fullName, email);
        return entity;
    }

    @Transactional
    public AuthEntity register(RegistrationDTO registrationDTO) {
        return register(registrationDTO.fullName(), registrationDTO.email(), registrationDTO.password());
    }

    @Transactional
    public AuthEntity authenticate(String email, String password) {
        var entity = loadUserByUsername(email);

        if(!passwordEncoder.matches(password, entity.getPassword()))
            throw new InvalidCredentialsException("Неверный логин или пароль");

        log.info("User log in successfully. Email: {}", email);
        return entity;
    }

    @Transactional
    public AuthEntity authenticate(LoginDTO loginDTO) {
        return authenticate(loginDTO.email(), loginDTO.password());
    }

    @Override
    public AuthEntity loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email));
    }

}
