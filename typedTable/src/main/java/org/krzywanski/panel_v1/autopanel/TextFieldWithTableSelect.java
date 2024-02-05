package org.krzywanski.panel_v1.autopanel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.TypedTable;
import org.krzywanski.table.TypedTablePanel;

import javax.swing.*;
import java.awt.*;

public class TextFieldWithTableSelect<T> extends JPanel {
    T currentValue;
    final JTextField textField = new JTextField();
    final TypedTablePanel<T> table;
    final JButton button = new JButton("...");

    public TextFieldWithTableSelect(TypedTablePanel<T> table) {
        this.table = table;
        this.textField.setEditable(false);

        button.addActionListener(e ->{
            JDialog dialog = new JDialog();
            dialog.setLayout(new MigLayout());
            dialog.add(table, "grow, span 2, wrap");
            dialog.add(new JButton("Cancel"));

           JButton okBUtton =  new JButton("OK");
           okBUtton.addActionListener(e1 -> {
                currentValue = table.getSelectedItem();
                textField.setText(currentValue.toString());
                dialog.dispose();
            });
            dialog.add(okBUtton, "wrap");

            dialog.repaint();
            dialog.pack();
            dialog.setLocationRelativeTo(button);
            dialog.setVisible(true);

        });
        setLayout(new MigLayout("gapx 0, insets 0"));
        add(textField, "grow, push");
        add(button);
        table.firstPageAction();
    }

    public void setTextField(T value){
        textField.setText(value.toString());
    }
    public T getCurrentValue() {
        return currentValue;
    }
}
