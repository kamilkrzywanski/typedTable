package org.krzywanski.table.annot;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ReflectionSort {
    /**
     * Enable sort by all possible columns by default
     *
     * @return - true when sort all columns by default
     */
    boolean allSortable() default false;
}