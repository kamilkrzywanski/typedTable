package org.krzywanski.panel_v1.dataflow;

/**
 * Controller for data flow between table, panel and datasource
 */
public interface DataFlowAdapter<T> extends Insert<T>, Update<T>, Remove<T> {

}
