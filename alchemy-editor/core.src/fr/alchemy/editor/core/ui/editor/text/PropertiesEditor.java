package fr.alchemy.editor.core.ui.editor.text;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.AbstractFileEditor;
import fr.alchemy.editor.api.editor.BaseFileEditor;
import fr.alchemy.editor.api.element.ToolbarEditorElement;
import fr.alchemy.editor.api.undo.AbstractUndoableOperation;
import fr.alchemy.editor.api.undo.UndoableFileEditor;
import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.editor.core.ui.component.dialog.AddPropertyDialog;
import fr.alchemy.editor.core.ui.editor.text.PropertiesEditor.PropertyPair;
import fr.alchemy.editor.core.ui.editor.undo.ModifyCountPropertyOperation;
import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.file.FileUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * <code>PropertiesEditor</code> is an implementation of {@link AbstractFileEditor} used for editing 
 * '.properties' file.
 * 
 * @author GnosticOccultist
 */
public class PropertiesEditor extends BaseFileEditor<TableView<PropertyPair>> {
	
	/**
	 * The currently edited properties.
	 */
	private Properties properties;
	/**
	 * The column containing the key of each property.
	 */
	private TableColumn<PropertyPair, String> keyColumn;
	/**
	 * The column containing the value of each property.
	 */
	private TableColumn<PropertyPair, String> valueColumn;
	/**
	 * The toolbar of the properties editor.
	 */
	private ToolbarEditorElement<PropertyPair> toolbar;
	
	/**
	 * Instantiates a new <code>PropertiesEditor</code>.
	 */
	public PropertiesEditor() {
		super();
		toolbar = new ToolbarEditorElement<PropertyPair>(this);
		elements.add(toolbar);
		construct(root);
	}
	
	@Override
	protected void construct(TableView<PropertyPair> root) {
		super.construct(root);
		
		root.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		root.setEditable(true);
		root.setFocusTraversable(true);
		
		keyColumn = new TableColumn<>("Key");
		keyColumn.setEditable(true);
		
		valueColumn = new TableColumn<>("Value");
		valueColumn.setEditable(true);

		keyColumn.prefWidthProperty().bind(root.widthProperty().divide(2));
		valueColumn.prefWidthProperty().bind(root.widthProperty().divide(2));
		
		keyColumn.setCellFactory(param -> new EditingCell()); 
		valueColumn.setCellFactory(param -> new EditingCell()); 

		valueColumn.setCellValueFactory(new PropertyValueFactory<PropertyPair, String>("value"));
		valueColumn.setOnEditCommit(new EventHandler<CellEditEvent<PropertyPair, String>>() {
			@Override
			public void handle(CellEditEvent<PropertyPair, String> evt) {
				PropertyPair pair = evt.getTableView().getItems().get(evt.getTablePosition().getRow());
				if(evt.getNewValue().equals(pair.getValue())) {
					return;
				}
				
				ModifiedPropertyOperation op = new ModifiedPropertyOperation(new PropertyPair(pair), 
						new PropertyPair(pair.getValue(), evt.getNewValue()));
				perform(op);
			}
        });

		keyColumn.setCellValueFactory(new PropertyValueFactory<PropertyPair, String>("key"));
		keyColumn.setOnEditCommit(new EventHandler<CellEditEvent<PropertyPair, String>>() {
			@Override
			public void handle(CellEditEvent<PropertyPair, String> evt) {
				PropertyPair pair = evt.getTableView().getItems().get(evt.getTablePosition().getRow());
				if(evt.getNewValue().equals(pair.getKey())) {
					return;
				}
				
				ModifiedPropertyOperation op = new ModifiedPropertyOperation(new PropertyPair(pair), 
						new PropertyPair(evt.getNewValue(), pair.getValue()));
				perform(op);
			}
        });
		
		Button addButton = new Button("Add");
		addButton.setTooltip(new Tooltip("Add a property (Ctrl + Enter)"));
		addButton.disableProperty().bind(readOnlyProperty());
		addButton.setGraphic(new ImageView(EditorManager.editor().loadIcon("/resources/icons/add.png")));
		addButton.setOnAction(new EventHandler<ActionEvent>() {  
            @Override  
            public void handle(ActionEvent event) {
            	AddPropertyDialog dialog = new AddPropertyDialog(PropertiesEditor.this);
            	dialog.show();
            }  
        });
		
		Button removeButton = new Button("Remove");
		removeButton.setTooltip(new Tooltip("Remove a property (Delete)"));
		removeButton.disableProperty().bind(readOnlyProperty());
		removeButton.setGraphic(new ImageView(EditorManager.editor().loadIcon("/resources/icons/remove.png")));
		removeButton.setOnAction(new EventHandler<ActionEvent>() {  
            @Override  
            public void handle(ActionEvent event) {
            	Collection<PropertyPair> pairs = getRoot().getSelectionModel().getSelectedItems();
            	
            	ModifyCountPropertyOperation op = new ModifyCountPropertyOperation(pairs);
            	perform(op);
            }  
        });
		
		getElement(ToolbarEditorElement.class).add(1, addButton);
		getElement(ToolbarEditorElement.class).add(2, removeButton);
		
		root.getColumns().add(keyColumn);
		root.getColumns().add(valueColumn);
	}
	
