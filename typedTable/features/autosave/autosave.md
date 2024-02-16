[Back to readme](../../../readme.MD)

# Filrers

## Description

By this feature your table will be able to save the visibility and width of the columns.
This feature is enabled global with <b>DefaultTableWidthProvider.java</b>
Default behavior is to save the width of the columns in user directory in the file named "{classOfDTO}{id}.properties"
{id} is the id of the table. If you have more than one table with the same DTO then you can provide id while creating
the table.

```java
    static {
    TableWidthProvider.setProvider(new DefaultTableWidthProvider());
}
```

To change this behavior (i.e. save the width of the columns in the database or in the file system)you need to implement
TableWidthTool and set it with TableWidthProvider.setProvider(TableWidthTool provider);
