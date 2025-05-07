Simple tool to map Entity to jtable in java
@Author Kamil Krzywanski <br>

Examples of usage yuo can find in:
directory: [typedTableExamples](typedTableExamples)
### Explore the Powerful Features of `typedTable`

Take your table management to the next level with these advanced features:

- **Autosave Column Settings**: Automatically save column visibility and width preferences. [Learn more](typedTable/features/table/autosave/autosave.md)  
- **Hide Columns**: Easily hide unnecessary columns for a cleaner view. [Learn more](typedTable/features/table/hideColumn/hideColumn.md)  
- **Advanced Filtering**: Add filters or create custom ones to refine your data. [Learn more](typedTable/features/table/filters/filters.md)  
- **Export Options**: Export your data effortlessly to Excel or CSV.  
- **Sortable Columns**: Define sortable columns for better data organization. [Learn more](typedTable/features/table/sorting/sorting.md)  
- **Custom Formatters**: Simplify the process of adding custom formatters. [Learn more](typedTable/features/table/customFormatters/customFormatters.md)  
- **Custom Renderers**: Use annotations to create custom field renderers. [Learn more](typedTable/features/table/customRenderers/customRenderers.md)  
- **Computed Columns**: Add functional-style computed columns for dynamic data. [Learn more](typedTable/features/table/computedColumns/computedColumns.md)  
- **Multi-Select Columns**: Add a column with multi-select functionality, perfect for pagination-friendly row selection. [Learn more](typedTable/features/table/multiSelect/multiSelect.md)  
- **Column Translations**: Add translations for column headers to support multiple languages. [Learn more](typedTable/features/table/translation/translation.md)  
- **Editable Tables**: Make your table fully editable for seamless data updates. [Learn more](typedTable/features/table/editable/editable.md)  

Discover how `typedTable` can simplify and enhance your table management experience!

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


Test environment:
- to run test database you need to have docker installed and run command:
```docker-compose up -d```

- application is located in typedTable directory
- example of usage is located in typedTableExamples directory

## TypedPanel

Simple tool for map your entity to panel with CRUD operations. --> See
more [TypedPanel](typedTable/features/panel/readme.MD)

![img.png](img.png)
