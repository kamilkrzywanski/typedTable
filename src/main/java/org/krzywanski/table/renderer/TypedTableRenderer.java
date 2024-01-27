package org.krzywanski.table.renderer;

import org.krzywanski.table.ColumnCreator;
import org.krzywanski.table.TypedTable;
import org.krzywanski.table.annot.CustomRenderer;
import org.krzywanski.table.annot.MyTableColumn;
import org.krzywanski.table.utils.Pair;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

public class TypedTableRenderer extends DefaultTableCellRenderer {
    ColumnCreator columnCreator;

    public TypedTableRenderer(ColumnCreator columnCreator) {
        this.columnCreator = columnCreator;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

       Field pdFieldPair = getColumnField(column, table);

        TableCellRenderer customRenderer = getCustomRenderer(pdFieldPair);
        if(customRenderer != null){
            return customRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

        if (Date.class.equals(pdFieldPair.getType())) {
            if (value != null)
                value = new SimpleDateFormat(Objects.toString(getFormat(pdFieldPair), "MM/dd/yy")).format(value);

        }
        if (Number.class.isAssignableFrom(pdFieldPair.getType())) {
            if (value != null) value = new DecimalFormat(Objects.toString(getFormat(pdFieldPair), "#.#")).format(value);

        }
        //Need to be before format by collection
        setToolTipText(createToolTipText(value));
        if(Collection.class.isAssignableFrom(pdFieldPair.getType())){
            value = formatCollection((Collection<?>) value);
        }

        if(Boolean.class.isAssignableFrom(pdFieldPair.getType())){
            return new DefaultBooleanRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }

        Format format = ((TypedTable<?>) table).getFormatMap().get(pdFieldPair.getType());
        if (format != null)
            value = format.format(value);

        setHorizontalAlignment(getColumnAlignment(pdFieldPair));
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    Field getColumnField(int columnIndex, JTable table) {
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
            //Margin left, otherwise it is moved to the right
            tooltipText.append("<ul style=\"margin-left: 10px;\">");
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
        if(collection == null || collection.isEmpty()) return "";
        return collection.stream().map(Object::toString).reduce((s, s2) -> s + "; " + s2).orElse("");
    }
    private String getFormat(Field pdFieldPair) {

        MyTableColumn annotation = pdFieldPair.getAnnotation(MyTableColumn.class);
        String format = null;
        if (annotation != null) {
            if (!annotation.format().isEmpty()) format = annotation.format();
        }
        return format;
    }

    /**
     * Get column alignment
     * @param pdFieldPair pair of property descriptor and field
     * @return alignment
     */
    private int getColumnAlignment(Field pdFieldPair) {

        MyTableColumn annotation = pdFieldPair.getAnnotation(MyTableColumn.class);
        int alignment = SwingConstants.LEFT;
        if (annotation != null) {
            switch (annotation.alignment()) {
                case LEFT:
                    alignment = SwingConstants.LEFT;
                    break;
                case RIGHT:
                    alignment = SwingConstants.RIGHT;
                    break;
                case CENTER:
                    alignment = SwingConstants.CENTER;
                    break;
            }
        }
        return alignment;
    }

    private TableCellRenderer getCustomRenderer(Field pdFieldPair) {
        CustomRenderer annotation = pdFieldPair.getAnnotation(CustomRenderer.class);
        if (annotation != null) {
            try {
                return annotation.renderer().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
