package ptknow.service;

public interface HandleService<V> {

    V getByHandle(String handle);
}
