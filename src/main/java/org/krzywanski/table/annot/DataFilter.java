package org.krzywanski.table.annot;

import java.lang.annotation.Repeatable;

@Repeatable(DataFilters.class)
public @interface DataFilter {
    String visibleName();
    String fieldName() default "";

    /**
     * list of values for filter - if you use list then combobox is default controll type
     */
    String[] values();
    /**
     * enum class for filter - if you use enum for filter then combobox is default controll type
     */
    Class<? extends Enum<?>> enumClass();

    ControlType controllType();

}
