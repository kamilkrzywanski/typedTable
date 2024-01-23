package org.krzywanski.table.providers;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public interface GenericSelectionListener<T> {
    void getSelectedItem(T element);
}
