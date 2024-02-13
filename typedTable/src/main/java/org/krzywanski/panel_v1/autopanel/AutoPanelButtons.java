package org.krzywanski.panel_v1.autopanel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.panel_v1.DataAction;
import org.krzywanski.panel_v1.dataflow.ControllerValidator;
import org.krzywanski.panel_v1.dataflow.Insert;
import org.krzywanski.panel_v1.dataflow.Remove;
import org.krzywanski.panel_v1.dataflow.Update;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoPanelButtons<T> extends JPanel {
    final List<ControllerValidator<T>> insertValidators = new ArrayList<>();
    final List<ControllerValidator<T>> updateValidators = new ArrayList<>();
    final List<ControllerValidator<T>> removeValidators = new ArrayList<>();
    ResourceBundle resourceBundle = ResourceBundle.getBundle("PanelBundle", Locale.getDefault());
    final TypedAutoPanel<T> dataPanel;
    JButton cancelButton = new JButton(resourceBundle.getString("panel.cancel.button"));
    JButton editButton = new JButton(resourceBundle.getString("panel.edit.button"));
    JButton saveButton = new JButton(resourceBundle.getString("panel.save.button"));
    JButton addButton = new JButton(resourceBundle.getString("panel.add.button"));
    JButton removeButton = new JButton(resourceBundle.getString("panel.remove.button"));
    JPanel addOrCancelPanel = new JPanel(new MigLayout("gapx 0, insets 0"));
    JPanel removeOrSavePanel = new JPanel(new MigLayout("gapx 0, insets 0"));
    List<JComponent> externalComponents = new ArrayList<>();
    PanelMode mode = PanelMode.NONE;
    final Supplier<Insert<?>> insertSupplier;
    final Supplier<Remove<?>> removeSupplier;
    final Supplier<Update<?>> updateSupplier;

    public AutoPanelButtons(TypedAutoPanel<T> dataPanel, Supplier<Insert<?>> insertSupplier, Supplier<Remove<?>> removeSupplier, Supplier<Update<?>> updateSupplier) {
        super(new MigLayout("gapx 0, insets 0"));
        this.dataPanel = dataPanel;
        this.insertSupplier = insertSupplier;
        this.removeSupplier = removeSupplier;
        this.updateSupplier = updateSupplier;
        addControllButtons();
    }


    private void addControllButtons() {

        cancelButton.setEnabled(false);
//        addOrCancelPanel.add(addButton, "grow");

        setAddOrCancelButton(AddOrCancel.ADD);

        add(addOrCancelPanel, "grow");



        add(editButton, "grow");

        cancelButton.setEnabled(false);

        setRemoveOrSaveButton(RemoveOrSave.REMOVE);
        add(removeOrSavePanel, "grow, span 3");

        installListeners();
    }

    private void installListeners() {
        saveButton.addActionListener(e -> {

            cancelButton.setEnabled(false);
            saveButton.setEnabled(false);
            editButton.setEnabled(validateAndInsertTooltip(updateValidators, editButton));
            lockExternalComponents(true);
            setAddOrCancelButton(AddOrCancel.ADD);
            setRemoveOrSaveButton(RemoveOrSave.REMOVE);


            dataPanel.setFieldsEditable(false);
            dataPanel.saveChanges(mode == PanelMode.ADD ? DataAction.INSERT : DataAction.UPDATE);
            mode = PanelMode.NONE;
        });

        editButton.addActionListener(e -> {

            cancelButton.setEnabled(true);
            saveButton.setEnabled(true);
            editButton.setEnabled(false);
            lockExternalComponents(false);
            mode = PanelMode.UPDATE;
            setAddOrCancelButton(AddOrCancel.CANCEL);
            setRemoveOrSaveButton(RemoveOrSave.SAVE);

            dataPanel.setFieldsEditable(true);
        });

        cancelButton.addActionListener(e -> {

            saveButton.setEnabled(false);
            editButton.setEnabled(validateAndInsertTooltip(updateValidators, editButton));
            cancelButton.setEnabled(false);
            lockExternalComponents(true);


            mode = PanelMode.NONE;
            setAddOrCancelButton(AddOrCancel.ADD);
            setRemoveOrSaveButton(RemoveOrSave.REMOVE);

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

            setAddOrCancelButton(AddOrCancel.CANCEL);
            setRemoveOrSaveButton(RemoveOrSave.SAVE);


        });

        removeButton.addActionListener(e -> {
            dataPanel.removeCurrentData();
            restorePanel();
        });
    }

    private void restorePanel() {
        dataPanel.updateCurrentData(dataPanel.dataSupplier.get());
        dataPanel.fillWithData();
        cancelButton.setEnabled(false);
        saveButton.setEnabled(false);
        editButton.setEnabled(validateAndInsertTooltip(updateValidators, editButton));
        lockExternalComponents(false);
        mode = PanelMode.NONE;
        setAddOrCancelButton(AddOrCancel.ADD);
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
    public void addExternalComponentToLock(JComponent component) {
        externalComponents.add(component);
    }

    public PanelMode getMode() {
        return mode;
    }
    private void setAddOrCancelButton(AddOrCancel button){
        if(button == AddOrCancel.ADD){
            addOrCancelPanel.remove(cancelButton);
            addOrCancelPanel.add(addButton, "grow");
            addButton.setEnabled(insertSupplier.get() != null && validateAndInsertTooltip(insertValidators, addButton));
        } else {
            addOrCancelPanel.remove(addButton);
            addOrCancelPanel.add(cancelButton, "grow");
            cancelButton.setEnabled(updateSupplier.get() != null);
        }
        addOrCancelPanel.repaint();
        addOrCancelPanel.revalidate();
    }

    private void setRemoveOrSaveButton(RemoveOrSave button) {
        if (button == RemoveOrSave.REMOVE) {
            removeOrSavePanel.remove(saveButton);
            removeOrSavePanel.add(removeButton, "grow");
            removeButton.setEnabled(removeSupplier.get() != null && validateAndInsertTooltip(removeValidators, removeButton));
        } else {
            removeOrSavePanel.remove(removeButton);
            removeOrSavePanel.add(saveButton, "grow");
            saveButton.setEnabled(
                    mode == PanelMode.UPDATE ? updateSupplier.get() != null && validateAndInsertTooltip(updateValidators, saveButton)
                            : insertSupplier.get() != null && validateAndInsertTooltip(insertValidators, saveButton));
        }
        removeOrSavePanel.repaint();
        removeOrSavePanel.revalidate();
    }

    /**
     * Validates buttons state in case of some external changes at interfaces
     */
    public void validateButtonsState() {
        if (mode == PanelMode.NONE) {
            setAddOrCancelButton(AddOrCancel.ADD);
            setRemoveOrSaveButton(RemoveOrSave.REMOVE);
        } else if (mode == PanelMode.ADD) {
            setAddOrCancelButton(AddOrCancel.CANCEL);
            setRemoveOrSaveButton(RemoveOrSave.SAVE);
        } else {
            setAddOrCancelButton(AddOrCancel.CANCEL);
            setRemoveOrSaveButton(RemoveOrSave.SAVE);
        }
        editButton.setEnabled(updateSupplier.get() != null && validateAndInsertTooltip(updateValidators, editButton));
    }

    enum AddOrCancel {
        ADD, CANCEL
    }

    enum RemoveOrSave {
        REMOVE, SAVE
    }

    public void addInsertValidator(ControllerValidator<T> validator) {
        insertValidators.add(validator);
    }

    public void addUpdateValidator(ControllerValidator<T> validator) {
        updateValidators.add(validator);
    }

    public void addRemoveValidator(ControllerValidator<T> validator) {
        removeValidators.add(validator);
    }


    private boolean validateAndInsertTooltip(List<ControllerValidator<T>> validators, JButton button) {
        AtomicBoolean result = new AtomicBoolean(true);

        validators.stream().filter(controllerValidator -> !controllerValidator.validate().apply(dataPanel.data)).findFirst().ifPresent(controllerValidator -> {
            result.set(false);
            button.setToolTipText(controllerValidator.getMessage());
        });

        return result.get();
    }
}
