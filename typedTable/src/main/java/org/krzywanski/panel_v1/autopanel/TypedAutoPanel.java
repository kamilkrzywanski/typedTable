package org.krzywanski.panel_v1.autopanel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.panel_v1.DataAction;
import org.krzywanski.panel_v1.dataflow.*;
import org.krzywanski.panel_v1.FieldController;
import org.krzywanski.panel_v1.fields.FieldValueController;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Basic implementation of panel for data with auto generated fields
 * @param <T> - type of data
 */
public class TypedAutoPanel<T> extends JPanel {

    List<PanelChangeValueListener<T>> listeners = new ArrayList<>();
    protected T data;
    protected Supplier<T> dataSupplier;
    protected Insert<T> insertRepository;
    protected Remove<T> removeRepository;
    protected Update<T> updateRepository;
    final Class<T> dataClass;

    FieldController<T> fieldController;
    final AutoPanelButtons<T> autoPanelButtons;

    public TypedAutoPanel(Supplier<T> dataSupplier, Class<T> dataClass) {
        this.data = dataSupplier.get();
        this.dataSupplier = dataSupplier;
        this.dataClass = dataClass;
        this.fieldController = new FieldController<>(dataClass);
        this.autoPanelButtons = new AutoPanelButtons<>(this, () -> insertRepository, () -> removeRepository, () -> updateRepository);
        setLayout(new MigLayout());
    }
    public TypedAutoPanel<T> buildPanel(){
        addFields();
        add(new JLabel());
        add(autoPanelButtons, "grow");
        fillWithData();
        return this;
    }

    /**
     * Fills fields with data from data object
     */
    protected void fillWithData() {
        if(autoPanelButtons.getMode() == PanelMode.UPDATE || autoPanelButtons.getMode() == PanelMode.ADD)
            return;

        setFieldsEditable(false);

        //TODO remove filter when all elements will have implemented FieldValueController
        fieldController.getElements().stream().filter(el -> el.getFieldValueController() != null).forEach((element) -> {
            element.getFieldValueController().setValue(() -> {
                try {
                    if(data != null)
                        return element.getPropertyDescriptor().getReadMethod().invoke(data);
                    return null;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        autoPanelButtons.validateButtonsState();
    }

    /**
     * Adds fields to panel
     */
    private void addFields() {
        fieldController.getElements().forEach((element) -> {
            add(element.getFirstComponent(), element.getSecondComponent() != null ? "grow" : "grow, span 2, wrap");
            if(element.getSecondComponent() != null)
                add(element.getSecondComponent(), "grow, wrap");
        });
    }

    /**
     * Saves changes to data object
     */
    protected void saveChanges(DataAction updateOrInsert) {
        fieldController.getElements().stream().filter(el -> el.getFieldValueController() != null).forEach((element) -> {
            try {
                element.getPropertyDescriptor().getWriteMethod().invoke(data, element.getFieldValueController().getValue());
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        });

        if (data != null) {
            switch (updateOrInsert){
                case UPDATE:
                    if (updateRepository != null && updateRepository.update(data) != null)
                        setFieldsEditable(false);
                    break;
                case INSERT:
                    if (insertRepository != null && insertRepository.insert(data) != null)
                        setFieldsEditable(false);
                    break;
            }
        }
        listeners.forEach(listener -> listener.valueChanged(data, updateOrInsert));
    }

    /**
     * Sets fields editable
     * @param enabled true if fields should be editable
     */
    protected void setFieldsEditable(boolean enabled) {
        fieldController.getElements().stream().filter(el -> el.getFieldValueController() != null).forEach((element) -> {
            element.getFieldValueController().setEditable(enabled);
        });
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
            removeRepository.remove(data);
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

    public void addInsertValidator(ControllerValidator validator) {
        autoPanelButtons.addInsertValidator(validator);
    }

    public void addUpdateValidator(ControllerValidator validator) {
        autoPanelButtons.addUpdateValidator(validator);
    }

    public void addRemoveValidator(ControllerValidator validator) {
        autoPanelButtons.addRemoveValidator(validator);
    }
}
