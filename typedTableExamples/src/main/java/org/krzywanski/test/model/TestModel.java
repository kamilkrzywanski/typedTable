package org.krzywanski.test.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Date;

@Entity
public class TestModel implements Comparable<TestModel>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String columnA;

    //    @Column(precision = 10, scale = 2)
    private Double columnB;
    private String columnC;
    private Date myDate = new Date();
    private TestEnum testEnum = TestEnum.MEDIUM;
    private Boolean booleanValue = true;
    private String testFormatClass;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getColumnC() {
        return columnC;
    }

    public void setColumnC(String columnC) {
        this.columnC = columnC;
    }


    public Date getMyDate() {
        return myDate;
    }

    public void setMyDate(Date myDate) {
        this.myDate = myDate;
    }

    public TestEnum getTestEnum() {
        return testEnum;
    }

    public void setTestEnum(TestEnum testEnum) {
        this.testEnum = testEnum;
    }

    public String getTestFormatClass() {
        return testFormatClass;
    }

    public void setTestFormatClass(String testFormatClass) {
        this.testFormatClass = testFormatClass;
    }

    @Override
    public int compareTo(TestModel o) {
        return columnB.compareTo(o.columnB);
    }


}
