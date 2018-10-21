package fr.alchemy.editor.core.ui.component.asset.tree.filler;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import fr.alchemy.utilities.collections.Array;
import javafx.scene.control.MenuItem;

/**
 * <code>ContextMenuFillerRegistry</code> is the registry containing all the {@link ContextMenuFiller}
 * 
 * @author GnosticOccultist
 */
public class ContextMenuFillerRegistry {
	
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
	private final Array<ContextMenuFiller> fillers = Array.newEmptyArray(ContextMenuFiller.class);
	
	/**
	 * Don't instantiate a new registry.
	 * Use {@link #filler()} to get the single instance.
	 */
	private ContextMenuFillerRegistry() {
		register(AlchemyContextMenuFiller.class);
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
	public void fill(AssetElement element, List<MenuItem> items) {
		
		for(ContextMenuFiller filler : fillers) {
			filler.fill(element, items);
		}
	}
}
