package org.krzywanski.table.validation;

import org.krzywanski.panel_v1.validation.FieldValidator;
import org.krzywanski.table.TypedTable;
import sun.reflect.misc.ReflectUtil;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Set;

public class GenericEditor<T> extends DefaultCellEditor {

    Class<?>[] argTypes = new Class<?>[]{String.class};
    java.lang.reflect.Constructor<?> constructor;
    Object value;
    FieldValidator<T> fieldValidator = new FieldValidator<>();
    final TypedTable<T> table;

    public GenericEditor(JTextField textField, TypedTable<T> table) {
        super(textField);
        getComponent().setName("Table.editor");
        this.table = table;
    }

    public boolean stopCellEditing() {
        String s = (String) super.getCellEditorValue();
        Set<String> result = fieldValidator.validateField(table.getTypeClass(), table.getColumnCreator().getColumnField(table.getSelectedColumn(), table).getField().getName(), s);
        if (!result.isEmpty()) {
            return false;
        }
        // Here we are dealing with the case where a user
        // has deleted the string value in a cell, possibly
        // after a failed validation. Return null, so that
        // they have the option to replace the value with
        // null or use escape to restore the original.
        // For Strings, return "" for backward compatibility.
        try {
            if ("".equals(s)) {
                if (constructor.getDeclaringClass() == String.class) {
                    value = s;
                }
                return super.stopCellEditing();
            }

            SwingUtilities2.checkAccess(constructor.getModifiers());
            value = constructor.newInstance(new Object[]{s});
        } catch (Exception e) {
            return false;
        }
        return super.stopCellEditing();
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int row, int column) {
        this.value = null;
        ((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
        try {
            Class<?> type = table.getColumnClass(column);
            // Since our obligation is to produce a value which is
            // assignable for the required type it is OK to use the
            // String constructor for columns which are declared
            // to contain Objects. A String is an Object.
            if (type == Object.class) {
                type = String.class;
            }
            ReflectUtil.checkPackageAccess(type);
            SwingUtilities2.checkAccess(type.getModifiers());
            constructor = type.getConstructor(argTypes);
        } catch (Exception e) {
            return null;
        }
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    public Object getCellEditorValue() {
        return value;
    }
}