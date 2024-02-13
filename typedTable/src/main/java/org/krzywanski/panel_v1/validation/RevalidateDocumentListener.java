package org.krzywanski.panel_v1.validation;

import jakarta.validation.ConstraintViolation;
import org.krzywanski.panel_v1.FieldControllerElement;
import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.util.Set;

public class RevalidateDocumentListener<T> implements DocumentListener {

    private final Class<T> dataClass;
    final JTextComponent component;
    final FieldControllerElement element;

    FieldValidator<T> validator = new FieldValidator<>();

    final TypedAutoPanel<?> typedAutoPanel;
    ValidatorDialog dialog;

    public RevalidateDocumentListener(JTextComponent component, FieldControllerElement element, Class<T> dataClass, TypedAutoPanel<?> typedAutoPanel) {
        this.component = component;
        this.element = element;
        this.dataClass = dataClass;
        this.typedAutoPanel = typedAutoPanel;
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


        if (SwingUtilities.getWindowAncestor(component) == null) {
            return;
        }

        Set<ConstraintViolation<T>> validationResult = getValidationResult();
        validationResult.forEach(violation -> getDialog().showErrorWindow(violation.getMessage()));


        if (validationResult.isEmpty() && dialog != null) {
            dialog.setVisible(false);
        }

    }


    ValidatorDialog getDialog() {
        if (dialog == null) {
            dialog = new ValidatorDialog<>(component, typedAutoPanel, this::getValidationResult);
        }
        return dialog;
    }

    public Set<ConstraintViolation<T>> getValidationResult() {
        return validator.validateValue(dataClass, element.getField().getName(), component.getText());
    }
}