	@Override
	protected void processKeyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER && event.isControlDown() && !isReadOnly()) {
			AddPropertyDialog dialog = new AddPropertyDialog(this);
        	dialog.show();
		} else if(event.getCode() == KeyCode.DELETE && !isReadOnly()) {
			Collection<PropertyPair> pairs = getRoot().getSelectionModel().getSelectedItems();
        	
        	ModifyCountPropertyOperation op = new ModifyCountPropertyOperation(pairs);
        	perform(op);
		}
		
		super.processKeyPressed(event);
	}

	@Override
	public boolean open(Path file) {
		super.open(file);
		
		this.properties = FileUtils.getProperties(file, properties);
		loadFromProperties();
		
		return true;
	}
	
	@Override
	public boolean save() {
		super.save();
		
		try (OutputStream out = Files.newOutputStream(file)) {
			properties.store(out, null);
			setDirty(false);
			return true;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Loads the <code>PropertiesEditor</code> content using the readed {@link Properties} from a file
	 * after {@link #open(Path)} has been called.
	 * <p>
	 * The internal edited properties must not be null in order to upload its content (might be empty though).
	 */
	private void loadFromProperties() {
		
		assert properties != null;
		
		ObservableList<PropertyPair> pairs = FXCollections.observableArrayList();
		for(Entry<Object, Object> entry : properties.entrySet()) {
			PropertyPair pair = new PropertyPair(entry.getKey().toString(), entry.getValue().toString());
			pairs.add(pair);
		}
		
		// Bind the search bar to the loaded list.
		toolbar.getSearchBar().bindTo(pairs);
		toolbar.getSearchBar().filter(root);
	}
	
	@Override
	public void handleAddedProperty(Object property) {
		if(property instanceof PropertyPair) {
			PropertyPair pair = (PropertyPair) property;
			properties.setProperty(pair.getKey(), pair.getValue());
			loadFromProperties();
		}
		
		if(property instanceof Collection) {
			Collection<?> props = (Collection<?>) property;
			
			Iterator<?> it = props.iterator();
			while (it.hasNext()) {
				Object value = it.next();
				if(value instanceof PropertyPair) {
					PropertyPair pair = (PropertyPair) value;
					properties.setProperty(pair.getKey(), pair.getValue());
					loadFromProperties();
				}
			}
		}
	}
	
	@Override
	public void handleRemovedProperty(Object property) {
		if(property instanceof PropertyPair) {
			PropertyPair pair = (PropertyPair) property;
			properties.remove(pair.getKey());
			loadFromProperties();
		}
		
		if(property instanceof Collection) {
			Collection<?> props = (Collection<?>) property;
			
			Iterator<?> it = props.iterator();
			while (it.hasNext()) {
				Object value = it.next();
				if(value instanceof PropertyPair) {
					PropertyPair pair = (PropertyPair) value;
					properties.remove(pair.getKey());
					loadFromProperties();
				}
			}
		}
	}
	
	@Override
	public Array<String> getSupportedExtensions() {
		return Array.of("properties");
	}
	
	@Override
	protected TableView<PropertyPair> createRoot() {
		return new TableView<>();
	}
	
	/**
	 * <code>ModifiedPropertyOperation</code> is an implementation of {@link AbstractUndoableOperation},
	 * which is used to modify a {@link PropertyPair} inside the <code>PropertiesEditor</code>.
	 * 
	 * @author GnosticOccultist
	 */
	class ModifiedPropertyOperation extends AbstractUndoableOperation {
		
		/**
		 * The old property pair.
		 */
		PropertyPair oldPair;
		/**
		 * The new property pair.
		 */
		PropertyPair newPair;
		
		/**
		 * Instantiates a new <code>ModifiedPropertyOperation</code> with the given old and new
		 * {@link PropertyPair}.
		 * 
		 * @param oldPair The old property pair (not null).
		 * @param newPair The new property pair (not null).
		 */
		public ModifiedPropertyOperation(PropertyPair oldPair, PropertyPair newPair) {
			Validator.nonNull(oldPair, "The old property pair can't be null!");
			Validator.nonNull(newPair, "The new property pair can't be null!");
			this.oldPair = oldPair;
			this.newPair = newPair;
		}
		
		@Override
		public void undo(UndoableFileEditor editor) {
			super.undo(editor);
	
			properties.setProperty(oldPair.getKey(), oldPair.getValue());
			properties.remove(newPair.getKey());
			loadFromProperties();
		}
		
		@Override
		public void redo(UndoableFileEditor editor) {
			super.redo(editor);
			
			properties.setProperty(newPair.getKey(), newPair.getValue());
			properties.remove(oldPair.getKey());
			loadFromProperties();
		}
	}
	
	/**
	 * <code>PropertyPair</code> represents a pair of key and value for a specific property inside
	 * a properties file.
	 * 
	 * @author GnosticOccultist
	 */
	public static class PropertyPair {
		
		/**
		 * The key of the property pair.
		 */
		private SimpleStringProperty key;
		/**
		 * The value of the property pair.
		 */
		private SimpleStringProperty value;
		
		/**
		 * Instantiates a new <code>PropertyPair</code> with the key and value of the 
		 * given pair.
		 * 
		 * @param other The other pair to copy from.
		 */
		public PropertyPair(PropertyPair other) {
			this(other.getKey(), other.getValue());
		}
		
		/**
		 * Instantiates a new <code>PropertyPair</code> with the given key and value
		 * for a property.
		 * 
		 * @param key	The key of the property pair.
		 * @param value The value of the property pair.
		 */
		public PropertyPair(String key, String value) {
			Validator.nonNull(key, "The key can't be null");
			Validator.nonNull(value, "The value can't be null");
			
			this.key = new SimpleStringProperty(key);
			this.value = new SimpleStringProperty(value);
		}
		
		/**
		 * Return the key of the <code>PropertyPair</code>.
		 * 
		 * @return The key of the property pair.
		 */
		public String getKey() {
			return key.get();
		}
		
		/**
		 * Sets the key of the <code>PropertyPair</code>.
		 * 
		 * @param key The key of the property pair.
		 */
		public void setKey(String key) {
			Validator.nonNull(key, "The key can't be null");
			this.key.set(key);
		}
		
		/**
		 * Return the value of the <code>PropertyPair</code>.
		 * 
		 * @return The value of the property pair.
		 */
		public String getValue() {
			return value.get();
		}
		
		/**
		 * Sets the value of the <code>PropertyPair</code>.
		 * 
		 * @param value The value of the property pair.
		 */
		public void setValue(String value) {
			Validator.nonNull(value, "The value can't be null");
			this.value.set(value);
		}
		
		/**
		 * Copy the key and value of the given pair to this <code>PropertyPair</code>.
		 * 
		 * @param other The other pair to copy from.
		 */
		public void copy(PropertyPair other) {
			Validator.nonNull(other, "The pair to copy from can't be null!");
			
			this.key.set(other.getKey());
			this.value.set(other.getValue());
		}
		
		@Override
		public String toString() {
			return key.get();
		}
	}
	
	/**
	 * <code>EditingCell</code> is an implementation of {@link TableCell} to create an editing cell
	 * by adding a {@link TextField} which allow to modify the cell text.
	 * 
	 * @author GnosticOccultist
	 */
	class EditingCell extends TableCell<PropertyPair, String> {
       
		/**
		 * The text field used to modify the cell's text.
		 */
		private TextField textField;
		/**
		 * The context menu of the cell.
		 */
		private ContextMenu menu;
		
		public EditingCell() {
			this.menu = new ContextMenu();
			
			if(!isReadOnly()) {
				setOnMouseClicked(this::onMouseClicked);
			}
		}
 
        @Override
        public void startEdit() {
        	super.startEdit();
        	setEditable(!isReadOnly());
        	
        	createTextField();
        	setText(null);
        	setGraphic(textField);
        	
        	// Select the text in the edited cell.
        	textField.selectAll();
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
 
            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
 
            if(isEditing()) {
            	if(textField != null) {
            		textField.setText(text());
            	}
            	setText(null);
            	setGraphic(textField);
            } else {
            	setText(text());
            	setGraphic(null);
            }
        }
 
        private void createTextField() {
        	if(textField == null) {
        		textField = new TextField();
        	}
        	
        	textField.setEditable(!isReadOnly());
        	textField.setText(text());
        	textField.setMinWidth(getWidth() - getGraphicTextGap() * 2);
        	textField.setOnKeyReleased(event -> {
        		if(event.getCode() == KeyCode.ENTER) {
        			commitEdit(textField.getText());
        		}
            });
        }
        
        private void onMouseClicked(MouseEvent event) {
        	menu.hide();
			menu.getItems().clear();
			
			if(event.getButton() == MouseButton.SECONDARY) {
				if(!getTableRow().isEmpty()) {
					MenuItem removeMenuItem = new MenuItem("Remove");  
		            removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
		                @Override  
		                public void handle(ActionEvent event) {
		                	Collection<PropertyPair> pairs = getRoot().getSelectionModel().getSelectedItems();
		                	
		                	ModifyCountPropertyOperation op = new ModifyCountPropertyOperation(pairs);
		                	perform(op);
		                }  
		            });
		            menu.getItems().add(removeMenuItem);
				} else {
					getRoot().getSelectionModel().clearSelection();
					MenuItem addMenuItem = new MenuItem("Add");  
					addMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
		                @Override  
		                public void handle(ActionEvent event) {
		                	AddPropertyDialog dialog = new AddPropertyDialog(PropertiesEditor.this);
		                	dialog.show();
		                }  
		            });
		            menu.getItems().add(addMenuItem);
				}
				menu.show(this, Side.BOTTOM, 0, 0);
			}
        }
        
        private String text() {
        	return getItem() == null ? "" : getItem().toString();
        }
	}
}
