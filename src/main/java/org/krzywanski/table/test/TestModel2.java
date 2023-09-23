package org.krzywanski.table.test;

import org.krzywanski.table.annot.MyTableColumn;

import java.util.Date;

public class TestModel2 {

    @MyTableColumn(width = 200)
    private String columnA;

    @MyTableColumn(format = "0.00$")
    private Double columnB;

    @MyTableColumn()
    private String columnC;

    @MyTableColumn()
    private Date date = new Date();

    @MyTableColumn()
    private TestEnum testEnum = TestEnum.MEDIUM;

    @MyTableColumn()
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
}
