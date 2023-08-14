package org.krzywanski.table.table;

import org.krzywanski.table.annot.MyTableColumn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class TypedTableRenderer extends DefaultTableCellRenderer {
    ColumnCreator columnCreator;

    TypedTableRenderer(ColumnCreator columnCreator) {
        this.columnCreator = columnCreator;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Field field = getColumnField(column, table);
        if (Date.class.equals(field.getType())) {
            if (value != null)
                value = new SimpleDateFormat(Objects.toString(getFormat(field), "MM/dd/yy")).format(value);

        }
        if (Number.class.isAssignableFrom(field.getType())) {
            if (value != null) value = new DecimalFormat(Objects.toString(getFormat(field), "#.#")).format(value);

        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    Field getColumnField(int columnIndex, JTable table) {
        return columnCreator.getFieldByName(table.getTableHeader().getColumnModel().getColumn(columnIndex).getHeaderValue());
    }

    private String getFormat(Field field) {
        MyTableColumn annotation = field.getAnnotation(MyTableColumn.class);
        String format = null;
        if (annotation != null) {
            if (!annotation.format().isEmpty()) format = annotation.format();
        }
        return format;
    }

}
