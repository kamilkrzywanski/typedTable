package org.krzywanski.panel_v1.dataflow;

public interface Update<T> {
    T update(T data) throws Exception;
}
