package org.apache.bark.repo;

public interface BarkIdRepo<T> extends BarkRepo<T> {

    Long getNextId();

    T getById(Long id);

    void delete(Long id);

}
