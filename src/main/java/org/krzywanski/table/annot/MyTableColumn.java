package org.krzywanski.table.annot;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for table labels and other properties
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MyTableColumn {

    /**
     * if you not passed this param then labell will be a field name
     * @return
     */
    String label() default "";

    /**
     * width for column
     * @return
     */
    int width() default 50;

    /**
     * format for table cell
     */
    String format() default "";

}
