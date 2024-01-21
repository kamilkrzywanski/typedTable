package org.krzywanski.table.panel.annot;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PanelField {
    String label();
}
