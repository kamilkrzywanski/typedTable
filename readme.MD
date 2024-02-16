Simple tool to map Entity to jtable in java 
@Author Kamil Krzywanski <br>
-Autosave of columns visibility and width -> [autoSave](typedTable/features/autosave/autosave.md)
-You can hide columns -> [hideColumn](typedTable/features/hideColumn/hideColumn.md) <br>
-Add filters, custom filters -> [filters](typedTable/features/filters/filters.md) <br>
-Export to excel, csv <br>
-Define sortables columns -> [sorting](typedTable/features/sorting/sorting.md) <br>
-Simlplify add custom formatters -> [formatters](typedTable/features/customFormatters/customFormatters.md) <br>
-Custom renderers for fields with
annotation -> [customRenderers](typedTable/features/customRenderers/customRenderers.md) <br>
-Add computed columns (functional
style) -> [computedColumns](typedTable/features/computedColumns/computedColumns.md) <br>
-Add column with multiselect for selecting rows (objects<T>) - pagination friendly <br>

Examples of usage yuo can find in:
directory: [typedTableExamples](typedTableExamples)

It's olny idea at the moment but by annotations you can create simple table
like there with export to CSV, EXCEL, Sort mechanism: 
@annotation @MyTableColumn make entity "table-friendly"
![img_3.png](typedTable/images/img_3.png)
With default constructor you can provide data from list of entities
![img.png](typedTable/images/img_6.png)
Effect of this opperation will be: <br>
![img_2.png](typedTable/images/img_2.png)
Custom formatter for objects example:
![img.png](typedTable/images/img.png)

Incoming features:
- crud operations on table
- create of crud panel for table


Test environment:
- to run test database you need to have docker installed and run command:
```docker-compose up -d```

- application is located in typedTable directory
- example of usage is located in typedTableExamples directory

