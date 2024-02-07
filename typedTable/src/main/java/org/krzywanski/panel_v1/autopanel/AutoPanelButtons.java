package org.krzywanski.panel_v1.autopanel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.panel_v1.UpdateOrInsert;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoPanelButtons<T> extends JPanel {
    final TypedAutoPanel<T> dataPanel;
    JButton cancelButton = new JButton("Cancel");
    JButton editButton = new JButton("Edit");
    JButton saveButton = new JButton("Save");
    JPanel addOrCancelPanel = new JPanel(new MigLayout("gapx 0, insets 0"));
    JButton addButton = new JButton("Add");
    JButton deleteButton = new JButton("Delete");
    JPanel deleteOrSavePanel = new JPanel(new MigLayout("gapx 0, insets 0"));
    List<JComponent> externalComponents = new ArrayList<>();
    PanelMode mode = PanelMode.NONE;

    public AutoPanelButtons(TypedAutoPanel<T> dataPanel) {
        super(new MigLayout("gapx 0, insets 0"));
        this.dataPanel = dataPanel;
        addControllButtons();
    }


    private void addControllButtons() {

        cancelButton.setEnabled(false);
        addOrCancelPanel.add(addButton, "grow");
        add(addOrCancelPanel, "grow");



        add(editButton, "grow");

        cancelButton.setEnabled(false);

        deleteOrSavePanel.add(deleteButton, "grow");

        add(deleteOrSavePanel, "grow, span 3");

        installListeners();
    }

    private void installListeners() {
        saveButton.addActionListener(e -> {

            cancelButton.setEnabled(false);
            saveButton.setEnabled(false);
            editButton.setEnabled(true);
            lockExternalComponents(true);
            setAddOrCancelButton(Button.ADD);

            dataPanel.setFieldsEditable(false);
            dataPanel.saveChanges(mode == PanelMode.ADD ? UpdateOrInsert.INSERT : UpdateOrInsert.UPDATE);
            mode = PanelMode.NONE;
        });

        editButton.addActionListener(e -> {

            cancelButton.setEnabled(true);
            saveButton.setEnabled(true);
            editButton.setEnabled(false);
            lockExternalComponents(false);
            mode = PanelMode.UPDATE;
            setAddOrCancelButton(Button.CANCEL);

            dataPanel.setFieldsEditable(true);
        });

        cancelButton.addActionListener(e -> {

            saveButton.setEnabled(false);
            editButton.setEnabled(true);
            cancelButton.setEnabled(false);
            lockExternalComponents(true);


            mode = PanelMode.NONE;
            setAddOrCancelButton(Button.ADD);

            dataPanel.updateCurrentData(dataPanel.dataSupplier.get());
            dataPanel.fillWithData();
            dataPanel.setFieldsEditable(false);
        });

        addButton.addActionListener(e -> {
            cancelButton.setEnabled(true);
            saveButton.setEnabled(true);
            editButton.setEnabled(false);
            lockExternalComponents(false);
            try {
                dataPanel.updateCurrentData(dataPanel.dataClass.getDeclaredConstructor().newInstance());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                     NoSuchMethodException ex) {
                restorePanel();
                Logger.getAnonymousLogger().log(Level.SEVERE,
                        "Error while creating new instance of data object." +
                        " Restoring panel to previous state." +
                        " Is default constructor present?", ex);
                return;
            }
            dataPanel.setFieldsEditable(true);
            mode = PanelMode.ADD;

            setAddOrCancelButton(Button.CANCEL);


        });

        deleteButton.addActionListener(e -> {
            dataPanel.removeCurrentData();
            restorePanel();
        });
    }

    private void restorePanel() {
        dataPanel.updateCurrentData(dataPanel.dataSupplier.get());
        dataPanel.fillWithData();
        cancelButton.setEnabled(false);
        saveButton.setEnabled(false);
        editButton.setEnabled(true);
        lockExternalComponents(false);
        mode = PanelMode.NONE;
        setAddOrCancelButton(Button.ADD);
        dataPanel.setFieldsEditable(false);
    }
    /**
     * Lock external components when panel is in edit mode
     * @param lock true if components should be locked
     */
    private void lockExternalComponents(boolean lock) {
        externalComponents.forEach(component -> component.setEnabled(lock));
    }

    /**
     * Add external component to lock when panel is in edit mode
     * @param component component to lock
     */
    public void addExternalComponent(JComponent component) {
        externalComponents.add(component);
    }

    public PanelMode getMode() {
        return mode;
    }
    private void setAddOrCancelButton(Button button){
        if(button == Button.ADD){
            addOrCancelPanel.remove(cancelButton);
            addOrCancelPanel.add(addButton, "grow");
        } else {
            addOrCancelPanel.remove(addButton);
            addOrCancelPanel.add(cancelButton, "grow");
        }
        addOrCancelPanel.repaint();
        addOrCancelPanel.revalidate();
    }

    enum Button{
        ADD, CANCEL
    }
}
