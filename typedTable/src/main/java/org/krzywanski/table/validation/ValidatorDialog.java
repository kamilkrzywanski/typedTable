package org.krzywanski.table.validation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Set;

public class ValidatorDialog<T> {

    final Component component;

    WindowDelegate window = null;

    public ValidatorDialog(Component component) {
        this.component = component;
    }


    public void showErrorWindow(String message) {
        if (window == null)
            window = new WindowDelegate(SwingUtilities.getWindowAncestor(component));

        window.setLabel(message);
        window.pack();
        window.setVisible(true);
    }

    public void showIfErrorsPresent(Set<String> errors) {
        if (!errors.isEmpty()) {
            showErrorWindow(errors.iterator().next());
        } else {
            if (window != null)
                window.dispose();
        }
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

//        @Override
//        public synchronized void setVisible(boolean b) {
//            super.setVisible(b && (parentPanel.getMode() == PanelMode.UPDATE || parentPanel.getMode() == PanelMode.ADD));
//        }

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
                Point loc = component.getLocationOnScreen();
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
