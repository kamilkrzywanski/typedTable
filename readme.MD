Simple tool to map Entity to jtable in java 
@Author Kamil Krzywanski <br>
-You can hide columns <br>
-Add filters, custom filters <br>
-Export to excel, csv <br>
-Define sortables columns <br>
-Simlplify add custom formatters <br>
-Autosave of columns visibility and width <br>

It's olny idea at the moment but by annotations you can create simple table
like there with export to CSV, EXCEL, Sort mechanism: 
@annotation @MyTableColumn make entity "table-friendly"
![img_3.png](img_3.png)
With default constructor you can provide data from list of entities
![img.png](images/img_6.png)
Effect of this opperation will be: <br>
![img_4.png](img_4.png)
Custom formatter for objects example:
![img.png](img.png)

Incoming features:
- crud operations on table
- add select and multiselect panel(needs to make fields editable)
-
