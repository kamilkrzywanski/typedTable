package org.krzywanski.panel_v1.fields;

import java.util.List;

public interface FieldBuilder<T> {
    /**
     * Get list of components for panel
     *
     * @return list of components
     */
    List<FieldControllerElement> getComponents();

    /**
     * Add data editor to panel
     *
     * @param fieldName            field name
     * @param columnClass          class of column
     * @param fieldValueController controller for field
     * @param <R>                  type of column
     */
    <R> void addDataEditor(String fieldName, Class<R> columnClass, FieldValueController<? extends R, ?> fieldValueController);
}
