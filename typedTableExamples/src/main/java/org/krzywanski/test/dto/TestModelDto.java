package org.krzywanski.test.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.krzywanski.table.annot.MyTableColumn;
import org.krzywanski.table.annot.ReflectionSort;
import org.krzywanski.table.annot.TableFilter;
import org.krzywanski.table.constraints.Alignment;
import org.krzywanski.test.model.TestEnum;
import org.krzywanski.test.model.TestFormatClass;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ReflectionSort
@TableFilter(type = String.class, name = "columnA")
@TableFilter(type = Double.class, name = "columnB", label = "Decimal column")
@TableFilter(type = TestEnum.class, name = "testEnum", label = "Test enum")
@TableFilter(type = Boolean.class, name = "testFormatClass", label = "Boolean value")
@Entity
public class TestModelDto implements Comparable<TestModelDto>, Serializable {

    Integer id;

    //    @CustomRenderer(renderer = BooleanIconRenderer.class)
    @MyTableColumn(label = "Boolean value")
    private Boolean booleanValue = true;

    @NotEmpty(message = "Mobile Number can not be a null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number must be 10 digits")
    @MyTableColumn(label = "XXX", width = 200, sortable = true)
    private String columnA;
    @MyTableColumn(label = "Decimal column", format = "0.00$", sortable = true)
    private BigDecimal columnB;
    @NotEmpty(message = "Test label cannot be empty")
    @Email
    @MyTableColumn(label = "Test label")
    private String columnC;

    @MyTableColumn(label = "DataLable", format = "dd-MM-yyyy", alignment = Alignment.CENTER)
    private Date date = new Date();

    @MyTableColumn(label = "Test enum")
    private TestEnum testEnum = TestEnum.MEDIUM;

    @MyTableColumn(label = "customFormatter")
    TestFormatClass testFormatClass = new TestFormatClass("X");

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

    public BigDecimal getColumnB() {
        return columnB;
    }

    public void setColumnB(BigDecimal columnB) {
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

    @Override
    public int compareTo(TestModelDto o) {
        return columnB.compareTo(o.columnB);
    }


}
