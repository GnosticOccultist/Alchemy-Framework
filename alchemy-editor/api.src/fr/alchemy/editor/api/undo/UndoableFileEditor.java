package fr.alchemy.editor.api.undo;

import fr.alchemy.editor.api.editor.FileEditor;

/**
 * <code>UndoableFileEditor</code> is an implementation of {@link FileEditor} to support undo/redo of {@link UndoableOperation}.
 * Implementations of this interface can use an internal {@link UndoableOperationControl} to manage the undo and redo methods
 * 
 * @author GnosticOccultist
 */
public interface UndoableFileEditor extends FileEditor {

}
