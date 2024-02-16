[Back to readme](../../../../readme.MD)

# Custom formatters for columns

## Description

By this function you can format the values of the columns.

## Usage

To add custom formatter yun need to use method: TypedTablePanel::addCustomFormatter(Class<?> typeClass, Format format)

```java
        panel.addCustomFormatter(TestFormatClass .class, new Format() {
    @Override
    public StringBuffer format (Object obj, StringBuffer toAppendTo, FieldPosition pos){
        return new StringBuffer("FORMAT");
    }

    @Override
    public Object parseObject (String source, ParsePosition pos){
        //Not used
        return null;
    }
});
```

With this code you will have custom formatter for column with type TestFormatClass.