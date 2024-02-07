package org.krzywanski.panel_v1.autopanel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.TypedTable;
import org.krzywanski.table.TypedTablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TextFieldWithTableSelect<T> extends JPanel {
    /**
     * Currently selected value
     */
    T currentValue;
    /**
     * Text field for displaying the selected value
     */
    final JTextField textField = new JTextField();
    /**
     * Table with selectable values
     */
    final TypedTablePanel<T> table;
    final JButton button = new JButton("...");
    final JButton okBUtton =  new JButton("OK");

    public TextFieldWithTableSelect(TypedTablePanel<T> table) {
        this.table = table;
        this.textField.setEditable(false);

        button.addActionListener(e ->{
            JDialog dialog = new JDialog();
            dialog.setLayout(new MigLayout());

            table.addTableKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                        okBUtton.doClick();
                    }
                }
            });

            dialog.add(table, "grow, span 2, wrap");
            dialog.add(new JButton("Cancel"));

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
        currentValue = value;

        if(value != null)
            textField.setText(value.toString());
        else
            textField.setText("");
    }
    public T getCurrentValue() {
        return currentValue;
    }
}
