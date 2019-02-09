package fr.alchemy.editor.api.element;

import java.util.function.Predicate;

import javax.swing.GroupLayout.Alignment;

import com.sun.imageio.stream.CloseableDisposerRecord;

import fr.alchemy.editor.core.EditorManager;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 * <code>TextSearchBar</code> represents a search bar which can be used to filter
 * a list of objects using a string value specified by the user.
 * 
 * @param <T> The type of object to filter.
 * 
 * @author GnosticOccultist
 */
public class TextSearchBar<T> extends HBox {
	
	/**
	 * The initial text used by the search bar field.
	 */
	protected static final String SEARCH_BAR_INITIAL_TEXT = "Search...";
	/**
	 * The font used by the search bar field.
	 */
	protected static final Font SEARCH_BAR_FONT = Font.font("Verdana", FontPosture.ITALIC, 12);
	 
	/**
	 * The content box.
	 */
	private final HBox content;
	/**
	 * The search bar field.
	 */
	private final TextField searchField;
	/**
	 * The search cancel icon.
	 */
	private final ImageView cancelIcon;
	/**
	 * The filtering action.
	 */
	private Predicate<T> filter;	
	/**
	 * The list providing the filtering action.
	 */
	private FilteredList<T> filteredList;
	
	/**
	 * Instantiates a new <code>TextSearchBar</code> with no filtering action.
	 */
	public TextSearchBar() {
		this(null);
	}
	
	/**
	 * Instantiates a new <code>TextSearchBar</code> with the provided
	 * filtering action.
	 * 
	 * @param filter The filtering action.
	 */
	public TextSearchBar(Predicate<T> filter) {
		this.searchField = new TextField();
		this.content = new HBox();
		this.cancelIcon = new ImageView();
		this.filter = filter;
		
		construct();
	}
	
	/**
	 * Construct the components of the <code>TextSearchBar</code>.
	 */
	protected void construct() {
		
		searchField.focusedProperty().addListener((observable, oldValue, newValue) -> handleFocus(newValue));
		searchField.setOnKeyReleased(this::processEvent);
		searchField.textProperty().addListener((observable, oldValue, newValue) -> search());
		searchField.setPromptText(searchBarText());
		
		StackPane clearPane = new StackPane();
		clearPane.setCursor(Cursor.DEFAULT);
		clearPane.setAlignment(Pos.CENTER_LEFT);
		cancelIcon.setImage(EditorManager.editor().loadIcon("resources/icons/cancel.png"));
		cancelIcon.setOnMouseReleased(this::processEvent);
		cancelIcon.setPickOnBounds(true);
		clearPane.getChildren().add(cancelIcon);
		
		content.getChildren().addAll(clearPane, searchField);
		getChildren().add(content);
	}

	/**
	 * Process the provided {@link KeyEvent#KEY_RELEASED} by searching and filtering
	 * the provided list.
	 * <p>
	 * If the search field is empty and the {@link KeyCode#ENTER} is pressed, it will {@link #reset()} 
	 * the field to its initial value.
	 * 
	 * @param event The key event to process.
	 */
	protected void processEvent(KeyEvent event) {
		event.consume();
		
		search();
	}
	
	/**
	 * Process the provided {@link MouseEvent#MOUSE_RELEASED} on the cancel icon,
	 *  by calling {@link #reset()}.
	 * 
	 * @param event The mouse event to process.
	 */
	protected void processEvent(MouseEvent event) {
		event.consume();
		
		if(event.getButton().equals(MouseButton.PRIMARY)) {
			reset();
		}
	}
	
	protected void handleFocus(Boolean focused) {
		if(!focused && filteredList.isEmpty()) {
			reset();
		}
	}
	
	/**
	 * Sets the {@link ObservableList} to search the matching fields in. 
	 * <p>
	 * The function will wrap a {@link FilteredList} around the provided list and will contains all its
	 * elements until a predicate is set using {@link #setFilterAction(Predicate)} method.
	 * 
	 * @param list The observable list to use the filtering for.
	 */
	public void bindTo(ObservableList<T> list) {
		filteredList = new FilteredList<T>(list);
	}
	
	/**
	 * Sets the items of the provided {@link TableView} to the one of the {@link FilteredList} of the
	 * <code>TextSearchBar</code> meaning it will only show up the elements which pass the predicate test.
	 * 
	 * @param table The table view to set the items for.
	 */
	public void filter(TableView<T> table) {
		table.setItems(filteredList);
	}
	
	/**
	 * Search the matching fields by setting the predicate property of the {@link FilteredList} to the one
	 * registered with the {@link #setFilterAction(Predicate)} method. The method will be performed only if the search field
	 * isn't empty.
	 * <p>
	 * It also set the background to red, if no fields are matching the filter or white if at least one of the field matches.
	 */
	protected void search() {
		if(filteredList == null || searchField.getText() == null) {
			return;
		}
		if(searchField.getText().isEmpty()) {
			reset();
			return;
		}
		
		filteredList.setPredicate(filter);
		filteredList.forEach(System.out::println);
		
		if(filteredList.isEmpty()) {
			searchField.setBackground(new Background(new BackgroundFill(Color.INDIANRED, null, null)));
		} else {
			searchField.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		}
	}
	
	/**
	 * Reset the <code>TextSearchBar</code> to its initial value by clearing
	 * the background color and setting the text and font of the initial text.
	 */
	protected void reset() {
		searchField.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		searchField.setText(null);
		filteredList.setPredicate(null);
	}
	
	/**
	 * Sets the filtering action of the <code>TextSearchBar</code>.
	 * 
	 * @param filter The filtering action.
	 * @return		 The updated search bar.
	 */
	public final TextSearchBar<T> setFilterAction(Predicate<T> filter) {
		this.filter = filter;
		return this;
	}
	
	/**
	 * Return the search {@link TextField} of the <code>TextSearchBar</code>.
	 * 
	 * @return The search field of the search bar.
	 */
	public TextField getSearchField() {
		return searchField;
	}
	
	/**
	 * Return the font to use for the <code>TextSearchBar</code>.
	 * 
	 * @return The search bar font.
	 */
	protected Font searchBarFont() {
		return SEARCH_BAR_FONT;
	}
	
	/**
	 * Return the initial text to use for the <code>TextSearchBar</code>.
	 * 
	 * @return The search bar initial text.
	 */
	protected String searchBarText() {
		return SEARCH_BAR_INITIAL_TEXT;
	}
}
