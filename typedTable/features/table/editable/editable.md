# Make table editable

## Description

By this function you can make table editable. You can edit the values of the cells and save the changes as a object of
your entity.

## Usage

To make table editable you need to add parameter editable = true to @MyTableColumn annotation and implement the contract
of
[DataFlowAdapter.java](..%2F..%2F..%2Fsrc%2Fmain%2Fjava%2Forg%2Fkrzywanski%2Fpanel_v1%2Fdataflow%2FDataFlowAdapter.java)
with method TypedTable::installDataUpdateAdapter(DataUpdateAdapter<T> dataUpdateAdapter).

```java
static {
    TypedTablePanel<TestModelDto> table = TypedTablePanel.getTableWithData(Main.getAllData(), TestModelDto.class, 3);
    table.installDataUpdateAdapter(new TestModelService());
}
```

Add panel editor for field:
To add table with select for class you need to use method TypedTable::setTableEditorForClass(Class<E> clazz,
TextFieldWithTableSelect<E> tableSelect)

```java
static {
    TextFieldWithTableSelect<TestFormatClass> selectPanel2 = TextFieldWithTableSelect.getTextWithTableSelect(List.of(new TestFormatClass("A"), new TestFormatClass("B")), "TestFormatClass.class");
    table.setTableEditorForClass(TestFormatClass.class, selectPanel2);
}
```

## Validation

For validation by default is jakarta.validaton with hibernate validator.
So each field with validation annotation will be validated.