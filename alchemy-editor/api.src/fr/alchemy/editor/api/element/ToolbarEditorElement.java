package fr.alchemy.editor.api.element;

import fr.alchemy.editor.api.editor.EditorElement;
import fr.alchemy.editor.api.editor.FileEditor;
import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.utilities.Validator;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 * <code>ToolbarEditorElement</code> is an implementation of {@link EditorElement} which is used to add a 
 * toolbar on the {@link FileEditor} page. It can be composed of buttons, for example a save button, or 
 * other types of {@link Control}.
 * 
 * @param <T> The type of elements filtered by the search bar.
 * 
 * @author GnosticOccultist
 */
public class ToolbarEditorElement<T> implements EditorElement {

	/**
	 * The file editor managing the element.
	 */
	private final FileEditor editor;
	/**
	 * The box representing the toolbar.
	 */
	private HBox toolbar;
	/**
	 * The text search bar used for filtering.
	 */
	private TextSearchBar<T> bar;

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
		HBox.setHgrow(toolbar, Priority.ALWAYS);
		toolbar.setPadding(new Insets(10));
		
		Button action = new Button("Save");
		action.setTooltip(new Tooltip("Save (Ctrl + S)"));
		action.setOnAction(event -> editor.save());
		action.setGraphic(new ImageView(EditorManager.editor().loadIcon("/resources/icons/save.png")));
		action.disableProperty().bind(editor.dirtyProperty().not());
		
		bar = new TextSearchBar<T>();
		bar.setFilterAction(o -> o.toString().toLowerCase().contains(bar.getSearchField().getText().toLowerCase()));
		bar.setPadding(new Insets(0, 0, 0, 10));
		
		toolbar.getChildren().addAll(action, bar);
		container.getChildren().add(toolbar);
	}
	
	/**
	 * Adds the provided {@link Node} as an element of the <code>ToolbarEditorElement</code>.
	 * 
	 * @param element The element to add to the toolbar (not null).
	 * @return		  The toolbar with the new element for chaining purposes.
	 */
	public ToolbarEditorElement<T> add(Node element) {
		Validator.nonNull(element, "The element can't be null!");
		toolbar.getChildren().add(element);
		return this;
	}
	
	/**
	 * Adds the provided {@link Node} as an element of the <code>ToolbarEditorElement</code>.
	 * at the given index.
	 * 
	 * @param element The element to add to the toolbar (not null).
	 * @param index   The index at which the element should be added.
	 * @return		  The toolbar with the new element for chaining purposes.
	 */
	public ToolbarEditorElement<T> add(int index, Node element) {
		Validator.nonNull(element, "The element can't be null!");
		toolbar.getChildren().add(index, element);
		return this;
	}
	
	/**
	 * Return the {@link TextSearchBar} used by the <code>ToolbarEditorElement</code>.
	 * 
	 * @return The search bar of the toolbar.
	 */
	public TextSearchBar<T> getSearchBar() {
		return bar;
	}
}
