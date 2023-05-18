package org.krzywanski.table.test;

import org.krzywanski.table.annot.MyTableColumn;

public class TestModel {


    @MyTableColumn(label = "XXX")
    public String columnA;

    public String columnB;

    @MyTableColumn(label = "Test label")
    public String columnC;

    @MyTableColumn(label = "Test label2", width = 250)
    public String columnD;


    public String getColumnA() {
        return columnA;
    }

    public void setColumnA(String columnA) {
        this.columnA = columnA;
    }

    public String getColumnB() {
        return columnB;
    }

    public void setColumnB(String columnB) {
        this.columnB = columnB;
    }

    public String getColumnC() {
        return columnC;
    }

    public void setColumnC(String columnC) {
        this.columnC = columnC;
    }

    public String getColumnD() {
        return columnD;
    }

    public void setColumnD(String columnD) {
        this.columnD = columnD;
    }
}
