package org.krzywanski.panel_v1;

import org.krzywanski.panel_v1.fields.FieldControllerElement;
import org.krzywanski.panel_v1.validation.RevalidateDocumentListener;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

public class FieldToolKit {

    public static void installDefaultEditorKit(JComponent component) {
        component.setComponentPopupMenu(new ToolBoxPopupMenu());
        UndoManager undoManager = new UndoManager();

        if (component instanceof JTextComponent) {
            JTextComponent textComponent = (JTextComponent) component;
            textComponent.getDocument().addUndoableEditListener(undoManager);

            component.registerKeyboardAction(e -> {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }, KeyStroke.getKeyStroke("control Z"), JComponent.WHEN_FOCUSED);

            component.registerKeyboardAction(e -> {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }, KeyStroke.getKeyStroke("control Y"), JComponent.WHEN_FOCUSED);
        }
        if (component instanceof JSpinner) {
            JSpinner spinner = (JSpinner) component;
            if (spinner.getEditor() instanceof JSpinner.DefaultEditor) {
                JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
                JTextComponent textComponent = editor.getTextField();
                textComponent.getDocument().addUndoableEditListener(undoManager);

                textComponent.registerKeyboardAction(e -> {
                    if (undoManager.canUndo()) {
                        undoManager.undo();
                    }
                }, KeyStroke.getKeyStroke("control Z"), JComponent.WHEN_FOCUSED);

                textComponent.registerKeyboardAction(e -> {
                    if (undoManager.canRedo()) {
                        undoManager.redo();
                    }
                }, KeyStroke.getKeyStroke("control Y"), JComponent.WHEN_FOCUSED);
            }
            //add undoManager
        }
    }

    public static FieldControllerElement installDocumentListener(FieldControllerElement element) {
        if (element.getEditorComponent() instanceof JTextComponent) {
            JTextComponent textComponent = (JTextComponent) element.getEditorComponent();
            textComponent.getDocument().addDocumentListener(new RevalidateDocumentListener(element));
        }
        if (element.getEditorComponent() instanceof JSpinner) {
            JSpinner spinner = (JSpinner) element.getEditorComponent();
            if (spinner.getEditor() instanceof JSpinner.DefaultEditor) {
                JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
                JTextComponent textComponent = editor.getTextField();
                textComponent.getDocument().addDocumentListener(new RevalidateDocumentListener(element));
            }
        }

        return element;
    }

}
