package fr.alchemy.editor.api.editor;

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
	 * @return The name of the component.
	 */
	String getName();
	
	/**
	 * Performs a finishing action used to post-load configurations
	 * or default state.
	 */
	default void finish() {}
}
