package io.github.zapolyarnydev.ptknow.service.user;

import io.github.zapolyarnydev.ptknow.entity.user.ProfileEntity;
import io.github.zapolyarnydev.ptknow.entity.user.UserEntity;
import io.github.zapolyarnydev.ptknow.exception.user.UserNotFoundException;
import io.github.zapolyarnydev.ptknow.generator.handle.HandleGenerator;
import io.github.zapolyarnydev.ptknow.repository.auth.ProfileRepository;
import io.github.zapolyarnydev.ptknow.service.HandleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProfileService implements HandleService<ProfileEntity> {

    ProfileRepository repository;
    HandleGenerator handleGenerator;

    public ProfileEntity createProfile(UserEntity user) {
        String handle = handleGenerator.generate(repository::existsByHandle);
        var entity = ProfileEntity.builder()
                .handle(handle)
                .user(user)
                .build();

        repository.save(entity);
        return entity;
    }

    @Override
    public ProfileEntity getByHandle(String handle) {
        return repository.findByHandle(handle)
                .orElseThrow(() -> new UserNotFoundException(handle));
    }
}
