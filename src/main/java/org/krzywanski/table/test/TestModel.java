package org.krzywanski.table.test;

import org.krzywanski.table.annot.MyTableColumn;

public class TestModel {


    @MyTableColumn()
    String columnA;

    String columnB;

    @MyTableColumn(label = "Test label")
    String columnC;

}
