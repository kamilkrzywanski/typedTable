package org.krzywanski.test.model;

import org.krzywanski.table.annot.MyTableColumn;

public class TestFormatClass {
    @MyTableColumn
    private String value;

    public TestFormatClass(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
