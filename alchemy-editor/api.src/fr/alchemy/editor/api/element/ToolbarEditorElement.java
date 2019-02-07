package fr.alchemy.editor.api.element;

import fr.alchemy.editor.api.editor.EditorElement;
import fr.alchemy.editor.api.editor.FileEditor;
import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.utilities.Validator;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * <code>ToolbarEditorElement</code> is an implementation of {@link EditorElement} which is used to add a 
 * toolbar on the {@link FileEditor} page. It can be composed of buttons, for example a save button, or 
 * other types of {@link Control}.
 * 
 * @author GnosticOccultist
 */
public class ToolbarEditorElement implements EditorElement {

	/**
	 * The file editor managing the element.
	 */
	private final FileEditor editor;
	/**
	 * The box representing the toolbar.
	 */
	private HBox toolbar;

	/**
	 * Instantiates a new <code>ToolbarEditorElement</code> managed by the specified
	 * {@link FileEditor}.
	 * 
	 * @param editor The file editor managing the toolbar element.
	 */
	public ToolbarEditorElement(FileEditor editor) {
		Validator.nonNull(editor, "The file editor can't be null!");
		this.editor = editor;
	}
	
	@Override
	public void constructUI(Pane container) {
		toolbar = new HBox();
		toolbar.prefWidthProperty().bind(container.widthProperty());
		editor.getRoot().prefHeightProperty().bind(container.heightProperty().subtract(toolbar.heightProperty()));
		
		Button action = new Button("Save");
		action.setTooltip(new Tooltip("Save (Ctrl + S)"));
		action.setOnAction(event -> editor.save());
		action.setGraphic(new ImageView(EditorManager.editor().loadIcon("/resources/icons/save.png")));
		action.disableProperty().bind(editor.dirtyProperty().not());
		
		toolbar.getChildren().add(action);
		container.getChildren().add(toolbar);
	}
}
