package org.krzywanski.panel_v1.validation;

import org.krzywanski.panel_v1.fields.FieldControllerElement;
import org.krzywanski.panel_v1.fields.TextFieldWithTableSelect;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class RevalidateDocumentListener implements DocumentListener {
    final FieldControllerElement element;

    public RevalidateDocumentListener(FieldControllerElement element) {
        this.element = element;
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
        if (element.getEditorComponent() instanceof JSpinner) {
            JSpinner spinner = (JSpinner) element.getEditorComponent();
            if (spinner.getEditor() instanceof JSpinner.DefaultEditor) {
                if (((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().hasFocus() || spinner.hasFocus())
                    element.validate();
            }
            return;
        }

        if (element.getEditorComponent() instanceof TextFieldWithTableSelect<?>) {
            element.validate();
        }


        if (element.getEditorComponent().hasFocus())
            element.validate();
    }


}
