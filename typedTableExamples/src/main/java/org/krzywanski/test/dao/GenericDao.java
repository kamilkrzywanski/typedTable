package org.krzywanski.test.dao;

import java.io.Serializable;

public interface GenericDao<T extends Serializable, Id extends Serializable> {
    T findById(Id id);
    void remove(T data);
    T insert(T data);
    T update(T data);
}