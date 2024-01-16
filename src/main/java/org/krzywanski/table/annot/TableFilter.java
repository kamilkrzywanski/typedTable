package org.krzywanski.table.annot;

import java.lang.annotation.Repeatable;

/**
 * Filters for table
 */
@Repeatable(TableFilters.class)
public @interface TableFilter {
    Class<?> type();
    String name();
    String label() default "";
}
