package fr.alchemy.editor.api.editor;

import javafx.scene.image.Image;

/**
 * <code>EditorComponent</code> is an interface used to implement an attachable 
 * UI component for the AlchemyEditor.
 * <p>
 * Some components may depends on other ones to be correctly loaded.
 */
public interface EditorComponent {
	
	/**
	 * Return the name used by the <code>EditorComponent</code>.
	 * 
	 * @return The name of the component (not null, not empty).
	 */
	String getName();
	
	/**
	 * Return an {@link Image} which can be used as an icon for the <code>EditorComponent</code>.
	 * 
	 * @return The icon of the component or null for none.
	 */
	default Image getIcon() {
		return null;
	}
	
	/**
	 * Performs a finishing action used to post-load configurations
	 * or default state.
	 */
	default void finish() {}
}
