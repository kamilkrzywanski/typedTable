package org.krzywanski.table;

import org.krzywanski.table.annot.MyTableColumn;
import org.krzywanski.table.providers.TableWidthProvider;
import org.krzywanski.table.utils.FieldMock;
import org.krzywanski.table.utils.ListSupportLinkedHashMap;

import javax.swing.table.TableColumn;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Finds properties of table by reflection
 * from {@link TableWidthProvider}
 */
public class ColumnCreator {
    /**
     * Map of table columns with property descriptors
     */
    List<FieldMock> fieldMocks = new LinkedList<>();
    final Map<FieldMock, TableColumn> tableColumns = new ListSupportLinkedHashMap<>(fieldMocks);

    public ColumnCreator(Class<?> classType, long id) {

        List<FieldMock> fields = Arrays.stream(classType.getDeclaredFields()).map(field -> new FieldMock(field.getName(), field)).collect(Collectors.toList());

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
            fields.sort(Comparator.comparing(item -> columnsNamesOrdered.indexOf(item.getField().getName())));
        }

        for (FieldMock field : fields) {
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

                tableColumns.put(field, tableColumn);

                iterator++;
            }


        }

    }

    public Map<FieldMock, TableColumn> getTableColumns() {
        return tableColumns;
    }

    public FieldMock getFieldByName(Object name) {
        return tableColumns.entrySet().stream().filter(fieldTableColumnEntry -> fieldTableColumnEntry.getValue().getHeaderValue().equals(name)).findFirst().get().getKey();
    }

    public Vector<String> getColumnsNames() {
        Vector<String> columnsNames = new Vector<>();
        for (TableColumn tableColumn : tableColumns.values()) {
            columnsNames.add(tableColumn.getHeaderValue().toString());
        }
        return columnsNames;
    }

    public List<FieldMock> getFieldMocks() {
        return fieldMocks;
    }
}
