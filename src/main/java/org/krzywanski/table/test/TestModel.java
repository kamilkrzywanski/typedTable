package org.krzywanski.table.test;

import org.krzywanski.table.annot.MyTableColumn;

import java.util.Date;

public class TestModel {

    @MyTableColumn(label = "XXX", width = 200)
    public String columnA;

    @MyTableColumn(label = "Decimal column", format = "0.00$")
    public Double columnB;

    @MyTableColumn(label = "Test label")
    public String columnC;

    @MyTableColumn(label = "DataLable", format = "YYYY")
    public Date date = new Date();

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
}
