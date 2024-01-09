package org.krzywanski.table.annot;

import javax.swing.table.TableCellRenderer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom renderer for table column
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CustomRenderer {
    Class<? extends TableCellRenderer> renderer();
}
