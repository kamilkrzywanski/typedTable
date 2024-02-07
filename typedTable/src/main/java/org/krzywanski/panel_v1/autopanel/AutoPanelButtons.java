package org.krzywanski.panel_v1.autopanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AutoPanelButtons<T> extends JPanel {
    final TypedAutoPanel<T> dataPanel;
    JButton cancelButton = new JButton("Cancel");
    JButton editButton = new JButton("Edit");
    JButton saveButton = new JButton("Save");
    List<JComponent> externalComponents = new ArrayList<>();
    PanelMode mode = PanelMode.NONE;

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
            lockExternalComponents(true);
            mode = PanelMode.NONE;

            dataPanel.fillWithData();
            dataPanel.setFieldsEditable(false);
        });
        add(cancelButton, "grow");


        editButton.addActionListener(e -> {

            cancelButton.setEnabled(true);
            saveButton.setEnabled(true);
            editButton.setEnabled(false);
            lockExternalComponents(false);
            mode = PanelMode.UPDATE;

            dataPanel.setFieldsEditable(true);
        });
        add(editButton, "grow");


        cancelButton.setEnabled(false);
        saveButton.addActionListener(e -> {

            cancelButton.setEnabled(false);
            saveButton.setEnabled(false);
            editButton.setEnabled(true);
            lockExternalComponents(true);
            mode = PanelMode.NONE;

            dataPanel.setFieldsEditable(false);
            dataPanel.saveChanges();
        });
        add(saveButton, "grow, span 3");
    }

    private void lockExternalComponents(boolean lock) {
        externalComponents.forEach(component -> component.setEnabled(lock));
    }

    public void addExternalComponent(JComponent component) {
        externalComponents.add(component);
    }

    public PanelMode getMode() {
        return mode;
    }
}
