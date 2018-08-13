package fr.alchemy.core.scene.component;

import java.io.IOException;
import java.io.OutputStream;

import fr.alchemy.core.asset.binary.BinaryReader;
import fr.alchemy.core.asset.binary.Exportable;
import fr.alchemy.core.scene.entity.Entity;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * <code>Component</code> is an attribute that can be bound to an <code>Entity</code>,
 * to describe specific behaviors.
 * 
 * @author GnosticOccultist
 */
public abstract class Component implements Exportable {
	/**
	 * The entity owning the component.
	 */
	private Entity owner;
	/**
	 * Whether the component is enabled.
	 */
	private final BooleanProperty enabled = new SimpleBooleanProperty(false);

	public Component() {
		enabled.addListener((observable, oldValue, newValue) -> {
			if(newValue == Boolean.TRUE) {
				enable();
			} else {
				disable();
			}
		});
	}
	
	/**
	 * Updates the component. The function is called when the entity updates itself, if
	 * the component is enabled.
	 * 
	 * @param now The current time.
	 */
	public void update(final long now) {}
	
	/**
	 * This method is called whenever the component is attached to an <code>Entity</code>.
	 * It sets the owner to the entity and enables the component.
	 * <p>
	 * You can override this method but make sure to call this implementation, otherwise
	 * it may cause some serious errors.
	 * 
	 * @param entity The entity to which the component was attached.
	 */
	public void onAttached(final Entity entity) {
		setOwner(entity);
		enabled.bind(getOwner().enabledProperty());
	}	
	
	/**
	 * This method is called whenever the component is detached from an <code>Entity</code>.
	 * It sets the owner to null and disables the component.
	 * <p>
	 * You can override this method but make sure to call this implementation, otherwise
	 * it may cause some serious errors.
	 * 
	 * @param entity The entity from which the component was detached.
	 */
	public void onDetached(final Entity entity) {
		enabled.unbind();
		setOwner(null);
	}
	
	/**
	 * Deletes the component. The function is called when the owner entity 
	 * needs to be cleaned up.
	 */
	public void cleanup() {}
	
	/**
	 * @return The entity owning the component.
	 */
	public Entity getOwner() {
		return owner;
	}

	/**
	 * Sets the entity owning the component.
	 * 
	 * @param owner The entity owning the component.
	 */
	public void setOwner(final Entity owner) {
		this.owner = owner;
	}

	/**
	 * @return Whether the component is enabled.
	 */
	public boolean isEnabled() {
		return enabled.get();
	}

	/**
	 * Called when the component is enabled.
	 */
	public void enable() {}
	
	/**
	 * Called when the component is disabled.
	 */
	public void disable() {}
	
	@Override
	public void export(final OutputStream os) throws IOException {
		
	}
	
	@Override
	public void insert(final BinaryReader reader) throws IOException {
	}
}
