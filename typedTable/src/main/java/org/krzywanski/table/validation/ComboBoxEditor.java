package org.krzywanski.table.validation;

import org.krzywanski.panel_v1.validation.FieldValidator;
import org.krzywanski.table.TypedTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Set;

public class ComboBoxEditor<T> extends DefaultCellEditor {

    Object value;
    FieldValidator<T> fieldValidator = new FieldValidator<>();
    final TypedTable<T> table;
    final ValidatorDialog<T> validatorDialog;
    final JComboBox<?> comboBox;

    public <E> ComboBoxEditor(JComboBox<E> comboBox, TypedTable<T> table, ValidatorDialog<T> validatorDialog) {
        super(comboBox);
        this.comboBox = comboBox;
        getComponent().setName("Table.editor");
        this.table = table;
        this.validatorDialog = validatorDialog;
    }

    public boolean stopCellEditing() {
        if (comboBox.isEditable()) {
            value = comboBox.getSelectedItem();
            // Commit edited value.
            Set<String> result = fieldValidator.validateField(table.getTypeClass(), table.getColumnCreator().getColumnField(table.getSelectedColumn(), table).getField().getName(), value);
            if (!result.isEmpty()) {
                validatorDialog.showIfErrorsPresent(result);
                return false;
            }

            comboBox.actionPerformed(new ActionEvent(
                    ComboBoxEditor.this, 0, ""));
        }
        return super.stopCellEditing();
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int row, int column) {
        this.value = null;
        ((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
//        try {
//            Class<?> type = table.getColumnClass(column);
//            // Since our obligation is to produce a value which is
//            // assignable for the required type it is OK to use the
//            // String constructor for columns which are declared
//            // to contain Objects. A String is an Object.
//            if (type == Object.class) {
//                type = String.class;
//            }
//            ReflectUtil.checkPackageAccess(type);
//            SwingUtilities2.checkAccess(type.getModifiers());
//            constructor = type.getConstructor(argTypes);
//        } catch (Exception e) {
//            validatorDialog.showIfErrorsPresent(Set.of(e.getMessage()));
//            return null;
//        }
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    public Object getCellEditorValue() {
        return comboBox.getSelectedItem();
    }
}