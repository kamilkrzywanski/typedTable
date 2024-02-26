package org.krzywanski.table;

import org.krzywanski.panel_v1.dataflow.Update;
import org.krzywanski.table.utils.FieldMock;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.lang.reflect.InvocationTargetException;

public class TableDataFlowListener<T> implements TableModelListener {
    final TypedTable<T> table;
    final Update<T> update;

    public TableDataFlowListener(TypedTable<T> table, Update<T> update) {
        this.table = table;
        this.update = update;
    }

    @Override
    public void tableChanged(TableModelEvent evt) {
        if (evt.getColumn() >= 0 && evt.getFirstRow() >= 0 && evt.getType() == TableModelEvent.UPDATE) {
            table.updateRow(evt.getFirstRow(), data -> updateColumn(data, evt));
        }
    }

    private T updateColumn(T data, TableModelEvent evt) {
        FieldMock fieldMock = table.columnCreator.getTableColumns().get(evt.getColumn());
        try {
            fieldMock.getPropertyDescriptor().getWriteMethod().invoke(data, table.model.getValueAt(evt.getFirstRow(), evt.getColumn()));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        if (update != null) {
            try {
                return update.update(data);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return data;
    }
}
