package io.github.zapolyarnydev.ptknow.repository.auth;

import io.github.zapolyarnydev.ptknow.entity.user.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {
    Optional<ProfileEntity> findByHandle(String handle);
    boolean existsByHandle(String handle);
}
