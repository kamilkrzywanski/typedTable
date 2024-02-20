package org.krzywanski.panel_v1;

import org.krzywanski.panel_v1.autopanel.PanelChangeValueListener;
import org.krzywanski.panel_v1.autopanel.PanelMode;
import org.krzywanski.panel_v1.autopanel.buttons.AutoPanelButtons;
import org.krzywanski.panel_v1.autopanel.buttons.ControllerValidator;
import org.krzywanski.panel_v1.dataflow.DataFlowAdapter;
import org.krzywanski.panel_v1.dataflow.Insert;
import org.krzywanski.panel_v1.dataflow.Remove;
import org.krzywanski.panel_v1.dataflow.Update;
import org.krzywanski.panel_v1.fields.FieldBuilder;
import org.krzywanski.panel_v1.fields.FieldControllerElement;
import org.krzywanski.panel_v1.fields.FieldValueController;
import org.krzywanski.panel_v1.validation.FieldValidator;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public abstract class AbstractTypedPanel<T> extends JPanel {
    protected FieldBuilder<T> fieldController;
    protected T data;
    final protected Supplier<T> dataSupplier;
    protected Insert<T> insertRepository;
    protected Remove<T> removeRepository;
    protected Update<T> updateRepository;
    final Class<T> dataClass;
    protected AutoPanelButtons<T> autoPanelButtons;
    final List<PanelChangeValueListener<T>> listeners = new ArrayList<>();


    public AbstractTypedPanel(Supplier<T> dataSupplier, Class<T> dataClass) {
        super();
        this.dataSupplier = dataSupplier;
        this.dataClass = dataClass;
        this.data = dataSupplier.get();
    }

    /**
     * Saves changes to data object
     */
    public boolean saveChanges(DataAction updateOrInsert) {

        if (!validateChanges())
            return false;

        fieldController.getComponents().stream().filter(el -> el.getFieldValueController() != null).forEach((element) -> {
            try {
                element.getPropertyDescriptor().getWriteMethod().invoke(data, element.getFieldValueController().getValue());
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        });


        FieldValidator<T> fieldValidator = new FieldValidator<>();
        Set<String> validationResult = fieldValidator.validateBean(data);
        if (!validationResult.isEmpty()) {
            JOptionPane.showMessageDialog(this, validationResult.iterator().next(), "Validation error", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        if (data != null) {
            switch (updateOrInsert) {
                case UPDATE:
                    try {
                        if (updateRepository != null && updateRepository.update(data) != null)
                            setFieldsEditable(false);
                        break;
                    } catch (Exception e) {
                        ErrorDialog.showErrorDialog(this, e.getMessage(), e);
                        return false;
                    }
                case INSERT:
                    try {
                        if (insertRepository != null && insertRepository.insert(data) != null)
                            setFieldsEditable(false);
                        break;
                    } catch (Exception e) {
                        ErrorDialog.showErrorDialog(this, e.getMessage(), e);
                        return false;
                    }
            }
        }
        listeners.forEach(listener -> listener.valueChanged(data, updateOrInsert));
        return true;
    }

    private boolean validateChanges() {
        boolean allFieldsValid = true;
        FieldValidator<T> fieldValidator = new FieldValidator<>();
        for (FieldControllerElement element : fieldController.getComponents()) {
            if (element.getFieldValueController() != null) {
                Set<String> validationResult = fieldValidator.validateField(dataClass, element);

                if (!validationResult.isEmpty()) {
                    if (element.getValidationDialog() != null)
                        element.getValidationDialog().showErrorWindow(validationResult.iterator().next());
                    allFieldsValid = false;
                }
            }
        }
        return allFieldsValid;
    }

    /**
     * Sets fields editable
     *
     * @param enabled true if fields should be editable
     */
    public void setFieldsEditable(boolean enabled) {
        fieldController
                .getComponents()
                .stream()
                .filter(el -> el.getFieldValueController() != null)
                .forEach((element) -> element.getFieldValueController().setEditable(enabled));
    }

    public void addPanelChangeValueListener(PanelChangeValueListener<T> listener) {
        listeners.add(listener);
    }

    public void resetBorder() {
        fieldController.getComponents().forEach((element) -> element.getFieldValueController().resetBorder());
    }

    public Class<T> getDataClass() {
        return dataClass;
    }

    public Supplier<T> getDataSupplier() {
        return dataSupplier;
    }

    public void hideValidationHints() {
        fieldController.getComponents().forEach(FieldControllerElement::hideValidationHint);
    }

    public void validateFields() {
        fieldController.getComponents().forEach(FieldControllerElement::validate);
    }

    /**
     *
     */
    public void removeCurrentData() {
        if (data != null && removeRepository != null) {
            try {
                removeRepository.remove(data);
            } catch (Exception e) {
                ErrorDialog.showErrorDialog(this, e.getMessage(), e);
                return;
            }
            listeners.forEach(listener -> listener.valueChanged(null, DataAction.REMOVE));
        }
    }

    /**
     * Fills fields with data from data object
     */
    public void fillWithData() {
        if (autoPanelButtons.getMode() == PanelMode.UPDATE)
            return;

        setFieldsEditable(false);


        //TODO remove filter when all elements will have implemented FieldValueController
        fieldController.getComponents().stream().filter(el -> el.getFieldValueController() != null)
                .forEach((element) -> element.getFieldValueController().setValue(() -> {
                    try {
                        if (data != null)
                            return element.getPropertyDescriptor().getReadMethod().invoke(data);
                        return null;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }));
        autoPanelButtons.validateButtonsState();

    }

    /**
     * Update fields with passed data
     *
     * @param data data
     */
    public void updateCurrentData(T data) {
        this.data = data;
        fillWithData();
    }

    public T getData() {
        return data;
    }

    /**
     * Adds data editor to field
     *
     * @param fieldName            name of field
     * @param columnClass          class of column
     * @param fieldValueController field value controller
     * @param <R>                  type of column
     */
    public <R> void addDataEditor(String fieldName, Class<R> columnClass, FieldValueController<? extends R, ?> fieldValueController) {
        fieldController.addDataEditor(fieldName, columnClass, fieldValueController);
    }

    /**
     * Sets data flow controller for all crud operations
     *
     * @param repository - interface for data flow
     */
    public void setDataFlowAdapter(DataFlowAdapter<T> repository) {
        this.insertRepository = repository;
        this.removeRepository = repository;
        this.updateRepository = repository;
        autoPanelButtons.validateButtonsState();
    }

    public void setInsertRepository(Insert<T> insertRepository) {
        this.insertRepository = insertRepository;
        autoPanelButtons.validateButtonsState();
    }

    public void setRemoveRepository(Remove<T> removeRepository) {
        this.removeRepository = removeRepository;
        autoPanelButtons.validateButtonsState();
    }

    public void setUpdateRepository(Update<T> updateRepository) {
        this.updateRepository = updateRepository;
        autoPanelButtons.validateButtonsState();
    }

    public void addExternalComponentToLock(JComponent component) {
        autoPanelButtons.addExternalComponentToLock(component);
    }

    public void addInsertValidator(ControllerValidator<T> validator) {
        autoPanelButtons.addInsertValidator(validator);
    }

    public void addUpdateValidator(ControllerValidator<T> validator) {
        autoPanelButtons.addUpdateValidator(validator);
    }

    public void addRemoveValidator(ControllerValidator<T> validator) {
        autoPanelButtons.addRemoveValidator(validator);
    }

    public PanelMode getMode() {
        return autoPanelButtons.getMode();
    }
}
