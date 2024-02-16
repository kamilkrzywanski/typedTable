package org.krzywanski.panel_v1.autopanel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.panel_v1.DataAction;
import org.krzywanski.panel_v1.ErrorDialog;
import org.krzywanski.panel_v1.FieldController;
import org.krzywanski.panel_v1.FieldControllerElement;
import org.krzywanski.panel_v1.autopanel.buttons.AutoPanelButtons;
import org.krzywanski.panel_v1.autopanel.buttons.ControllerValidator;
import org.krzywanski.panel_v1.dataflow.DataFlowAdapter;
import org.krzywanski.panel_v1.dataflow.Insert;
import org.krzywanski.panel_v1.dataflow.Remove;
import org.krzywanski.panel_v1.dataflow.Update;
import org.krzywanski.panel_v1.fields.FieldValueController;
import org.krzywanski.panel_v1.validation.FieldValidator;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Basic implementation of panel for data with auto generated fields
 * @param <T> - type of data
 */
public class TypedAutoPanel<T> extends JPanel {

    final List<PanelChangeValueListener<T>> listeners = new ArrayList<>();

    protected T data;
    final protected Supplier<T> dataSupplier;
    protected Insert<T> insertRepository;
    protected Remove<T> removeRepository;
    protected Update<T> updateRepository;
    final Class<T> dataClass;

    final FieldController<T> fieldController;
    final AutoPanelButtons<T> autoPanelButtons;

    final JPanel fieldsPanel = new JPanel(new MigLayout());

    public TypedAutoPanel(Supplier<T> dataSupplier, Class<T> dataClass) {
        this.data = dataSupplier.get();
        this.dataSupplier = dataSupplier;
        this.dataClass = dataClass;
        this.fieldController = new FieldController<>(dataClass, this);
        this.autoPanelButtons = new AutoPanelButtons<>(this, () -> insertRepository, () -> removeRepository, () -> updateRepository);
        setLayout(new MigLayout("debug, fill"));
    }

    public TypedAutoPanel<T> buildPanel(){
        return buildPanel(1);
    }

    public TypedAutoPanel<T> buildPanel(int rows) {
        addFields(rows);
        add(fieldsPanel, "grow");
        add(new JLabel(), "wrap");
        add(autoPanelButtons, "right");
        fillWithData();
        return this;
    }

    /**
     * Fills fields with data from data object
     */
    public void fillWithData() {
        if (autoPanelButtons.getMode() == PanelMode.UPDATE)
            return;

        setFieldsEditable(false);


        //TODO remove filter when all elements will have implemented FieldValueController
        fieldController.getElements().stream().filter(el -> el.getFieldValueController() != null)
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
     * Adds fields to panel
     */
    private void addFields(int rows) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        fieldController.getElements().forEach((element) -> {
            final boolean isWrap = atomicInteger.getAndIncrement() % rows == 0;

            fieldsPanel.add(element.getFirstComponent(), element.getSecondComponent() != null ? "" : "span 2" + (isWrap ? ",wrap" : ""));
            if(element.getSecondComponent() != null)
                fieldsPanel.add(element.getSecondComponent(), "grow, push" + (isWrap ? ",wrap" : ""));
        });
    }

    /**
     * Saves changes to data object
     */
    public boolean saveChanges(DataAction updateOrInsert) {

        if (!validateChanges())
            return false;

        fieldController.getElements().stream().filter(el -> el.getFieldValueController() != null).forEach((element) -> {
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
            switch (updateOrInsert){
                case UPDATE:
                    try {
                        if (updateRepository != null && updateRepository.update(data) != null)
                            setFieldsEditable(false);
                        break;
                    }catch (Exception e){
                        ErrorDialog.showErrorDialog(this, e.getMessage(), e);
                       return false;
                    }
                case INSERT:
                    try {
                        if (insertRepository != null && insertRepository.insert(data) != null)
                            setFieldsEditable(false);
                        break;
                    }catch (Exception e){
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
        for (FieldControllerElement element : fieldController.getElements()) {
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
     * @param enabled true if fields should be editable
     */
    public void setFieldsEditable(boolean enabled) {
        fieldController
                .getElements()
                .stream()
                .filter(el -> el.getFieldValueController() != null)
                .forEach((element) -> element.getFieldValueController().setEditable(enabled));
    }

    /**
     * Adds data editor to field
     * @param fieldName name of field
     * @param columnClass class of column
     * @param fieldValueController field value controller
     * @param <R> type of column
     */
    public <R> void addDataEditor(String fieldName, Class<R> columnClass, FieldValueController<? extends R, ?> fieldValueController) {
        fieldController.addDataEditor(fieldName, columnClass, fieldValueController);
    }

    /**
     * Update fields with passed data
     * @param data data
     */
    public void updateCurrentData(T data) {
        this.data = data;
        fillWithData();
    }

    /**
     *
     */
    public void removeCurrentData() {
        if (data != null && removeRepository != null) {
            try {
                removeRepository.remove(data);
            }catch (Exception e){
                ErrorDialog.showErrorDialog(this, e.getMessage(), e);
                return;
            }
            listeners.forEach(listener -> listener.valueChanged(null, DataAction.REMOVE));
        }
    }

    /**
     * Sets data flow controller for all crud operations
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

    public void addPanelChangeValueListener(PanelChangeValueListener<T> listener){
        listeners.add(listener);
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

    public void resetBorder() {
        fieldController.getElements().forEach((element) -> element.getFieldValueController().resetBorder());
    }

    public Class<T> getDataClass() {
        return dataClass;
    }

    public Supplier<T> getDataSupplier() {
        return dataSupplier;
    }

    public void hideValidationHints() {
        fieldController.getElements().forEach(FieldControllerElement::hideValidationHint);
    }

    public void validateFields() {
        fieldController.getElements().forEach(FieldControllerElement::validate);
    }

    public T getData() {
        return data;
    }
}
