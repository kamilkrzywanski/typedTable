package org.krzywanski.table.validation;

import org.krzywanski.table.TypedTable;

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
    final TypedTable<T> table;

    public ValidatorDialog(Component component, TypedTable<T> table) {
        this.component = component;
        this.table = table;
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
            setVisible(false);
        }
    }

    public void setVisible(boolean b) {
        if (window != null)
            window.setVisible(b);
    }

    public void computeLocationForCell(JTable table, int row, int column) {
        if (window != null)
            window.computeLocationForCell(table, row, column);
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
            if (b)
                computeLocation();
            super.setVisible(b);
        }

        private void buildUI() {
            JPanel contentPane = (JPanel) getContentPane();
            contentPane.add(errorLabel);
            contentPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            pack();


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
            computeLocationForCell(table, table.getSelectedRow(), table.getSelectedColumn());
        }

        protected void setLabel(String message) {
            errorLabel.setText(message);
        }

        public void computeLocationForCell(JTable table, int row, int column) {
            try {
                Rectangle rect = table.getCellRect(row, column, true);
                Point cellLocationOnScreen = rect.getLocation();
                Point tableLocationOnScreen = table.getLocationOnScreen();
                int relativeX = tableLocationOnScreen.x + cellLocationOnScreen.x;
                int relativeY = tableLocationOnScreen.y + cellLocationOnScreen.y + rect.height;
                setLocation(relativeX, relativeY);
            } catch (IllegalComponentStateException e) {
                //do nothing
            }
        }
    }
}
