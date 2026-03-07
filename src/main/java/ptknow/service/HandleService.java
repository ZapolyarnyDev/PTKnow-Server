package ptknow.service;

import ptknow.model.auth.Auth;

public interface HandleService<V> {

    V getByHandle(String handle);

    V seeByHandle(String handle, Auth initiator);
}

