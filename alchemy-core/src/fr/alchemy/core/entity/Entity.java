package fr.alchemy.core.entity;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.Parent;

/**
 * <code>Entity</code> represents a scene object defined by one, multiple or no {@link Component components}.
 * Its state is updated every loop cycle. 
 * 
 * @author GnosticOccultist
 */
public class Entity extends Parent {
	
	/**
	 * The list of components defining the entity.
	 */
	private List<Component> components = new ArrayList<>();
	/**
	 * Whether the entity is enabled.
	 */
	private BooleanProperty enabled = new SimpleBooleanProperty(true);
	
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
	public Entity attach(final Component component) {
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
	public <T extends Component> Entity detach(final Class<T> type) {
		final Component component = getComponent(type);
		this.components.remove(component);
		component.onDetached(this);
		
		return this;
	}
	
	/**
	 * @return Whether the provided type of component is attached to this entity.
	 */
	public <T extends Component> boolean has(final Class<T> type) {
		return getComponent(type) != null;
	}
	
	/**
	 * @return The component matching the provided type, or null.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(final Class<T> type) {
		for(Component component : components) {
			if(type.isAssignableFrom(component.getClass())) {
				return (T) component;
			}
		}
		return null;
	}
	
	/**
	 * @return The entity position from the parent's origin.
	 */
	public Point2D getPosition() {
		return new Point2D(getTranslateX(), getTranslateY());
	}
	
	/**
	 * Sets the entity position from the parent's origin.
	 * 
	 * @param position The position vector.
	 * @return		   The translated entity.
	 */
	public Entity setPosition(final Point2D position) {
		return setPosition(position.getX(), position.getY());
	}
	
	/**
	 * Sets the entity position from the parent's origin.
	 * 
	 * @param x The X position vector.
	 * @param y The Y position vector.
	 * @return	The translated entity.
	 */
	public Entity setPosition(final double x, final double y) {
		setTranslateX(x);
		setTranslateY(y);
		return this;
	}
	
	/**
	 * Translates the entity by the provided vector.
	 * 
	 * @param x The X translation vector.
	 * @param y The Y translation vector.
	 */
	public void translate(final double x, final double y) {
		setTranslateX(getTranslateX() + x);
		setTranslateY(getTranslateY() + y);
	}
	
	/**
	 * @return Whether the <code>Entity</code> is enabled.
	 */
	public boolean isEnabled() {
		return enabled.get();
	}
	
	/**
	 * Sets whether the <code>Entity</code> should be enabled.
	 * It also enable/disable all of its components.
	 * 
	 * @param enabled Whether the entity is enabled.
	 */
	public void setEnabled(final boolean enabled) {
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
		components.stream().forEach(component -> component.cleanup());
		components.clear();
		getChildren().clear();
	}
}
