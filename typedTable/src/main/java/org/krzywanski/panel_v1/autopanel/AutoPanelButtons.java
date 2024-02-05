package org.krzywanski.panel_v1.autopanel;

import javax.swing.*;

public class AutoPanelButtons<T> extends JPanel {
    final TypedAutoPanel<T> dataPanel;
    JButton cancelButton = new JButton("Cancel");
    JButton editButton = new JButton("Edit");
    JButton saveButton = new JButton("Save");

    public AutoPanelButtons(TypedAutoPanel<T> dataPanel) {
        super();
        this.dataPanel = dataPanel;
        addControllButtons();
    }


    private void addControllButtons() {
        cancelButton.setEnabled(false);
        cancelButton.addActionListener(e -> {

            saveButton.setEnabled(false);
            editButton.setEnabled(true);
            cancelButton.setEnabled(false);

            dataPanel.fillWithData();
            dataPanel.setFieldsEditable(false);
        });
        add(cancelButton, "grow");


        editButton.addActionListener(e -> {

            cancelButton.setEnabled(true);
            saveButton.setEnabled(true);
            editButton.setEnabled(false);

            dataPanel.setFieldsEditable(true);
        });
        add(editButton, "grow");


        cancelButton.setEnabled(false);
        saveButton.addActionListener(e -> {

            cancelButton.setEnabled(false);
            saveButton.setEnabled(false);
            editButton.setEnabled(true);

            dataPanel.setFieldsEditable(false);
            dataPanel.saveChanges();
        });
        add(saveButton, "grow, span 3");
    }
}
