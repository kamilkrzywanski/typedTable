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

/**
 * Basic implementation of panel for data with auto generated fields
 * @param <T> - type of data
 */
public class TypedAutoPanel<T> extends JPanel {

    List<PanelChangeValueListener<T>> listeners = new ArrayList<>();
    private T data;
    private DataFlowController<T> repository;

    FieldController<T> fieldController;

    public TypedAutoPanel(T data, Class<T> dataClass) {
        this.data = data;
        this.fieldController = new FieldController<>(dataClass);
        setLayout(new MigLayout());
    }
    public TypedAutoPanel<T> buildPanel(){
        addFields();
        add(new AutoPanelButtons<>(this), "grow, span 3");
        fillWithData();
        return this;
    }

    protected void fillWithData() {
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

    private void addFields() {
        fieldController.getElements().forEach((element) -> {
            add(element.getFirstComponent(), element.getSecondComponent() != null ? "grow" : "grow, span 2, wrap");
            if(element.getSecondComponent() != null)
                add(element.getSecondComponent(), "grow, wrap");
        });
    }

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

    protected void setFieldsEditable(boolean enabled) {
        fieldController.getElements().stream().filter(el -> el.getFieldValueController() != null).forEach((element) -> {
            element.getFieldValueController().setEditable(enabled);
        });
    }

    public <R> void addDataEditor(String fieldName, Class<R> columnClass, FieldValueController<? extends R, ?> fieldValueController) {
        fieldController.addDataEditor(fieldName, columnClass, fieldValueController);
    }

    public void updateCurrentData(T data) {
        this.data = data;
        fillWithData();
    }


    public void setDataFlowController(DataFlowController<T> repository) {
        this.repository = repository;
    }

    public void addPanelChangeValueListener(PanelChangeValueListener<T> listener){
        listeners.add(listener);
    }

}
