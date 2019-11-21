package fr.alchemy.editor.core.ui.editor.undo;

import fr.alchemy.editor.api.undo.AbstractUndoableOperation;
import fr.alchemy.editor.api.undo.UndoableFileEditor;
import fr.alchemy.editor.core.ui.editor.text.PropertiesEditor;
import fr.alchemy.editor.core.ui.editor.text.PropertiesEditor.PropertyPair;
import fr.alchemy.utilities.Validator;

/**
 * <code>ModifyCountPropertyOperation</code> is an implementation of {@link AbstractUndoableOperation},
 * which is used to remove or add a {@link PropertyPair} inside the {@link PropertiesEditor}.
 * 
 * @author GnosticOccultist
 */
public final class ModifyCountPropertyOperation extends AbstractUndoableOperation {

	/**
	 * The pair which was removed.
	 */
	PropertyPair pair;
	/**
	 * Whether the pair was removed or added.
	 */
	boolean removed;
	
	/**
	 * Instantiates a new <code>ModifyCountPropertyOperation</code> with the given {@link PropertyPair}.
	 * 
	 * @param oldPair The property pair to be removed (not null).
	 */
	public ModifyCountPropertyOperation(PropertyPair pair) {
		this(pair, true);
	}
	
	/**
	 * Instantiates a new <code>ModifyCountPropertyOperation</code> with the given {@link PropertyPair}.
	 * 
	 * @param oldPair The property pair to be removed or added (not null).
	 * @param removed Whether the property should be removed or added.
	 */
	public ModifyCountPropertyOperation(PropertyPair pair, boolean removed) {
		Validator.nonNull(pair, "The property pair can't be null!");
		this.pair = pair;
		this.removed = removed;
	}
	
	@Override
	public void undo(UndoableFileEditor editor) {
		super.undo(editor);
		
		if(removed) {
			editor.handleAddedProperty(pair);
		} else {
			editor.handleRemovedProperty(pair);
		}
	}
	
	@Override
	public void redo(UndoableFileEditor editor) {
		super.redo(editor);
		
		if(removed) {
			editor.handleRemovedProperty(pair);
		} else {
			editor.handleAddedProperty(pair);
		}
	}
}
