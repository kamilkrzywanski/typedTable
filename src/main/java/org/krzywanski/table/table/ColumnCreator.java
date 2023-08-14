package org.krzywanski.table.table;

import org.krzywanski.table.annot.MyTableColumn;

import javax.swing.table.TableColumn;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Finds properties of table by reflection and {@link WidthWriter}
 * from {@link TableWidthProvider}
 */
public class ColumnCreator {
    private Map<String,Integer> columns = new HashMap<>();

    /**
     * Returns list of table columns with properties from {@link MyTableColumn} annotation
     */
    Map<Field,TableColumn> tableColumns = new LinkedHashMap<>();
    public ColumnCreator(Class<?> classType) {

        /**
         * Make sure that provider is included
         */
        if(TableWidthProvider.getInstance() != null){
            if(TableWidthProvider.getInstance()!= null && TableWidthProvider.getInstance().getTable(classType.getCanonicalName()) != null
                    && TableWidthProvider.getInstance().getTable(classType.getCanonicalName()).containsKey(classType.getCanonicalName()))
                columns = TableWidthProvider.getInstance().getTable(classType.getCanonicalName());
        }

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
                tableColumns.put(field,tableColumn);

                iterator++;
            }


        }

    }
    public Map<Field,TableColumn> getTableColumns() {
        return tableColumns;
    }

}
