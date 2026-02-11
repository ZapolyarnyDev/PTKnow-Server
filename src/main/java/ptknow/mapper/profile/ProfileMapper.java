package ptknow.mapper.profile;

import ptknow.dto.profile.ProfileResponseDTO;
import ptknow.entity.profile.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "avatarUrl", source = "avatar.id", qualifiedByName = "mapAvatarIdToUrl")
    ProfileResponseDTO toDto(ProfileEntity entity);

    @Named("mapAvatarIdToUrl")
    default String mapAvatarIdToUrl(java.util.UUID avatarId) {
        if (avatarId == null) return null;
        return "/v0/files/" + avatarId;
    }
}
