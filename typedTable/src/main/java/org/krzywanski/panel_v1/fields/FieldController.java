package org.krzywanski.panel_v1.fields;

import java.util.List;

/**
 * Controller for panel field
 * create borders on validation error
 * Pass data to panel
 * Get data from panel
 */
public class FieldController<T> {
    final FieldBuilder<T> fieldBuilder;

    /**
     * Constructor
     */
    public FieldController(FieldBuilder<T> panelFieldCreator) {
        this.fieldBuilder = panelFieldCreator;
    }

    public List<FieldControllerElement> getElements() {
        return fieldBuilder.getComponents();
    }

    public <R> void addDataEditor(String fieldName, Class<R> columnClass, FieldValueController<? extends R,?> fieldValueController) {
        fieldBuilder.addDataEditor(fieldName, columnClass, fieldValueController);
    }
}
