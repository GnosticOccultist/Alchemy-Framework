package fr.alchemy.editor.core.ui.component;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import fr.alchemy.editor.core.EditorManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
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
	 * The store for the list without filter.
	 */
	private ObservableList<T> store = FXCollections.observableArrayList();
	/**
	 * The list to filter.
	 */
	private ObservableList<T> list;
	/**
	 * The filtering action.
	 */
	private BiPredicate<String, T> filter;	
	
	/**
	 * Instantiates a new <code>TextSearchBar</code> with the provided
	 * filtering action.
	 * 
	 * @param filter The filtering action.
	 */
	public TextSearchBar(BiPredicate<String, T> filter) {
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
		cancelIcon.setImage(EditorManager.editor().loadIcon("resources/icons/cancel.png"));
		cancelIcon.setOnMouseReleased(this::processEvent);
		cancelIcon.setPickOnBounds(true);
		
		searchField.setOnKeyReleased(this::processEvent);
		searchField.textProperty().addListener((observable, oldValue, newValue) -> search());
		searchField.setText(searchBarText());
		searchField.setFont(searchBarFont());
		
		content.getChildren().addAll(cancelIcon, searchField);
		getChildren().add(content);
	}
	
	/**
	 * Process the provided {@link KeyEvent#KEY_RELEASED} by searching and filtering
	 * the provided list.
	 * <p>
	 * If the search field is empty it will {@link #reset()} the field to its initial value.
	 * 
	 * @param event The key event to process.
	 */
	protected void processEvent(KeyEvent event) {
		event.consume();
		
		if(event.getCode().equals(KeyCode.ENTER)) {
			// Prevent useless searching and automatically reset the search field.
			if(searchField.getText().isEmpty()) {
				reset();
				return;
			}
			
			search();
		}
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
	
	/**
	 * Sets the list to search the matching fields in.
	 * 
	 * @param list The list to filter.
	 */
	public void searchFor(ObservableList<T> list) {
		this.store.setAll(list);
		this.list = list;
	}
	
	/**
	 * Search the matching fields from the list using the provided filtering action and
	 * updates the children list according to the result. 
	 * You can set the list to search in, by calling {@link #searchFor(ObservableList)}.
	 * <p>
	 * It also set the background to red, if no fields are matching the filter.
	 */
	protected void search() {
		if(searchField.getText().equals(SEARCH_BAR_INITIAL_TEXT) || list == null) {
			return;
		}
		
		List<T> filtered = store.stream().filter(t -> filter.test(searchField.getText(), t)).collect(Collectors.toList());
			
		if(filtered.isEmpty()) {
			searchField.setBackground(new Background(new BackgroundFill(Color.INDIANRED, null, null)));
			list.setAll(filtered);
		} else {
			searchField.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			list.setAll(filtered);
		}	
	}
	
	/**
	 * Reset the <code>TextSearchBar</code> to its initial value by clearing
	 * the background color and setting the text and font of the initial text.
	 */
	protected void reset() {
		searchField.setText(searchBarText());
		searchField.setFont(searchBarFont());
		searchField.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		list.setAll(store);
	}
	
	/**
	 * Sets the filtering action of the <code>TextSearchBar</code>.
	 * 
	 * @param filter The filtering action.
	 * @return		 The updated search bar.
	 */
	public final TextSearchBar<T> setFilterAction(BiPredicate<String, T> filter) {
		this.filter = filter;
		return this;
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
