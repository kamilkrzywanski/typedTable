package org.krzywanski.panel_v1.autopanel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.panel_v1.FieldController;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Basic implementation of panel for data with auto generated fields
 * @param <T> - type of data
 */
public class TypedAutoPanel<T> extends JPanel {
    
    private T data;

    private final Class<T> dataClass;

    FieldController<T> fieldController;

    @SuppressWarnings("unchecked")
    public TypedAutoPanel(T data) {
        this.data = data;
        this.dataClass = (Class<T>) data.getClass();
        this.fieldController = new FieldController<>(dataClass);
        setLayout(new MigLayout());

    }
    public TypedAutoPanel<T> buildPanel(){
        addFields();
        add(new AutoPanelButtons<>(this));
        fillWithData();
        return this;
    }

    protected void fillWithData() {
        fieldController.getElements().stream().filter(el -> el.getFieldValueController() != null).forEach((element) -> {
            element.getFieldValueController().setEditable(false);
        });

        //TODO remove filter when all elements will have implemented FieldValueController
        fieldController.getElements().stream().filter(el -> el.getFieldValueController() != null).forEach((element) -> {
            element.getFieldValueController().setValue(() -> {
                try {
                   return element.getPropertyDescriptor().getReadMethod().invoke(data);
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
    }

    protected void setFieldsEditable(boolean enabled) {
        fieldController.getElements().stream().filter(el -> el.getFieldValueController() != null).forEach((element) -> {
            element.getFieldValueController().setEditable(enabled);
        });
    }

    public <R> void addDataEditor(String columnA, R columnClass) {
    }
}
