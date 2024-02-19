package org.krzywanski.panel_v1.autopanel;

import org.krzywanski.panel_v1.DataAction;

public interface PanelChangeValueListener<T> {
    void valueChanged(T element, DataAction action);
}
