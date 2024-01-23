package org.krzywanski.table.components;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

public class PopupDialog extends JDialog {
    ResourceBundle resourceBundle = ResourceBundle.getBundle("TableBundle", Locale.getDefault());
    JTextField textField = new JFormattedTextField();
    JButton searchButton = new JButton(new ImageIcon(ClassLoader.getSystemResource("search.png")));
    final ActionListener firstPageAction;

    public PopupDialog(ActionListener firstPageAction) {
        super();
        setUndecorated(true);
        this.firstPageAction = firstPageAction;
        textField.setPreferredSize(new Dimension(100, 20));
        setLayout(new MigLayout("insets 0"));
        add(textField);
        addButton(searchButton);
        pack();

        getRootPane().registerKeyboardAction(e -> this.setVisible(false), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> invokeSearchAction(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        textField.setToolTipText(resourceBundle.getString("search.tooltip"));
        textField.addFocusListener(new FocusListenerImpl());
        addFocusListener(new FocusListenerImpl());
        textField.addActionListener(e -> invokeSearchAction());
        searchButton.addActionListener(e -> invokeSearchAction());
    }

    private void invokeSearchAction() {
        setVisible(false);
        firstPageAction.actionPerformed(new ActionEvent(this, 0, textField.getText()));
    }

    class FocusListenerImpl extends java.awt.event.FocusAdapter {
        public void focusLost(java.awt.event.FocusEvent evt) {
            setVisible(false);
        }
    }

    public void setVisible(boolean b, Component invoker) {
        setLocation(invoker.getLocationOnScreen().x, invoker.getLocationOnScreen().y + 20);
        pack();
        super.setVisible(b);
    }

    public String getText() {
        return textField.getText();
    }

    void addButton(JButton button) {
        button.setFocusable(false);
        button.setBackground(null);
        button.setOpaque(true);
        button.setBackground(null);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        add(button);
    }
}
