package org.krzywanski.panel_v1.validation;

import jakarta.validation.ConstraintViolation;
import org.krzywanski.panel_v1.FieldControllerElement;
import org.krzywanski.panel_v1.autopanel.PanelMode;
import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Set;

public class ValidatorDialog<T> {

    JLabel errorLabel = new JLabel();
    FieldControllerElement controller;
    final TypedAutoPanel<T> parentPanel;
    FieldValidator<T> validator = new FieldValidator<>();

    WindowDelegate window = null;

    public ValidatorDialog(FieldControllerElement controller, TypedAutoPanel<T> parentPanel) {
        this.controller = controller;
        this.parentPanel = parentPanel;
    }



    public void showErrorWindow(String message) {
        controller.getFieldValueController().setBorder(BorderFactory.createLineBorder(java.awt.Color.RED, 2));
        errorLabel.setText(message);

        if (window == null)
            window = new WindowDelegate(SwingUtilities.getWindowAncestor(controller.getEditorComponent()));

        window.pack();
        window.setVisible(true);
    }

    public void showIfErrorsPresent() {
        if (!getValidationResult().isEmpty()) {
            showErrorWindow(getValidationResult().iterator().next().getMessage());
        } else {
            controller.getFieldValueController().resetBorder();

            if (window != null)
                window.setVisible(false);
        }
    }


    public Set<ConstraintViolation<T>> getValidationResult() {
        return validator.validateValue(parentPanel.getDataClass(), controller.getField().getName(), controller.getFieldValueController().getValue());
    }


    public void setVisible(boolean b) {
        if (window == null)
            window = new WindowDelegate(SwingUtilities.getWindowAncestor(controller.getEditorComponent()));
        window.setVisible(b);
    }


    private class WindowDelegate extends JWindow {


        public WindowDelegate(Window parent) {
            super(parent);
            buildUI();
        }

        @Override
        public void setVisible(boolean b) {
            super.setVisible(b && (parentPanel.getMode() == PanelMode.UPDATE || parentPanel.getMode() == PanelMode.ADD));
        }

        private void buildUI() {
            JPanel contentPane = (JPanel) getContentPane();
            contentPane.add(errorLabel);
            contentPane.setBackground(Color.white);
            contentPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            pack();
            computeLocation();
            controller.getEditorComponent().addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    setVisible(false);
                    super.focusLost(e);
                }

                @Override
                public void focusGained(FocusEvent e) {

                    if (!getValidationResult().isEmpty()) {
                        setVisible(true);
                    }
                    super.focusGained(e);
                }
            });

            getOwner().addComponentListener(new ComponentAdapter() {
                @Override
                public void componentMoved(ComponentEvent e) {
                    computeLocation();
                    super.componentMoved(e);
                }
            });

            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    setVisible(false);
                }
            });

        }

        private void computeLocation() {
            try {
                Point loc = controller.getEditorComponent().getLocationOnScreen();
                setLocation(loc.x + 20, loc.y + 30);
            } catch (IllegalComponentStateException e) {
                //do nothing
            }
        }
    }
}
