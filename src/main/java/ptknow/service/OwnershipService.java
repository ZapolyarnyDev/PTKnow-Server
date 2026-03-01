package ptknow.service;

import ptknow.model.auth.Auth;

public interface OwnershipService<ID> {

    boolean isOwner(ID resourceId, Auth auth);

    Auth getOwner(ID resourceId);

}

