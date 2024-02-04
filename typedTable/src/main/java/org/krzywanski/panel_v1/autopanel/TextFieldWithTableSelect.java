package org.krzywanski.panel_v1.autopanel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.TypedTable;

import javax.swing.*;

public class TextFieldWithTableSelect<T> extends JPanel {
    final JTextField textField = new JTextField();
    final TypedTable<T> table;
    final JButton button = new JButton("...");

    public TextFieldWithTableSelect(TypedTable<T> table) {
        this.table = table;

        button.addActionListener(e ->{
            JDialog dialog = new JDialog();
            dialog.setLayout(new MigLayout());
            dialog.add(table, "grow, span 2, wrap");
            dialog.add(new JButton("Cancel"));
            dialog.add(new JButton("OK"), "wrap");

            dialog.pack();
            dialog.setVisible(true);

        });

        add(textField);
        add(button);
    }
}
