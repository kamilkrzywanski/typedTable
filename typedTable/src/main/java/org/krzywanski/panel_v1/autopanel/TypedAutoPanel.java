package org.krzywanski.panel_v1.autopanel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.panel_v1.DataFlowController;
import org.krzywanski.panel_v1.FieldController;
import org.krzywanski.panel_v1.fields.FieldValueController;
import org.krzywanski.panel_v1.fields.TableValueController;

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
    private T data;
    protected Supplier<T> dataSupplier;
    private DataFlowController<T> repository;
    final Class<T> dataClass;

    FieldController<T> fieldController;
    final AutoPanelButtons<T> autoPanelButtons;

    public TypedAutoPanel(Supplier<T> dataSupplier, Class<T> dataClass) {
        this.data = dataSupplier.get();
        this.dataSupplier = dataSupplier;
        this.dataClass = dataClass;
        this.fieldController = new FieldController<>(dataClass);
        this.autoPanelButtons = new AutoPanelButtons<>(this);
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
    protected void saveChanges(){
        fieldController.getElements().stream().filter(el -> el.getFieldValueController() != null).forEach((element) -> {
            try {
                element.getPropertyDescriptor().getWriteMethod().invoke(data, element.getFieldValueController().getValue());
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        });

        if(data != null && repository != null){
            if(repository.update(data) != null)
                setFieldsEditable(false);
        }
        listeners.forEach(listener -> listener.valueChanged(data));
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
     * Sets data flow controller for crud operations
     * @param repository - interface for data flow
     */
    public void setDataFlowController(DataFlowController<T> repository) {
        this.repository = repository;
    }

    public void addPanelChangeValueListener(PanelChangeValueListener<T> listener){
        listeners.add(listener);
    }

    public void addExternalComponent(JComponent component){
        autoPanelButtons.addExternalComponent(component);
    }

}
