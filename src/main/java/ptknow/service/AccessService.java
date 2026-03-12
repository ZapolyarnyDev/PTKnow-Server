package ptknow.service;

import ptknow.model.auth.Auth;

public interface AccessService<ID> {

    boolean canSee(ID id, Auth initiator);
}
