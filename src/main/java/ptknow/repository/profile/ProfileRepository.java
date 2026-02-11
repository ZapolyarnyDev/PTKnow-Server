package ptknow.repository.profile;

import ptknow.entity.profile.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {
    Optional<ProfileEntity> findByHandle(String handle);
    Optional<ProfileEntity> findByUserId(UUID userId);
    boolean existsByHandle(String handle);
}
