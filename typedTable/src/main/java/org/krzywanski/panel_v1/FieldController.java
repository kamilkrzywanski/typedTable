package org.krzywanski.panel_v1;

import java.util.List;

/**
 * Controller for panel field
 * create borders on validation error
 * Pass data to panel
 * Get data from panel
 */
public class FieldController<T> {
    final List<FieldControllerElement> elements;

    /**
     * Constructor
     * @param dataClass class of data
     */
    public FieldController(Class<T> dataClass) {
        this.elements = new PanelFieldCreator(dataClass).getComponents();
    }

    public List<FieldControllerElement> getElements() {
        return elements;
    }
}
