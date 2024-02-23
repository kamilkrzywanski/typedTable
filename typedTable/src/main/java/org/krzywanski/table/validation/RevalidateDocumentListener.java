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
import java.util.Set;

public class RevalidateDocumentListener<T> implements DocumentListener {
    final TypedTable<T> table;
    final JTextField component;
    FieldValidator<T> fieldValidator = new FieldValidator<>();
    final ColumnCreator columnCreator;
    final Border originalBorder;
    final ValidatorDialog<T> validatorDialog;

    public RevalidateDocumentListener(TypedTable<T> table, JTextField component, ColumnCreator columnCreator) {
        this.table = table;
        this.component = component;
        this.columnCreator = columnCreator;
        originalBorder = component.getBorder();
        validatorDialog = new ValidatorDialog<T>(component);
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
        if (component.hasFocus()) {
            Set<String> result = fieldValidator.validateField(table.getTypeClass(), columnCreator.getColumnField(table.getSelectedColumn(), table).getField().getName(), component.getText());
            if (!result.isEmpty()) {
                component.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.RED), originalBorder));
            } else {
                component.setBorder(originalBorder);
            }

            validatorDialog.showIfErrorsPresent(result);
        }
    }


}
