package fr.alchemy.editor.api.editor;

import javafx.scene.layout.Pane;

/**
 * <code>EditorElement</code> is an interface to implement a UI element to compose a {@link FileEditor}.
 * Each registered elements will be added to the editor view in order to create a flexible and user-friendly interface.
 * 
 * @author GnosticOccultist
 */
public interface EditorElement {
	
	/**
	 * Construct the UI element by adding its element to the provided {@link Pane}.
	 * This method is called by the {@link FileEditor} managing this <code>EditorElement</code>.
	 * 
	 * @param pane The editor pane to which attach the UI element.
	 */ 
	void constructUI(Pane pane);
}
