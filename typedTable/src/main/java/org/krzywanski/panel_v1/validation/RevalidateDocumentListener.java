package org.krzywanski.panel_v1.validation;

import org.krzywanski.panel_v1.FieldControllerElement;

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
        if (element.getEditorComponent().hasFocus())
            element.validate();
    }


}
