package ptknow.service.profile;

import ptknow.dto.profile.ProfileUpdateDTO;
import ptknow.model.file.File;
import ptknow.model.profile.Profile;
import ptknow.model.auth.Auth;
import ptknow.exception.user.UserNotFoundException;
import ptknow.generator.handle.HandleGenerator;
import ptknow.repository.profile.ProfileRepository;
import ptknow.service.HandleService;
import ptknow.service.file.FileService;
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
public class ProfileService implements HandleService<Profile> {

    FileService fileService;
    ProfileRepository repository;
    HandleGenerator handleGenerator;

    @Transactional
    public Profile createProfile(String fullName, Auth user) {
        String handle = handleGenerator.generate(repository::existsByHandle);
        var entity = Profile.builder()
                .fullName(fullName)
                .handle(handle)
                .user(user)
                .build();

        return repository.save(entity);
    }

    @Transactional
    public Profile update(UUID userId, ProfileUpdateDTO dto) {
        Profile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (dto.fullName() != null)
            profile.setFullName(dto.fullName());
        if (dto.summary() != null)
            profile.setSummary(dto.summary());
        if (dto.handle() != null)
            profile.setHandle(dto.handle());

        return repository.save(profile);
    }

    @Transactional
    public Profile updateAvatar(UUID userId, MultipartFile file) throws IOException {
        Profile profile = getProfile(userId);

        File savedFile = fileService.saveFile(file);
        profile.setAvatar(savedFile);

        return repository.save(profile);
    }

    @Transactional(readOnly = true)
    @Override
    public Profile getByHandle(String handle) {
        return repository.findByHandle(handle)
                .orElseThrow(() -> new UserNotFoundException(handle));
    }

    @Transactional(readOnly = true)
    public Profile getProfile(UUID userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}

