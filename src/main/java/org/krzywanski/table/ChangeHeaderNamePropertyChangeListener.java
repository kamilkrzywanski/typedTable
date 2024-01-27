package org.krzywanski.table;

import javax.swing.table.TableColumn;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Listener for change header name event
 * in case when you change header name in table it updates column name in list of propertDescriptors
 */
public class ChangeHeaderNamePropertyChangeListener implements PropertyChangeListener {

    final ColumnCreator columnCreator;

    public ChangeHeaderNamePropertyChangeListener(ColumnCreator columnCreator) {
        this.columnCreator = columnCreator;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(TableColumn.HEADER_VALUE_PROPERTY)) {
            columnCreator.tableColumns.
                    entrySet().
                    stream().
                    filter(entry -> entry.getValue().getHeaderValue().equals(evt.getOldValue())).
                    findFirst().
                    ifPresent(propertyDescriptorTableColumnEntry -> propertyDescriptorTableColumnEntry.getValue().setHeaderValue(evt.getNewValue()));
        }
    }
}
