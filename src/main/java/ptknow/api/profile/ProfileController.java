package ptknow.api.profile;

import ptknow.dto.profile.ProfileResponseDTO;
import ptknow.dto.profile.ProfileUpdateDTO;
import ptknow.model.auth.Auth;
import ptknow.mapper.profile.ProfileMapper;
import ptknow.service.profile.ProfileService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ProfileResponseDTO> getMyProfile(@AuthenticationPrincipal Auth user) {
        var profile = profileService.getProfile(user.getId());
        var dto = profileMapper.toDto(profile);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{handle}")
    public ResponseEntity<ProfileResponseDTO> getProfileByHandle(@PathVariable String handle) {
        var profile = profileService.getByHandle(handle);
        var dto = profileMapper.toDto(profile);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/avatar")
    public ResponseEntity<ProfileResponseDTO> updateAvatar(
            @AuthenticationPrincipal Auth user,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        var updatedProfile = profileService.updateAvatar(user.getId(), file);
        var dto = profileMapper.toDto(updatedProfile);
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDTO> updateMyProfile(
            @AuthenticationPrincipal Auth user,
            @Valid @RequestBody ProfileUpdateDTO dto
    ) {
        var updated = profileService.update(user.getId(), dto);
        var updatedDto = profileMapper.toDto(updated);
        return ResponseEntity.ok(updatedDto);
    }
}

