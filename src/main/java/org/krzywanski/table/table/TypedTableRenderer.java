package org.krzywanski.table.table;

import org.krzywanski.table.annot.MyTableColumn;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public class TypedTableRenderer extends DefaultTableCellRenderer {
    ColumnCreator columnCreator;

    TypedTableRenderer(ColumnCreator columnCreator) {
        this.columnCreator = columnCreator;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        Pair<PropertyDescriptor, Field> pdFieldPair = getColumnField(column, table);
        if (Date.class.equals(pdFieldPair.getSecond().getType())) {
            if (value != null)
                value = new SimpleDateFormat(Objects.toString(getFormat(pdFieldPair), "MM/dd/yy")).format(value);

        }
        if (Number.class.isAssignableFrom(pdFieldPair.getSecond().getType())) {
            if (value != null) value = new DecimalFormat(Objects.toString(getFormat(pdFieldPair), "#.#")).format(value);

        }
        //Need to be before format by collection
        setToolTipText(createToolTipText(value));
        if(Collection.class.isAssignableFrom(pdFieldPair.getSecond().getType())){
            value = formatCollection((Collection<?>) value);
        }
        Format format = ((TypedTable<?>) table).getFormatMap().get(pdFieldPair.getSecond().getType());
        if (format != null)
            value = format.format(value);

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    Pair<PropertyDescriptor, Field> getColumnField(int columnIndex, JTable table) {
        return columnCreator.getFieldByName(
                table.getTableHeader().
                        getColumnModel().
                        getColumn(columnIndex).
                        getHeaderValue()
        );
    }

    private String createToolTipText(Object tooltip) {
        StringBuilder tooltipText = new StringBuilder("<html>");
        if(tooltip == null) return "";


        if(tooltip instanceof Collection){
            tooltipText.append("<ul>");
            for(Object o : (Collection<?>) tooltip){
                tooltipText.append("<li>").append(o).append("</li>");
            }
            tooltipText.append("</ul>");
        }else{
            tooltipText.append(tooltip);
        }
        return tooltipText.toString();
    }
    private String formatCollection(Collection<?> collection){
        return collection.stream().map(Object::toString).reduce((s, s2) -> s + "; " + s2).orElse("");
    }
    private String getFormat(Pair<PropertyDescriptor, Field> pdFieldPair) {

        MyTableColumn annotation = pdFieldPair.getSecond().getAnnotation(MyTableColumn.class);
        String format = null;
        if (annotation != null) {
            if (!annotation.format().isEmpty()) format = annotation.format();
        }
        return format;
    }

}
