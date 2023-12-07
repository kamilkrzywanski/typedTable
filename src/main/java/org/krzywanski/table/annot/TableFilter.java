package org.krzywanski.table.annot;

import java.lang.annotation.Repeatable;

/**
 * Filter dla tabelki
 */
@Repeatable(TableFilters.class)
public @interface TableFilter {
    Class<?> type();
    String name();
    String label() default "";
}
