package org.krzywanski.table.test;

import org.krzywanski.table.annot.MyTableColumn;

import java.util.Date;

public class TestModel {

    @MyTableColumn(label = "XXX")
    public String columnA;

    public String columnB;

    @MyTableColumn(label = "Test label")
    public String columnC;

    @MyTableColumn(label = "Test label2", width = 250)
    public String columnD;

    public String columnE;

    @MyTableColumn(label = "Test label3", width = 220)
    public String columnF;

    @MyTableColumn(label = "DataLable", width = 220, format = "YYYY")
    public Date date = new Date();

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

    public String getColumnE() {
        return columnE;
    }

    public void setColumnE(String columnE) {
        this.columnE = columnE;
    }

    public String getColumnF() {
        return columnF;
    }

    public void setColumnF(String columnF) {
        this.columnF = columnF;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
