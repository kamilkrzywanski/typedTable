package org.krzywanski.table.test;

import org.krzywanski.table.annot.CustomRenderer;
import org.krzywanski.table.annot.MyTableColumn;
import org.krzywanski.table.annot.ReflectionSort;
import org.krzywanski.table.annot.TableFilter;
import org.krzywanski.table.table.Alignment;

import java.util.Date;

@ReflectionSort
@TableFilter(type = String.class, name = "columnA")
@TableFilter(type = Double.class, name = "columnB", label = "Decimal column")
@TableFilter(type = TestEnum.class, name = "testEnum", label = "Test enum")
@TableFilter(type = Boolean.class, name = "testFormatClass", label = "Boolean value")
public class TestModel {

    @MyTableColumn(label = "XXX", width = 200, sortable = true)
    private String columnA;

    @MyTableColumn(label = "Decimal column", format = "0.00$", sortable = true)
    private Double columnB;

    @MyTableColumn(label = "Test label")
    private Integer columnC;

    @MyTableColumn(label = "DataLable", format = "YYYY", alignment = Alignment.CENTER)
    private Date date = new Date();

    @MyTableColumn(label = "Test enum")
    private TestEnum testEnum = TestEnum.MEDIUM;

    @MyTableColumn(label = "customFormatter")
    TestFormatClass testFormatClass = new TestFormatClass();

    @CustomRenderer(renderer = BooleanIconRenderer.class)
    @MyTableColumn(label = "Boolean value")
    private Boolean booleanValue = true;

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
    public String getColumnA() {
        return columnA;
    }

    public void setColumnA(String columnA) {
        this.columnA = columnA;
    }

    public Double getColumnB() {
        return columnB;
    }

    public void setColumnB(Double columnB) {
        this.columnB = columnB;
    }

    public Integer getColumnC() {
        return columnC;
    }

    public void setColumnC(Integer columnC) {
        this.columnC = columnC;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TestFormatClass getTestFormatClass() {
        return testFormatClass;
    }

    public void setTestFormatClass(TestFormatClass testFormatClass) {
        this.testFormatClass = testFormatClass;
    }


    public TestEnum getTestEnum() {
        return testEnum;
    }

    public void setTestEnum(TestEnum testEnum) {
        this.testEnum = testEnum;
    }
}
