package org.krzywanski.panel_v1.manualPanel;

import org.krzywanski.panel_v1.fields.FieldValueController;
import org.krzywanski.panel_v1.fields.StringTextFieldValueController;

import javax.swing.*;

public class FieldControllerResolver {

    public FieldControllerResolver() {
    }

    public FieldValueController<?, ?> findFieldValueController(Class<?> dataClass, JComponent component) {

        switch (dataClass.getName()) {
            case "java.lang.String":
                return gestStringValueControllerFor(component);
        }


        return null;
    }

    public FieldValueController<?, ?> gestStringValueControllerFor(JComponent component) {
        return new StringTextFieldValueController((JTextField) component);
    }
}
