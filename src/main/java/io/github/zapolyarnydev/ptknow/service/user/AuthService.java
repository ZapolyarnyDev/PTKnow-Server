package io.github.zapolyarnydev.ptknow.service.user;

import io.github.zapolyarnydev.ptknow.dto.auth.LoginDTO;
import io.github.zapolyarnydev.ptknow.dto.auth.RegistrationDTO;
import io.github.zapolyarnydev.ptknow.entity.user.UserEntity;
import io.github.zapolyarnydev.ptknow.exception.email.EmailAlreadyUsedException;
import io.github.zapolyarnydev.ptknow.exception.email.EmailNotFoundException;
import io.github.zapolyarnydev.ptknow.exception.credentials.InvalidCredentialsException;
import io.github.zapolyarnydev.ptknow.repository.auth.UserRepository;
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

    UserRepository repository;
    PasswordEncoder passwordEncoder;
    ProfileService profileService;

    @Transactional
    public UserEntity register(String fullName, String email, String password) {
        if(repository.existsByEmail(email))
            throw new EmailAlreadyUsedException(email);

        String hashedPassword = passwordEncoder.encode(password);

        var entity = UserEntity.builder()
                .email(email)
                .password(hashedPassword)
                .build();

        repository.save(entity);
        profileService.createProfile(fullName, entity);
        log.info("User registered successfully. Full name: {}; Email: {}", fullName, email);
        return entity;
    }

    @Transactional
    public UserEntity register(RegistrationDTO registrationDTO) {
        return register(registrationDTO.fullName(), registrationDTO.email(), registrationDTO.password());
    }

    @Transactional
    public UserEntity authenticate(String email, String password) {
        var entity = loadUserByUsername(email);

        if(!passwordEncoder.matches(password, entity.getPassword()))
            throw new InvalidCredentialsException("Неверный логин или пароль");

        log.info("User log in successfully. Email: {}", email);
        return entity;
    }

    @Transactional
    public UserEntity authenticate(LoginDTO loginDTO) {
        return authenticate(loginDTO.email(), loginDTO.password());
    }

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new EmailNotFoundException(username));
    }

}
