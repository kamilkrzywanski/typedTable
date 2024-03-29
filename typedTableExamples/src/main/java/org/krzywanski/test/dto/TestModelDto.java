package org.krzywanski.test.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.krzywanski.table.annot.MyTableColumn;
import org.krzywanski.table.annot.ReflectionSort;
import org.krzywanski.table.annot.TableFilter;
import org.krzywanski.table.constraints.Alignment;
import org.krzywanski.test.model.TestEnum;
import org.krzywanski.test.model.TestFormatClass;
import org.krzywanski.test.validation.BetweenValidator;
import org.krzywanski.test.validation.TestModelDtoValidator;

import java.io.Serializable;
import java.util.Date;

@ReflectionSort
@TableFilter(type = String.class, name = "columnA")
@TableFilter(type = Double.class, name = "columnB", label = "decimal.column")
@TableFilter(type = TestEnum.class, name = "testEnum", label = "Test enum")
@TableFilter(type = Boolean.class, name = "testFormatClass", label = "Boolean value")
@Entity

@TestModelDtoValidator
public class TestModelDto implements Comparable<TestModelDto>, Serializable {

    Integer id;

    //        @CustomRenderer(renderer = BooleanIconRenderer.class)
    @MyTableColumn(label = "Boolean value", editable = true)
    private Boolean booleanValue = true;

    @NotEmpty(message = "{field.not.empty}")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "{mobile.number.10.digits}")
    @MyTableColumn(label = "Mobile number", width = 200, sortable = true, editable = true)
    private String columnA;

    @BetweenValidator(min = "10.00", max = "11111.00")
    @NotNull(message = "{field.not.empty}")
    @MyTableColumn(label = "decimal.column", format = "0.00$", sortable = true, editable = true)
    private Double columnB;

    @NotEmpty(message = "{valid.emial.required}")
    @Email(message = "{valid.emial.required}")
    @MyTableColumn(label = "E-mail", editable = true)
    private String columnC;

    @MyTableColumn(label = "User date", format = "dd-MM-yyyy", alignment = Alignment.CENTER)
    private Date date = new Date();

    @MyTableColumn(label = "Priority", editable = true)
    private TestEnum testEnum = TestEnum.MEDIUM;

    @MyTableColumn(label = "customFormatter", editable = true)
    @NotNull(message = "{field.not.empty}")
    TestFormatClass testFormatClass;

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
        if (o == null)
            return 1;
        return this.columnA.compareTo(o.columnA);

    }


}
