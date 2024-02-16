[Back to readme](../../../../readme.MD)

# Sorting

## Description

By this function you can sort the table by the values of the columns.

## Usage

To make column sortable you need to add parameter sortable = true to @MyTableColumn annotation.

```java

@MyTableColumn(label = "Decimal column", sortable = true)
private BigDecimal columnB;
```

When you click on the column header, the table will be sorted by the values of the column you clicked.
To change the direction of sorting, click on the column header again.
To implement sorting you need to honor the contract
of [DataProviderInterface](../../../src/main/java/org/krzywanski/table/providers/DataProviderInterface.java)
List<SortColumn> sortOrder.
This list contains the columns that are sorted and the direction of sorting.

## Example

```java
    public class SortColumn {
    private final String columnName;
    private final SortOrder sortOrder;
}
```

## ReflectionSort

If you want to sort the table without implementing the DataProviderInterface, you can use the @ReflectionSort
annotation.

```java

@MyTableColumn(label = "Decimal column", sortable = true)
@ReflectionSort
class MyDtoClass {
}
```

With this annotation, the table will fetch <b>ALL</b> data from the data provider and sort it in memory.