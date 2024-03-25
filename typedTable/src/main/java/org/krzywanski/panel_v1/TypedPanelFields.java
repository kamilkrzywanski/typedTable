package org.krzywanski.panel_v1;

import org.krzywanski.panel_v1.fields.FieldProvider;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for rgeistering custom fields in panel
 */
public class TypedPanelFields {
    public final static Map<Class<?>, FieldProvider<?, ?>> fields = new HashMap<>();

    /**
     * Register field
     * @param dataClass class of data
     * @param fieldValueController field controller
     */
    public static <T, R extends JComponent> void registerField(Class<T> dataClass, FieldProvider<T, R> fieldValueController) {
        fields.put(dataClass, fieldValueController);
    }

    /**
     * Get field
     * @param dataClass class of data
     * @return field controller
     */
    @SuppressWarnings("unchecked")
    public static <T, R extends JComponent> FieldProvider<T, R> getField(Class<T> dataClass) {
        return (FieldProvider<T, R>) fields.get(dataClass);
    }
}
