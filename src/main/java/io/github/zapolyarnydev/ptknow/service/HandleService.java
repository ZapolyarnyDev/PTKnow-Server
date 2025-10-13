package io.github.zapolyarnydev.ptknow.service;

import org.springframework.stereotype.Service;

@Service
public interface HandleService<V> {

    V getByHandle(String handle);
}
