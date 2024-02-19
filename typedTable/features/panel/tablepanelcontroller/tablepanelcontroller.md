# Connect table with panel

## Description

Table panel controller is a feature that can be used to connect table with panel.
With this feature your table will be automatically updated when you update fields in panel.

### Basic usage

To connect table with panel you need to invoke method:

```java
static {
    new PanelTableController<>(table.table, autoPanel);
}
```

When you invoke this method your table will be connected with panel. When you update fields in panel your table will be
automatically updated.