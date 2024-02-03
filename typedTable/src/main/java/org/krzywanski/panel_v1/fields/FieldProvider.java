package org.krzywanski.panel_v1.fields;

import javax.swing.*;
import java.awt.*;

public interface FieldProvider<T> {
    Component getComponent();

    FieldValueController<T, JComponent> getController();
}
