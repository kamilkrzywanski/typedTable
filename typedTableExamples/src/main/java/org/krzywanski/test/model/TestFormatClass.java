package org.krzywanski.test.model;

import org.krzywanski.table.annot.MyTableColumn;

public class TestFormatClass {

    @MyTableColumn
    private String value = "test";

    @Override
    public String toString() {
        return "FormatClass{}";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
