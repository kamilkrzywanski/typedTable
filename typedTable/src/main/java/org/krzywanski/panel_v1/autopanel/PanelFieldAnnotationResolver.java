package org.krzywanski.panel_v1.autopanel;

import org.krzywanski.table.annot.MyTableColumn;

import java.lang.reflect.Field;

public class PanelFieldAnnotationResolver {

    public String resolveField(Field field) {
        if (field.isAnnotationPresent(MyTableColumn.class)) {
            MyTableColumn annotation = field.getAnnotation(MyTableColumn.class);
            if (!annotation.label().isEmpty()) {
                return annotation.label();
            }
        }
        return null;
    }
}
