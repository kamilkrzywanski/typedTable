package org.krzywanski.table.validation;

import org.krzywanski.panel_v1.validation.FieldValidator;
import org.krzywanski.table.ColumnCreator;
import org.krzywanski.table.TypedTable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;

public class RevalidateDocumentListener<T> implements DocumentListener {
    final TypedTable<T> table;
    final JTextField component;
    FieldValidator<T> fieldValidator = new FieldValidator<>();
    final ColumnCreator columnCreator;
    final Border originalBorder;
    final ValidatorDialog<T> validatorDialog;
    final ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", Locale.getDefault());
    final Function<String, ?> transformer;

    public <E> RevalidateDocumentListener(TypedTable<T> table,
                                          JTextField component,
                                          ColumnCreator columnCreator,
                                          ValidatorDialog<T> validatorDialog,
                                          Function<String, E> transformer) {
        this.table = table;
        this.component = component;
        this.columnCreator = columnCreator;
        originalBorder = component.getBorder();
        this.validatorDialog = validatorDialog;
        this.transformer = transformer;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        insertUpdateAdapter();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        insertUpdateAdapter();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        insertUpdateAdapter();
    }

    public void insertUpdateAdapter() {
        Object value;
        if (component.getText().isEmpty())
            return;
        try {
            value = transformer.apply(component.getText());
        } catch (Exception ex) {
            validatorDialog.showIfErrorsPresent(Set.of(resourceBundle.getString("number.format.error")));
            return;
        }
        Set<String> result = fieldValidator.validateField(table.getTypeClass(), columnCreator.getColumnField(table.getSelectedColumn(), table).getField().getName(), value);
            if (!result.isEmpty()) {
                component.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.RED), originalBorder));
            } else {
                component.setBorder(originalBorder);
            }

            validatorDialog.showIfErrorsPresent(result);
    }


}
