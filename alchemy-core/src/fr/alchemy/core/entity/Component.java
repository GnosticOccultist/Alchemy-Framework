package fr.alchemy.core.entity;

/**
 * <code>Component</code> is an attribute that can be bound to an <code>Entity</code>,
 * to describe specific behaviors.
 * 
 * @author GnosticOccultist
 */
public abstract class Component {
	/**
	 * The entity owning the component.
	 */
	private Entity owner;
	/**
	 * Whether the component is enabled.
	 */
	private boolean enabled;
	
	/**
	 * Updates the component. The function is called when the entity updates itself, if
	 * the component is enabled.
	 * 
	 * @param now The current time.
	 */
	public abstract void update(final long now);
	
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
		setEnabled(true);
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
		setOwner(null);
		setEnabled(false);
	}
	
	/**
	 * Deletes the component. The function is called when the owner entity 
	 * needs to be cleaned up.
	 */
	public abstract void cleanup();
	
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
		return enabled;
	}

	/**
	 * Sets whether the component should be enabled.
	 * 
	 * @param enabled Whether the component is enabled.
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}
}
