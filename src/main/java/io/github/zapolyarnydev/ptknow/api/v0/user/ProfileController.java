package io.github.zapolyarnydev.ptknow.api.v0.user;

import io.github.zapolyarnydev.ptknow.api.v0.ApiResponse;
import io.github.zapolyarnydev.ptknow.dto.profile.ProfileResponseDTO;
import io.github.zapolyarnydev.ptknow.dto.profile.ProfileUpdateDTO;
import io.github.zapolyarnydev.ptknow.entity.user.ProfileEntity;
import io.github.zapolyarnydev.ptknow.entity.user.UserEntity;
import io.github.zapolyarnydev.ptknow.mapper.ProfileMapper;
import io.github.zapolyarnydev.ptknow.service.user.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v0/profile")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {

    ProfileService profileService;
    ProfileMapper profileMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> getMyProfile(@AuthenticationPrincipal UserEntity user) {
        var profile = profileService.getProfile(user.getId());
        var dto = profileMapper.toDto(profile);
        return ResponseEntity.ok(ApiResponse.success("Информация о профиле успешно получена!", dto));
    }

    @GetMapping("/{handle}")
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> getProfileByHandle(@PathVariable String handle) {
        var profile = profileService.getByHandle(handle);
        var dto = profileMapper.toDto(profile);
        return ResponseEntity.ok(ApiResponse.success("Информация о профиле успешно получена!", dto));
    }

    @PostMapping("/avatar")
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> updateAvatar(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        var updatedProfile = profileService.updateAvatar(user.getId(), file);
        var dto = profileMapper.toDto(updatedProfile);
        return ResponseEntity.ok(ApiResponse.success("Аватар обновлён", dto));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileEntity>> updateMyProfile(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody ProfileUpdateDTO dto
    ) {
        var updated = profileService.update(user.getId(), dto);
        return ResponseEntity.ok(ApiResponse.success("Профиль обновлён", updated));
    }
}
