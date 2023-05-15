package org.krzywanski.table.procressor;

import org.krzywanski.table.annot.MyTableColumn;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.*;

/**
 * Finds properties of table by reflection and {@link org.krzywanski.table.table.WidthMockWriter}
 * from {@link TableWidthProvider}
 */
public class ColumnCreator {

    private Map<String,Integer> columns = new HashMap<>();

    /**
     * Returns list of table columns with properties from {@link MyTableColumn} annotation
     */
    List<TableColumn> tableColumns = new ArrayList<>();
    public ColumnCreator(Class<?> classType) {

        /**
         * Make sure that provider is included
         */
        if(TableWidthProvider.getInstance() != null){
            if(TableWidthProvider.getInstance().getReader()!= null
                    && TableWidthProvider.getInstance().getReader().getTableList().containsKey(classType.getCanonicalName()))
                columns = TableWidthProvider.getInstance().getReader().getTableList().get(classType.getCanonicalName());
        }

        try {
           PropertyDescriptor[] propertyDescriptors=  Introspector.getBeanInfo(classType).getPropertyDescriptors();

           int iterator = 0;
           for(Field field : classType.getDeclaredFields()){
               MyTableColumn annotation =  field.getAnnotation(MyTableColumn.class);
               String tableLabel;
               if(annotation!=null){
                   if(!annotation.label().isEmpty())
                       tableLabel = annotation.label();
                   else
                       tableLabel = field.getName();
                   int tableWidth = columns.getOrDefault(field.getName(),annotation.width());

                   TableColumn tableColumn = new TableColumn(iterator, tableWidth);
                   tableColumn.setHeaderValue(tableLabel);
                   tableColumns.add(tableColumn);
                   iterator++;
               }


           }

        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

    }
    public List<TableColumn> getTableColumns() {
        return tableColumns;
    }
}
