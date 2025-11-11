package io.github.zapolyarnydev.ptknow.api.v0.user;

import io.github.zapolyarnydev.ptknow.api.v0.ApiResponse;
import io.github.zapolyarnydev.ptknow.jwt.JwtTokens;
import io.github.zapolyarnydev.ptknow.service.auth.JwtService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v0/token")
@RequiredArgsConstructor
public class TokenController {

    private final JwtService jwtService;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(
            @CookieValue("refreshToken") @NotBlank(message = "Отсутствует refresh token") String refreshToken) {
        JwtTokens tokens = jwtService.refresh(refreshToken);

        var response = ApiResponse.success("Токен успешно обновлён", tokens.accessToken());
        ResponseCookie cookie = jwtService.tokenToCookie("/v0/token/refresh", tokens.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
}
