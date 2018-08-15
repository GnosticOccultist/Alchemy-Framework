package fr.alchemy.core.scene.component;

import fr.alchemy.core.scene.entity.Entity;

/**
 * <code>SimpleObjectComponent</code> represents a {@link Component} defined by a single object.
 * It is basically a wrapper around the object, allowing it to be attached to an {@link Entity}.
 * 
 * @author GnosticOccultist
 */
public class SimpleObjectComponent<T> extends Component {
	/**
	 * The object defining the component.
	 */
	private T object;
	
	public SimpleObjectComponent() {}
	
	public SimpleObjectComponent(final T object) {
		this.object = object;
	}
	
	/**
	 * @return The object contained in the component.
	 */
	public T getObject() {
		return object;
	}
	
	@Override
	public String toString() {
		return "Object: " + object;
	}
}
