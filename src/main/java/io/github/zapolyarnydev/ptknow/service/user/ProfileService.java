package io.github.zapolyarnydev.ptknow.service.user;

import io.github.zapolyarnydev.ptknow.entity.file.FileEntity;
import io.github.zapolyarnydev.ptknow.entity.user.ProfileEntity;
import io.github.zapolyarnydev.ptknow.entity.user.UserEntity;
import io.github.zapolyarnydev.ptknow.exception.user.UserNotFoundException;
import io.github.zapolyarnydev.ptknow.generator.handle.HandleGenerator;
import io.github.zapolyarnydev.ptknow.repository.auth.ProfileRepository;
import io.github.zapolyarnydev.ptknow.service.HandleService;
import io.github.zapolyarnydev.ptknow.service.file.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProfileService implements HandleService<ProfileEntity> {

    FileService fileService;
    ProfileRepository repository;
    HandleGenerator handleGenerator;

    @Transactional
    public ProfileEntity createProfile(UserEntity user) {
        String handle = handleGenerator.generate(repository::existsByHandle);
        var entity = ProfileEntity.builder()
                .handle(handle)
                .user(user)
                .build();

        repository.save(entity);
        return entity;
    }

    @Transactional
    public ProfileEntity updateSummary(UUID userId, String summary) {
        ProfileEntity profile = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setSummary(summary);
        return repository.save(profile);
    }

    @Transactional
    public ProfileEntity updateAvatar(UUID userId, MultipartFile file) throws IOException {
        ProfileEntity profile = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        FileEntity savedFile = fileService.saveFile(file);
        profile.setAvatar(savedFile);

        return repository.save(profile);
    }

    @Override
    public ProfileEntity getByHandle(String handle) {
        return repository.findByHandle(handle)
                .orElseThrow(() -> new UserNotFoundException(handle));
    }
}
