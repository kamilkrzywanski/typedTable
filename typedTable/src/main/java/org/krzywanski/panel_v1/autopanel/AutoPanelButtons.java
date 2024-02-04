package org.krzywanski.panel_v1.autopanel;

import javax.swing.*;

public class AutoPanelButtons<T> extends JPanel {
    final TypedAutoPanel<T> dataPanel;
    public AutoPanelButtons(TypedAutoPanel<T> dataPanel) {
        super();
        this.dataPanel = dataPanel;
        addControllButtons();
    }


    private void addControllButtons() {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> {
            dataPanel.fillWithData();
            dataPanel.setFieldsEditable(false);
        });
        add(cancelButton, "grow");


        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            dataPanel.setFieldsEditable(true);
        });
        add(editButton, "grow");


        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            dataPanel.setFieldsEditable(false);
            dataPanel.saveChanges();
        });
        add(saveButton, "grow, span 3");
    }
}
