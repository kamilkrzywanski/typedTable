package org.krzywanski.table.table;

import org.krzywanski.table.annot.MyTableColumn;

import javax.swing.table.TableColumn;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Finds properties of table by reflection
 * from {@link TableWidthProvider}
 */
public class ColumnCreator {

    /**
     * Returns list of table columns with properties from {@link MyTableColumn} annotation
     */
    private final Class<?> classType;

    private final Field[] fields;

    Map<PropertyDescriptor, TableColumn> tableColumns = new LinkedHashMap<>();

    public ColumnCreator(Class<?> classType) {

        this.classType = classType;
        fields = classType.getDeclaredFields();

        Map<String, Integer> columns = new HashMap<>();
        if (TableWidthProvider.getInstance() != null) {
            if (TableWidthProvider.getInstance() != null && TableWidthProvider.getInstance().getTable(classType.getCanonicalName()) != null && TableWidthProvider.getInstance().getTable(classType.getCanonicalName()).containsKey(classType.getCanonicalName()))
                columns = TableWidthProvider.getInstance().getTable(classType.getCanonicalName());
        }

        int iterator = 0;

        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(classType);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {

            if (pd.getReadMethod() == null || "class".equals(pd.getName())) continue;


            Field field = findFieldForPd(pd);
            MyTableColumn annotation = field.getAnnotation(MyTableColumn.class);
            String tableLabel;
            if (annotation != null) {
                if (!annotation.label().isEmpty()) tableLabel = annotation.label();
                else tableLabel = field.getName();
                int tableWidth = columns.getOrDefault(field.getName(), annotation.width());

                TableColumn tableColumn = new TableColumn(iterator, tableWidth);
                tableColumn.setHeaderValue(tableLabel);
                tableColumns.put(pd, tableColumn);

                iterator++;
            }


        }

    }

    private Field findFieldForPd(PropertyDescriptor pd){
        return Arrays.stream(classType.getDeclaredFields()).filter(field -> field.getName().equals(pd.getName())).findFirst().get();
    }

    public Map<PropertyDescriptor, TableColumn> getTableColumns() {
        return tableColumns;
    }

    public Pair<PropertyDescriptor, Field> getFieldByName(Object name) {
        PropertyDescriptor pd = tableColumns.entrySet().stream().filter(fieldTableColumnEntry -> fieldTableColumnEntry.getValue().getHeaderValue().equals(name)).findFirst().get().getKey();
        Field field = Arrays.stream(fields).filter(field1 -> field1.getName().equals(pd.getName())).findFirst().get();

        return new Pair<>(pd,field);
    }

}
