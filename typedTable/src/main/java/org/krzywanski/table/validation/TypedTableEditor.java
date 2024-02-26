/*
 * Copyright (c) 1997, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package org.krzywanski.table.validation;

import org.krzywanski.panel_v1.fields.TextFieldWithTableSelect;
import org.krzywanski.panel_v1.validation.FieldValidator;
import org.krzywanski.table.TypedTable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.ConstructorProperties;
import java.util.EventObject;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * The default editor for table and tree cells.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans&trade;
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @author Alan Chung
 * @author Philip Milne
 * @since 1.2
 */
public class TypedTableEditor<T, E> extends AbstractCellEditor
        implements TableCellEditor {

    final ResourceBundle resourceBundle = ResourceBundle.getBundle("Messages", Locale.getDefault());
    final FieldValidator<E> fieldValidator = new FieldValidator<>();
    final TypedTable<E> table;
    final ValidatorDialog<E> validatorDialog;
    /**
     * The Swing component being edited.
     */
    protected TextFieldWithTableSelect<T> editorComponent;

    /**
     * An integer specifying the number of clicks needed to start editing.
     * Even if <code>clickCountToStart</code> is defined as zero, it
     * will not initiate until a click occurs.
     */
    protected int clickCountToStart = 1;

    /**
     * Constructs a <code>DefaultCellEditor</code> that uses a text field.
     *
     * @param editorComponent a <code>TextFieldWithTableSelect<T></code> object
     */
    @ConstructorProperties({"component"})
    public TypedTableEditor(final TextFieldWithTableSelect<T> editorComponent, TypedTable<E> table, ValidatorDialog<E> validatorDialog) {
        this.editorComponent = editorComponent;
        this.table = table;
        this.validatorDialog = validatorDialog;
        editorComponent.addActionListener(this::actionPerformed);
    }


    /**
     * Returns a reference to the editor component.
     *
     * @return the editor <code>Component</code>
     */
    public Component getComponent() {
        return editorComponent;
    }

    public Object getCellEditorValue() {
        return editorComponent.getCurrentValue();
    }


    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        getCellEditorValue();
        if (table.getSelectedColumn() >= 0) {
            try {
                Set<String> result = fieldValidator.validateField(table.getTypeClass(), table.getColumnCreator().getColumnField(table.getSelectedColumn(), table).getField().getName(), getCellEditorValue());
                if (!result.isEmpty()) {
                    validatorDialog.showIfErrorsPresent(result);
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    /**
     * Implements the <code>TableCellEditor</code> interface.
     */
    public synchronized Component getTableCellEditorComponent(JTable table, Object value,
                                                              boolean isSelected,
                                                              int row, int column) {
        editorComponent.setTextField((T) value);
        validatorDialog.computeLocationForCell(table, row, column);
        return editorComponent;
    }


    /**
     * Returns true if <code>anEvent</code> is <b>not</b> a
     * <code>MouseEvent</code>.  Otherwise, it returns true
     * if the necessary number of clicks have occurred, and
     * returns false otherwise.
     *
     * @param anEvent the event
     * @return true  if cell is ready for editing, false otherwise
     * @see #shouldSelectCell
     */
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    /**
     * When an action is performed, editing is ended.
     *
     * @param e the action event
     * @see #stopCellEditing
     */
    public void actionPerformed(ActionEvent e) {
        stopCellEditing();
    }
} // End of class JCellEditor
