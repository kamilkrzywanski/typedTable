package org.krzywanski.panel_v1;

import javax.swing.*;
import javax.swing.undo.UndoManager;

public class FieldToolKit {

    public static void installDefaultEditorKit(JTextField component) {
        component.setComponentPopupMenu(new ToolBoxPopupMenu());
        UndoManager undoManager = new UndoManager();
        component.getDocument().addUndoableEditListener(undoManager);
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

}
