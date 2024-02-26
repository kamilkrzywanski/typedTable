package org.krzywanski.panel_v1.fields;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.table.TypedTablePanel;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class TextFieldWithTableSelect<T> extends JPanel {

    /**
     * Action listeners for the text field
     */
    final List<ActionListener> actionListeners = new ArrayList<>();
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
    final JButton okBUtton = new JButton("Ok");
    final JButton emptyButton = new JButton("Empty");

    public TextFieldWithTableSelect(TypedTablePanel<T> table, String dialogTitle) {
        this.table = table;
        this.textField.setEditable(false);

        table.addTableMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    okBUtton.doClick();
                }
            }
        });

        table.addTableKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    okBUtton.doClick();
                }
            }
        });


        button.addActionListener(e -> {
            JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(table));
            dialog.setModal(true);
            dialog.setLayout(new MigLayout());

            dialog.setTitle(dialogTitle);
            dialog.add(table, "grow, span 3, wrap");
            JButton cancelButton = new JButton("Cancel");
            dialog.add(cancelButton);


            cancelButton.addActionListener(e1 -> {
                dialog.dispose();
                actionListeners.forEach(actionListener -> actionListener.actionPerformed(new ActionEvent(this, 0, "Cancel")));
            });

            dialog.add(okBUtton);
            dialog.add(emptyButton, "wrap");

            okBUtton.addActionListener(e1 -> {
                currentValue = table.getSelectedItem();
                textField.setText(currentValue.toString());

                actionListeners.forEach(actionListener -> actionListener.actionPerformed(new ActionEvent(this, 0, "OK")));
                dialog.dispose();
            });

            emptyButton.addActionListener(evt -> {
                textField.setText("");
                currentValue = null;
                actionListeners.forEach(actionListener -> actionListener.actionPerformed(new ActionEvent(this, 0, "OK")));
                dialog.dispose();
            });

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

        actionListeners.forEach(actionListener -> actionListener.actionPerformed(new ActionEvent(this, 0, "OK")));
    }
    public T getCurrentValue() {
        return currentValue;
    }

    public JTextField getTextField() {
        return textField;
    }

    public JButton getButton() {
        return button;
    }

    @SuppressWarnings("unchecked")
    public static <T> TextFieldWithTableSelect<T> getTextWithTableSelect(List<T> data, String dialogTitle) {
        return new TextFieldWithTableSelect<>(TypedTablePanel.getTableWithData(data, (Class<T>) data.get(0).getClass()), dialogTitle);
    }

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

}
