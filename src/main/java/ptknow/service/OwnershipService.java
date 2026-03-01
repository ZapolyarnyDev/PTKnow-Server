package ptknow.service;

import ptknow.entity.auth.AuthEntity;

public interface OwnershipService<ID> {

    boolean isOwner(ID resourceId, AuthEntity auth);

    AuthEntity getOwner(ID resourceId);

}
