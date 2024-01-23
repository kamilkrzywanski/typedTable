package org.krzywanski.table;

import org.krzywanski.table.annot.MyTableColumn;
import org.krzywanski.table.utils.Pair;

import javax.swing.table.TableColumn;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Finds properties of table by reflection
 * from {@link TableWidthProvider}
 */
public class ColumnCreator {
    /**
     * Fields of class
     */
    private final List<Field> fields;
    /**
     * Map of table columns with property descriptors
     */
    Map<PropertyDescriptor, TableColumn> tableColumns = new LinkedHashMap<>();

    public ColumnCreator(Class<?> classType, long id) {

        fields = Arrays.asList(classType.getDeclaredFields());

        LinkedHashMap<String, Integer> columns = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> tempColumns = null;
        if (TableWidthProvider.getInstance() != null) {
            tempColumns = TableWidthProvider.getInstance().getTable(classType.getCanonicalName(), id);
        }
        //To avoid multiple write TableWidthProvider.getInstance().getTable()... which is trying to ask db/read file from disk
        if (tempColumns != null)
            columns = tempColumns;

        int iterator = 0;

        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(classType);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        //Sort columns based on previous saved order
        List<PropertyDescriptor> propertyDescriptors = Arrays.asList(beanInfo.getPropertyDescriptors());
        if (!columns.isEmpty()) {
            List<String> columnsNamesOrdered = new ArrayList<>(columns.keySet());
            fields.sort(Comparator.comparing(item -> columnsNamesOrdered.indexOf(item.getName())));
        }

        for (Field field : fields) {
            PropertyDescriptor pd = propertyDescriptors.stream().
                    filter(propertyDescriptor -> propertyDescriptor.getName().equals(field.getName())).
                    findFirst().orElse(null);
            if (pd == null) continue;

            MyTableColumn annotation = field.getAnnotation(MyTableColumn.class);
            String tableLabel;
            if (annotation != null) {
                if (!annotation.label().isEmpty()) tableLabel = annotation.label();
                else tableLabel = field.getName();
                int tableWidth = columns.getOrDefault(field.getName(), annotation.width());

                TableColumn tableColumn = new TableColumn(iterator, tableWidth);
                tableColumn.setHeaderValue(tableLabel);

                //If you hide column in last session it will be still hidden
                if (tableWidth == 0) {
                    tableColumn.setMinWidth(0);
                    tableColumn.setMaxWidth(0);
                    tableColumn.setWidth(0);
                }

                tableColumns.put(pd, tableColumn);

                iterator++;
            }


        }

    }

    public Map<PropertyDescriptor, TableColumn> getTableColumns() {
        return tableColumns;
    }

    public Pair<PropertyDescriptor, Field> getFieldByName(Object name) {
        PropertyDescriptor pd = tableColumns.entrySet().stream().filter(fieldTableColumnEntry -> fieldTableColumnEntry.getValue().getHeaderValue().equals(name)).findFirst().get().getKey();
        Field field = fields.stream().filter(field1 -> field1.getName().equals(pd.getName())).findFirst().get();

        return new Pair<>(pd, field);
    }

}
