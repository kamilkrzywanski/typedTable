package org.krzywanski.table.validation;

import org.krzywanski.panel_v1.validation.FieldValidator;
import org.krzywanski.table.TypedTable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;

public class GenericEditor<T> extends DefaultCellEditor {

    Object value;
    FieldValidator<T> fieldValidator = new FieldValidator<>();
    final TypedTable<T> table;
    final ValidatorDialog<T> validatorDialog;
    final ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", Locale.getDefault());
    final Function<String, ?> transformer;

    public <E> GenericEditor(JTextField textField, TypedTable<T> table, ValidatorDialog<T> validatorDialog, Function<String, E> transformer) {
        super(textField);
        getComponent().setName("Table.editor");
        this.table = table;
        this.validatorDialog = validatorDialog;
        this.transformer = transformer;
    }

    public boolean stopCellEditing() {
        String s = (String) super.getCellEditorValue();
        try {
            value = transformer.apply(s);

            Set<String> result = fieldValidator.validateField(table.getTypeClass(), table.getColumnCreator().getColumnField(table.getSelectedColumn(), table).getField().getName(), value);
            if (!result.isEmpty()) {
                validatorDialog.showIfErrorsPresent(result);
                return false;
            }
        } catch (Exception e) {
            validatorDialog.showIfErrorsPresent(Set.of(resourceBundle.getString("number.format.error")));
            return false;
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
        return value;
    }
}