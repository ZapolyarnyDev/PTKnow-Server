package ptknow.exception.profile;

import java.util.UUID;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(UUID id) {
        super("Profile with id " + id + " not found");
    }
}
