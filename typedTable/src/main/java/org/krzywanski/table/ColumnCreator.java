package org.krzywanski.table;

import org.krzywanski.BundleTranslator;
import org.krzywanski.TypedFrameworkConfiguration;
import org.krzywanski.table.annot.MyTableColumn;
import org.krzywanski.table.providers.TableWidthProvider;
import org.krzywanski.table.utils.FieldMock;

import javax.swing.*;
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
    final BundleTranslator bundleTranslator = new BundleTranslator(Locale.getDefault(), TypedFrameworkConfiguration.resourceBundles);
    final List<FieldMock> tableColumns = new LinkedList<>();

    public ColumnCreator(Class<?> classType, long id) {

        List<FieldMock> fields = Arrays.stream(classType.getDeclaredFields()).map(field -> new FieldMock(field.getName(), field, null, null)).collect(Collectors.toList());

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
        if (!columns.isEmpty()) {
            List<String> columnsNamesOrdered = new ArrayList<>(columns.keySet());
            fields.sort(Comparator.comparing(item -> columnsNamesOrdered.indexOf(item.getField().getName())));
        }

        if(fields.stream().noneMatch(fieldMock -> fieldMock.getField().isAnnotationPresent(MyTableColumn.class))){
            throw new RuntimeException("No fields with MyTableColumn annotation at class " + classType.getCanonicalName());
        }

        List<PropertyDescriptor> propertyDescriptors = Arrays.asList(beanInfo.getPropertyDescriptors());
        for (FieldMock field : fields) {
            PropertyDescriptor pd = propertyDescriptors.stream().
                    filter(propertyDescriptor -> propertyDescriptor.getName().equals(field.getName())).
                    findFirst().orElse(null);
            if (pd == null) continue;

            MyTableColumn annotation = field.getAnnotation(MyTableColumn.class);
            String tableLabel;
            if (annotation != null) {
                if (!annotation.label().isEmpty()) tableLabel = bundleTranslator.getTranslation(annotation.label());
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

                tableColumns.add(new FieldMock(field.getName(), field.getField(), tableColumn, annotation.editable()));

                iterator++;
            }


        }

    }

    public List<FieldMock> getTableColumns() {
        return tableColumns;
    }

    public FieldMock getFieldByName(Object name) {
        for (FieldMock fieldTableColumnEntry : tableColumns) {
            if (fieldTableColumnEntry.getTableColumn().getHeaderValue().equals(name)) {
                return Optional.of(fieldTableColumnEntry).get();
            }
        }
        return null;
    }

    public Vector<String> getColumnsNames() {
        Vector<String> columnsNames = new Vector<>();
        for (FieldMock fieldMock : tableColumns) {
            columnsNames.add(fieldMock.getTableColumn().getHeaderValue().toString());
        }
        return columnsNames;
    }

    public FieldMock getColumnField(int columnIndex, JTable table) {
        return getFieldByName(
                table.getTableHeader().
                        getColumnModel().
                        getColumn(columnIndex).
                        getHeaderValue()
        );
    }
}
