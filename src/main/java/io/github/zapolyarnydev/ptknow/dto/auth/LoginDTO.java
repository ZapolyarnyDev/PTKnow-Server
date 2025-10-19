package io.github.zapolyarnydev.ptknow.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record LoginDTO(
        @NotNull(message = "Email обязателен")
        @NotBlank(message = "Email не должен быть пустым")
        @Email(message = "Данные не являются электронной почтой")
        String email,

        @NotNull(message = "Пароль обязателен")
        @Length(min = 8, max = 32, message = "Длина пароля от 8 до 32 символов")
        String password
) {}