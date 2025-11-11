package io.github.zapolyarnydev.ptknow.api.v0.user;

import io.github.zapolyarnydev.ptknow.api.v0.ApiResponse;
import io.github.zapolyarnydev.ptknow.dto.auth.LoginDTO;
import io.github.zapolyarnydev.ptknow.dto.auth.RegistrationDTO;
import io.github.zapolyarnydev.ptknow.entity.auth.AuthEntity;
import io.github.zapolyarnydev.ptknow.jwt.JwtTokens;
import io.github.zapolyarnydev.ptknow.service.auth.AuthService;
import io.github.zapolyarnydev.ptknow.service.auth.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v0/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegistrationDTO registrationDTO) {
        AuthEntity entity = authService.register(registrationDTO);
        JwtTokens tokens = jwtService.generateTokenPair(entity);

        var response = ApiResponse.success("Регистрация прошла успешно", tokens.accessToken());
        ResponseCookie cookie = jwtService.tokenToCookie("/v0/token/refresh", tokens.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginDTO loginDTO) {
        AuthEntity entity = authService.authenticate(loginDTO);
        JwtTokens tokens = jwtService.generateTokenPair(entity);

        var response = ApiResponse.success("Вход прошёл успешно", tokens.accessToken());
        ResponseCookie cookie = jwtService.tokenToCookie("/v0/token/refresh", tokens.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal AuthEntity user) {
        jwtService.invalidateUserTokens(user);

        var response = ApiResponse.success("Вы успешно вышли из системы");
        ResponseCookie cookie = jwtService.tokenToCookie("/v0/token/refresh", "");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

}
