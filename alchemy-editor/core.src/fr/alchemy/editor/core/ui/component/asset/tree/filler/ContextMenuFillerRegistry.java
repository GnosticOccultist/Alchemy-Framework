package fr.alchemy.editor.core.ui.component.asset.tree.filler;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;

import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import javafx.scene.control.MenuItem;

/**
 * <code>ContextMenuFillerRegistry</code> is the registry containing all the {@link ContextMenuFiller}.
 * 
 * @author GnosticOccultist
 */
public final class ContextMenuFillerRegistry {
	
	/**
	 * The single instance of this registry.
	 */
	private static final ContextMenuFillerRegistry INSTANCE = new ContextMenuFillerRegistry();
	
	/**
	 * Return the single instance of the <code>ContextMenuFillerRegistry</code>.
	 * 
	 * @return The context menu filler registry.
	 */
	public static ContextMenuFillerRegistry filler() {
		return INSTANCE;
	}
	
	/**
	 * The array of context menu fillers available in the registry.
	 */
	private final Array<ContextMenuFiller> fillers = ArrayFactory.newArray(ContextMenuFiller.class);
	
	/**
	 * Don't instantiate a new registry.
	 * Use {@link #filler()} to get the single instance.
	 */
	private ContextMenuFillerRegistry() {
		register(AssetContextMenuFiller.class);
	}
	
	/**
	 * Registers a new <code>ContextMenuFiller</code> for the <code>ContextMenuFillerRegistry</code>.
	 * 
	 * @param type The type of filler.
	 * @return     The registry with the new type of filler.
	 */
	public <T extends ContextMenuFiller> ContextMenuFillerRegistry register(Class<T> type) {
		
		ContextMenuFiller filler = null;
		try {
			filler = type.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException 
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.err.println("Unable to register a filler from type: " + type.getClass().getName());
		}
		
		if(filler != null) {
			fillers.add(filler);
		}
		return this;
	}
	
	/**
	 * Fills the specified {@link AssetElement} with a list of available {@link MenuItem}.
	 * 
	 * @param element The element to fill with items.
	 * @param items   The list to add items to.
	 */
	@SuppressWarnings("unchecked")
	public <E> void fill(E element, List<MenuItem> items) {
		
		for(ContextMenuFiller<E> filler : fillers) {
			try {
				filler.fill(element, items);
			} catch (ClassCastException ex) {
				// If the provided element can't be cast to the filler just continue...
				// TODO: We might have to add a type checker for choosing a correct filler in the future.
				continue;
			}
			
		}
	}
}
