package org.krzywanski.panel_v1;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.util.ResourceBundle;

public class ToolBoxPopupMenu extends JPopupMenu {

    static final ResourceBundle rb = ResourceBundle.getBundle("ToolBoxPopupMenu");
    JMenuItem cutMenuItem = new JMenuItem();
    JMenuItem copyMenuItem = new JMenuItem();
    JMenuItem pasteMenuItem = new JMenuItem();

    public ToolBoxPopupMenu() {
        cutMenuItem.setText(rb.getString("cut"));
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        add(cutMenuItem);

        copyMenuItem.setText(rb.getString("copy"));
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        add(copyMenuItem);

        pasteMenuItem.setText(rb.getString("paste"));
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        add(pasteMenuItem);

        cutMenuItem.addActionListener(new DefaultEditorKit.CutAction());
        copyMenuItem.addActionListener(new DefaultEditorKit.CopyAction());
        pasteMenuItem.addActionListener(new DefaultEditorKit.PasteAction());
    }
}
