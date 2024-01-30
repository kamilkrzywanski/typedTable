package org.krzywanski.test.model;

public class TestFormatClass {

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
