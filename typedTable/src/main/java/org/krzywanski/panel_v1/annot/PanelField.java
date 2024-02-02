package org.krzywanski.panel_v1.annot;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PanelField {
    String label();
}
