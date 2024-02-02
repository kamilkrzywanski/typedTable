package org.krzywanski.panel_v1;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

public class TypedPanel<T> extends JPanel {
    
    private final T data;

    private Class<T> dataClass;

    FieldController<T> fieldController;

    @SuppressWarnings("unchecked")
    public TypedPanel(T data) {
        this.data = data;
        this.dataClass = (Class<T>) data.getClass();
        this.fieldController = new FieldController<>(dataClass);
        setLayout(new MigLayout("debug"));
        initComponents();
        fillWithData(data);
    }

    private void fillWithData(T data) {

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

    private void initComponents() {
        fieldController.getElements().forEach((element) -> {
            add(element.getFirstComponent(), element.getSecondComponent() != null ? "grow" : "grow, span 2, wrap");
            if(element.getSecondComponent() != null)
                add(element.getSecondComponent(), "grow, wrap");
        });
    }


}
