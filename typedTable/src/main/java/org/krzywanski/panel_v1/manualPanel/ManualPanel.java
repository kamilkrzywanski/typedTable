package org.krzywanski.panel_v1.manualPanel;

import net.miginfocom.swing.MigLayout;
import org.krzywanski.panel_v1.AbstractTypedPanel;
import org.krzywanski.panel_v1.FieldToolKit;
import org.krzywanski.panel_v1.autopanel.buttons.AutoPanelButtons;
import org.krzywanski.panel_v1.fields.FieldBuilder;
import org.krzywanski.panel_v1.fields.FieldControllerElement;
import org.krzywanski.panel_v1.fields.FieldValueController;

import javax.swing.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ManualPanel<T> extends AbstractTypedPanel<T> {
    List<FieldControllerElement> components = new ArrayList<>();

    public ManualPanel(Supplier<T> dataSupplier, Class<T> dataClass) {
        super(dataSupplier, dataClass);
        setLayout(new MigLayout("fill"));
        add(autoPanelButtons);
    }

    @Override
    protected FieldBuilder<T> createFieldController(Class<T> dataClass) {
        return new FieldBuilder<T>() {
            @Override
            public List<FieldControllerElement> getComponents() {
                return components;
            }

            @Override
            public <R> void addDataEditor(String fieldName, Class<R> columnClass, FieldValueController<? extends R, ?> fieldValueController) {

            }
        };
    }


    public void connectFieldWithPanel(String fieldName, JTextField component) {

        Field field = Arrays.stream(getDataClass().getDeclaredFields())
                .filter(f -> f.getName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Field %s not found in class %s", fieldName, getDataClass())));
        PropertyDescriptor propertyDescriptor;
        try {
            propertyDescriptor = new PropertyDescriptor(fieldName, getDataClass());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        FieldValueController<?, ?> fieldValueController = new FieldControllerResolver().findFieldValueController(field.getType(), component);


        FieldControllerElement fieldControllerElement = new FieldControllerElement(this, field, propertyDescriptor);
        fieldControllerElement.setFieldValueController(fieldValueController);
        fieldControllerElement.setFirstComponent(component);
        FieldToolKit.installDefaultEditorKit(component);
        FieldToolKit.installDocumentListener(fieldControllerElement);
        components.add(fieldControllerElement);
        fillWithData();
    }

    public AutoPanelButtons<T> getButtonsForPanel() {
        return autoPanelButtons;
    }
}
