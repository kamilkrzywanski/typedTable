package org.krzywanski.table.test;

import org.krzywanski.table.annot.EnableMultiSort;
import org.krzywanski.table.annot.MyTableColumn;

import java.util.Date;

@EnableMultiSort
public class TestModel {

    @MyTableColumn(label = "XXX", width = 200, sortable = true)
    private String columnA;

    @MyTableColumn(label = "Decimal column", format = "0.00$", sortable = true)
    private Double columnB;

    @MyTableColumn(label = "Test label")
    private String columnC;

    @MyTableColumn(label = "DataLable", format = "YYYY")
    private Date date = new Date();

    @MyTableColumn(label = "Test enum")
    private TestEnum testEnum = TestEnum.MEDIUM;

    @MyTableColumn(label = "customFormatter")
    TestFormatClass testFormatClass = new TestFormatClass();

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

    public String getColumnC() {
        return columnC;
    }

    public void setColumnC(String columnC) {
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
