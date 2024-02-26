[Back to readme](../../../../readme.MD)

# Translations

You can provide own translations for fields in table. To do this you need to provide your own
{translationFilename}.properties file in your resources directory.
To register your translation file you need to add it to TypedFrameworkConfiguration. Example of usage:

```java
static {
    TypedFrameworkConfiguration.addResourceBundle({translationFilename});
    //EXAMPLE OF VALIDATION FILE TestModelDto.properties
    TypedFrameworkConfiguration.addResourceBundle("TestModelDto");
}
```

Then you can use properties as a translation for your fields. Example of usage:

```java
static {
    @MyTableColumn(label = "decimal.column", format = "0.00$", sortable = true)
    @Column(precision = 10, scale = 2)
    private BigDecimal columnB;
}
```