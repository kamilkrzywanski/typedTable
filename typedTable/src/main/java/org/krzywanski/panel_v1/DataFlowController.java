package org.krzywanski.panel_v1;

/**
 * Controller for data flow between table, panel and datasource
 */
public interface DataFlowController<T> {
    void remove(T data);
    T insert(T data);
    T update(T data);

}
