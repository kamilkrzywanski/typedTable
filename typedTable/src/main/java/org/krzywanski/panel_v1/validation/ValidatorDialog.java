package org.krzywanski.panel_v1.validation;

import org.krzywanski.panel_v1.AbstractTypedPanel;
import org.krzywanski.panel_v1.autopanel.PanelMode;
import org.krzywanski.panel_v1.fields.FieldControllerElement;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Set;

public class ValidatorDialog<T> {

    final FieldControllerElement controller;
    final AbstractTypedPanel<T> parentPanel;
    final FieldValidator<T> validator = new FieldValidator<>();

    WindowDelegate window = null;

    public ValidatorDialog(FieldControllerElement controller, AbstractTypedPanel<T> parentPanel) {
        this.controller = controller;
        this.parentPanel = parentPanel;
    }



    public void showErrorWindow(String message) {
        controller.getFieldValueController().errorBorder();

        if (window == null)
            window = new WindowDelegate(SwingUtilities.getWindowAncestor(controller.getEditorComponent()));

        window.setLabel(message);
        window.pack();
        window.setVisible(true);
    }

    public void showIfErrorsPresent() {
        if(parentPanel.getMode() == PanelMode.ADD || parentPanel.getMode() == PanelMode.UPDATE) {
            if (!getValidationResult().isEmpty()) {
                showErrorWindow(getValidationResult().iterator().next());
            } else {
                controller.getFieldValueController().resetBorder();

                if (window != null)
                    window.dispose();
            }
        }
    }


    public Set<String> getValidationResult() {
        return validator.validateField(parentPanel.getDataClass(), controller);
    }

    public void dispose() {
        if (window != null)
            window.dispose();
    }


    private class WindowDelegate extends JWindow {

        final JLabel errorLabel;

        public WindowDelegate(Window parent) {
            super(parent);
            errorLabel = new JLabel();
            errorLabel.setBorder(new LineBorder(Color.RED, 1));
            buildUI();
        }

        @Override
        public synchronized void setVisible(boolean b) {
            super.setVisible(b && (parentPanel.getMode() == PanelMode.UPDATE || parentPanel.getMode() == PanelMode.ADD));
        }

        private void buildUI() {
            JPanel contentPane = (JPanel) getContentPane();
            contentPane.add(errorLabel);
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

        protected void setLabel(String message) {
            errorLabel.setText(message);
        }
    }
}
