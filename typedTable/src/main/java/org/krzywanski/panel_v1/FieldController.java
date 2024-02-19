package org.krzywanski.panel_v1;

import org.krzywanski.panel_v1.autopanel.FieldBuilder;
import org.krzywanski.panel_v1.autopanel.TypedAutoPanel;
import org.krzywanski.panel_v1.fields.FieldValueController;

import java.util.List;

/**
 * Controller for panel field
 * create borders on validation error
 * Pass data to panel
 * Get data from panel
 */
public class FieldController<T> {
    final FieldBuilder<T> panelFieldCreator;

    /**
     * Constructor
     * @param dataClass class of data
     */
    public FieldController(Class<T> dataClass, TypedAutoPanel<T> parentPanel, FieldBuilder<T> panelFieldCreator) {
        this.panelFieldCreator = panelFieldCreator;
    }

    public List<FieldControllerElement> getElements() {
        return panelFieldCreator.getComponents();
    }

    public <R> void addDataEditor(String fieldName, Class<R> columnClass, FieldValueController<? extends R,?> fieldValueController) {
        panelFieldCreator.addDataEditor(fieldName, columnClass, fieldValueController);
    }
}
