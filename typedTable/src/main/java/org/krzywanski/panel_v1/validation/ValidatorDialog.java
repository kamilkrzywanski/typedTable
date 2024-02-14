package org.krzywanski.panel_v1.validation;

import jakarta.validation.ConstraintViolation;
import org.krzywanski.panel_v1.autopanel.PanelMode;
import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Set;
import java.util.function.Supplier;

public class ValidatorDialog<T> extends JWindow {

    JLabel errorLabel = new JLabel();
    final Supplier<Set<ConstraintViolation<T>>> validationResult;
    Component component;
    final TypedAutoPanel<?> parentPanel;

    public ValidatorDialog(Component parent, TypedAutoPanel<?> parentPanel, Supplier<Set<ConstraintViolation<T>>> validationResult) {
        super(SwingUtilities.getWindowAncestor(parent));
        this.validationResult = validationResult;
        this.component = parent;
        this.parentPanel = parentPanel;

        buildUI();
    }

    private void buildUI() {
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.add(errorLabel);
        contentPane.setBackground(Color.white);
        contentPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        pack();
        computeLocation();
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setVisible(false);
                super.focusLost(e);
            }

            @Override
            public void focusGained(FocusEvent e) {

                if (!validationResult.get().isEmpty()) {
                    setVisible(true);
                }
                super.focusGained(e);
            }
        });

        getParent().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                computeLocation();
                super.componentMoved(e);
            }
        });

    }

    private void computeLocation() {
        Point loc = component.getLocationOnScreen();
        setLocation(loc.x + 20, loc.y + 30);
    }

    public void showErrorWindow(String message) {
        errorLabel.setText(message);
        pack();
        setVisible(true);
    }


    @Override
    public void setVisible(boolean b) {
        super.setVisible(b && (parentPanel.getMode() == PanelMode.UPDATE || parentPanel.getMode() == PanelMode.ADD));
    }
}
