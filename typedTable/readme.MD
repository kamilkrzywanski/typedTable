Simple tool to map Entity to jtable in java 
@Author Kamil Krzywanski <br>
-You can hide columns <br>
-Add filters, custom filters <br>
-Export to excel, csv <br>
-Define sortables columns <br>
-Simlplify add custom formatters <br>
-Autosave of columns visibility and width <br>
-Add computed columns
-Add column with multiselect

Examples of usage yuo can find in:
https://github.com/kamilkrzywanski/typedTableIntegration

It's olny idea at the moment but by annotations you can create simple table
like there with export to CSV, EXCEL, Sort mechanism: 
@annotation @MyTableColumn make entity "table-friendly"
![img_3.png](images/img_3.png)
With default constructor you can provide data from list of entities
![img.png](images/img_6.png)
Effect of this opperation will be: <br>
![img_4.png](images/img_4.png)
Custom formatter for objects example:
![img.png](images/img.png)

Incoming features:
- crud operations on table
- create of crud panel for table

