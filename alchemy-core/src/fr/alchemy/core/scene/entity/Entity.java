package fr.alchemy.core.scene.entity;

import java.util.ArrayList;
import java.util.List;

import fr.alchemy.core.scene.component.Component;
import fr.alchemy.core.scene.component.Transform;
import fr.alchemy.core.scene.component.VisualComponent;
import fr.alchemy.core.util.VoidAction;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * <code>Entity</code> represents a scene object defined by one, multiple or no {@link Component components}.
 * Its state is updated every loop cycle. 
 * 
 * @author GnosticOccultist
 */
public class Entity {
	
	/**
	 * The list of components defining the entity.
	 */
	private List<Component> components = new ArrayList<>();
	/**
	 * Whether the entity is enabled.
	 */
	private BooleanProperty enabled = new SimpleBooleanProperty(true);
	
	/**
	 * Instantiates a new <code>Entity</code> with a <code>Transform</code> 
	 * and a <code>VisualComponent</code>.
	 */
	public Entity() {
		attach(new Transform());
		attach(new VisualComponent());
	}
	
	/**
	 * This method is called automatically every loop cycle by the <code>AlchemyApplication</code>
	 * to update the <code>Entity</code> state.
	 * 
	 * @param now The current time.
	 */
	public final void update(final long now) {
		if(!isEnabled()) {
			return;
		}
		
		components.stream().filter(Component::isEnabled).forEach(component -> component.update(now));
	}
	
	/**
	 * Attaches the given component to the <code>Entity</code>.
	 * 
	 * @param component The component to attach.
	 * @return			The entity with its new component.
	 */
	public final Entity attach(final Component component) {
		this.components.add(component);
		component.onAttached(this);
		
		return this;
	}
	
	/**
	 * Detaches the given component from the <code>Entity</code>.
	 * 
	 * @param component The component to detach.
	 * @return			The entity with its removed component.
	 */
	public final <T extends Component> Entity detach(final Class<T> type) {
		final Component component = getComponent(type);
		
		this.components.remove(component);
		component.onDetached(this);
		
		return this;
	}
	
	/**
	 * @return Whether the provided type of component is attached to this entity.
	 */
	public final <T extends Component> boolean has(final Class<T> type) {
		return getComponent(type) != null;
	}
	
	/**
	 * @return The component matching the provided type, or null.
	 */
	@SuppressWarnings("unchecked")
	public final <T extends Component> T getComponent(final Class<T> type) {
		for(Component component : components) {
			if(type.isAssignableFrom(component.getClass())) {
				return (T) component;
			}
		}
		return null;
	}
	
	/**
	 * Performs an action with the specified type of <code>Component</code> if it exists for the
	 * <code>Entity</code>.
	 * 
	 * @param type	 The type of component to perform the action with.
	 * @param action The action to perform.
	 * @return		 Whether the action has been performed.
	 */
	public final <T extends Component> boolean perform(Class<T> type, VoidAction<T> action) {
		T component = getComponent(type);
		if(component != null) {
			action.perform(component);
			return true;
		}
		return false;
	}
	
	/**
	 * @return Whether the <code>Entity</code> is enabled.
	 */
	public final boolean isEnabled() {
		return enabled.get();
	}
	
	/**
	 * Sets whether the <code>Entity</code> should be enabled.
	 * It also enable/disable all of its components.
	 * 
	 * @param enabled Whether the entity is enabled.
	 */
	public final void setEnabled(final boolean enabled) {
		this.enabled.set(enabled);
		if(enabled)
			components.stream().forEach(Component::enable);
		else
			components.stream().forEach(Component::disable);
	}
	
	/**
	 * Deletes the <code>Entity</code> by deleting all of its components and children.
	 */
	public final void cleanup() {
		setEnabled(false);
		
		components.stream().forEach(component -> component.cleanup());
		components.clear();
	}
}
