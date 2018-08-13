package fr.alchemy.core.scene.entity;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import fr.alchemy.core.annotation.CoreComponent;
import fr.alchemy.core.asset.binary.BinaryReader;
import fr.alchemy.core.asset.binary.BinaryWriter;
import fr.alchemy.core.asset.binary.Exportable;
import fr.alchemy.core.scene.component.Component;
import fr.alchemy.core.scene.component.NameComponent;
import fr.alchemy.core.scene.component.SimpleObjectComponent;
import fr.alchemy.core.scene.component.Transform;
import fr.alchemy.core.scene.component.VisualComponent;
import fr.alchemy.utilities.functions.VoidAction;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * <code>Entity</code> represents a scene object defined by one, multiple or no {@link Component components}.
 * Its state is updated every loop cycle as well as its components. However either of those can be disabled to
 * prevent updating.
 * <p>
 * Two components are added by default when creating a new <code>Entity</code>: {@link Transform} and {@link VisualComponent}
 * to allow the entity to have position/rotation/scale and graphics node in the scene.
 * 
 * @author GnosticOccultist
 */
public class Entity implements Exportable {
	
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
		if(component == null) {
			return this;
		}
		
		for(Annotation annotation : type.getAnnotations()) {
			if(annotation.annotationType().equals(CoreComponent.class)) {
				System.err.println(type.getSimpleName() + 
						" is marked as a core component, therefore it can't be detached!");
				return this;
			}
		}
	
		components.remove(component);
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
	 * @return Whether the provided type of component is enabled for this entity.
	 * 		   If the component doesn't exist it will return false.
	 */
	public final <T extends Component> boolean isEnabled(final Class<T> type) {
		final T component = getComponent(type);
		if(component != null) {
			return component.isEnabled();
		}
		return false;
	}
	
	/**
	 * @return The component matching the provided type, or null.
	 */
	@SuppressWarnings("unchecked")
	public final <C extends Component> C getComponent(final Class<C> type) {
		for(int i = 0; i < components.size(); i++) {
			if(type.isAssignableFrom(components.get(i).getClass())) {
				return (C) components.get(i);
			}
		}
		return null;
	}
	
	/**
	 * @return The wrapped object of a {@link SimpleObjectComponent} 
	 * 		   matching the provided type, or null.
	 */
	@SuppressWarnings("unchecked")
	public final <T> T getObjectComponent(final Class<T> type) {
		for(int i = 0; i < components.size(); i++) {
			if(SimpleObjectComponent.class.isAssignableFrom(components.get(i).getClass())) {
				if(type.isAssignableFrom(((SimpleObjectComponent<T>)components.get(i)).getObject().getClass())) {
					return ((SimpleObjectComponent<T>)components.get(i)).getObject();
				}
			}
		}
		return null;
	}
	
	/**
	 * Performs an action with the specified type of <code>Component</code> if it exists for the
	 * <code>Entity</code> and is enabled.
	 * 
	 * @see Component#enable()
	 * @param type	 The type of component to perform the action with.
	 * @param action The action to perform.
	 * @return		 Whether the action has been performed.
	 */
	public final <T extends Component> boolean perform(Class<T> type, VoidAction<T> action) {
		final T component = getComponent(type);
		if(component != null && component.isEnabled()) {
			action.perform(component);
			return true;
		}
		return false;
	}
	
	/**
	 * Performs an action with the specified type of <code>Component</code> if it exists for the
	 * <code>Entity</code> and ignore if the component is enabled or disabled.
	 * 
	 * @param type	 The type of component to perform the action with.
	 * @param action The action to perform.
	 * @return		 Whether the action has been performed.
	 */
	public final <T extends Component> boolean forcePerform(Class<T> type, VoidAction<T> action) {
		final T component = getComponent(type);
		if(component != null) {
			action.perform(component);
			return true;
		}
		return false;
	}
	
	/**
	 * @return The enabled property of the <code>Entity</code>.
	 */
	public final BooleanProperty enabledProperty() {
		return enabled;
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
	}
	
	/**
	 * Deletes the <code>Entity</code> by deleting all of its components and children.
	 */
	public final void cleanup() {
		setEnabled(false);
		
		components.stream().forEach(component -> component.cleanup());
		components.clear();
	}
	
	/**
	 * @return A name corresponding to this <code>Entity</code> or 
	 * 		   'Entity' if nothing was found.
	 */
	public String name() {
		if(getComponent(NameComponent.class) != null) {
			return getComponent(NameComponent.class).getName();
		}
		
		return "Entity";
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Entity: ");
		components.forEach(c -> sb.append(c.toString() + "\n"));
		return sb.toString();
	}

	@Override
	public void export(final BinaryWriter writer) throws IOException {
		writer.write("enabled", enabled.get());
		
		for(Component component : components) {
			writer.write(component);
		}
	}
	
	@Override
	public void insert(final BinaryReader reader) throws IOException {
		enabled.set(reader.read("enabled", true));
		getComponent(Transform.class).set(reader.read(Transform.class, getComponent(Transform.class)));
		getComponent(VisualComponent.class).set(reader.read(VisualComponent.class, getComponent(VisualComponent.class)));
		perform(VisualComponent.class, v -> v.refresh());
	}
}
