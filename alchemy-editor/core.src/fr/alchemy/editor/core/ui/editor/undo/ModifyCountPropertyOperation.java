package fr.alchemy.editor.core.ui.editor.undo;

import java.util.Collection;

import fr.alchemy.editor.api.undo.AbstractUndoableOperation;
import fr.alchemy.editor.api.undo.OperationConsumer;
import fr.alchemy.editor.core.ui.editor.text.PropertiesEditor;
import fr.alchemy.editor.core.ui.editor.text.PropertiesEditor.PropertyPair;
import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.array.Array;

/**
 * <code>ModifyCountPropertyOperation</code> is an implementation of {@link AbstractUndoableOperation}, which is 
 * used to remove or add one or multiple {@link PropertyPair} at the same time inside the {@link PropertiesEditor}.
 * 
 * @author GnosticOccultist
 */
public final class ModifyCountPropertyOperation extends AbstractUndoableOperation {

	/**
	 * The pairs which was removed or added.
	 */
	Array<PropertyPair> pairs;
	/**
	 * Whether the pair was removed or added.
	 */
	boolean removed;
	
	/**
	 * Instantiates a new <code>ModifyCountPropertyOperation</code> with the given {@link PropertyPair}.
	 * 
	 * @param pair The property pair to be removed (not null).
	 */
	public ModifyCountPropertyOperation(PropertyPair pair) {
		this(pair, true);
	}
	
	/**
	 * Instantiates a new <code>ModifyCountPropertyOperation</code> with the given collection of {@link PropertyPair}.
	 * 
	 * @param pairs	The collection of pairs to be removed or added (not null, not empty).
	 */
	public ModifyCountPropertyOperation(Collection<PropertyPair> pairs) {
		Validator.nonEmpty(pairs, "The property pair can't be null!");
		this.pairs = Array.of(pairs);
		this.removed = true;
	}
	
	/**
	 * Instantiates a new <code>ModifyCountPropertyOperation</code> with the given collection of {@link PropertyPair}.
	 * 
	 * @param pairs	  The collection of pairs to be removed or added (not null, not empty).
	 * @param removed Whether the property should be removed or added.
	 */
	public ModifyCountPropertyOperation(Collection<PropertyPair> pairs, boolean removed) {
		Validator.nonEmpty(pairs, "The property pair can't be null!");
		this.pairs = Array.of(pairs);
		this.removed = removed;
	}
	
	/**
	 * Instantiates a new <code>ModifyCountPropertyOperation</code> with the given {@link PropertyPair}.
	 * 
	 * @param pair 	  The property pair to be removed or added (not null).
	 * @param removed Whether the property should be removed or added.
	 */
	public ModifyCountPropertyOperation(PropertyPair pair, boolean removed) {
		Validator.nonNull(pair, "The property pair can't be null!");
		this.pairs = Array.of(pair);
		this.removed = removed;
	}
	
	@Override
	protected void doUndo(OperationConsumer editor) {
		if(removed) {
			editor.handleAddedProperty(pairs);
		} else {
			editor.handleRemovedProperty(pairs);
		}
	}
	
	@Override
	protected void doRedo(OperationConsumer editor) {
		if(removed) {
			editor.handleRemovedProperty(pairs);
		} else {
			editor.handleAddedProperty(pairs);
		}
	}
	
	@Override
	public String toString() {
		String prefix = removed ? "Removed " : "Added ";
		if(pairs.size() == 1) {
			PropertyPair pair = pairs.get(0);
			prefix += "'" + pair.getKey() + "' -> " + pair.getValue() + ".";
		} else {
			prefix += pairs.size() + " properties.";
		}
		return prefix;
	}
}
