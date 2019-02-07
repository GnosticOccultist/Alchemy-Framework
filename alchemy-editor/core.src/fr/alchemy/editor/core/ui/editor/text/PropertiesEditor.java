package fr.alchemy.editor.core.ui.editor.text;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map.Entry;
import java.util.Properties;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.AbstractFileEditor;
import fr.alchemy.editor.api.element.ToolbarEditorElement;
import fr.alchemy.editor.core.ui.editor.text.PropertiesEditor.PropertyPair;
import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.file.FileUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

/**
 * <code>PropertiesEditor</code> is an implementation of {@link AbstractFileEditor} used for editing 
 * '.properties' file.
 * 
 * @author GnosticOccultist
 */
public class PropertiesEditor extends AbstractFileEditor<TableView<PropertyPair>> {
	
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
	 * Instantiates a new <code>PropertiesEditor</code>.
	 */
	public PropertiesEditor() {
		super();
		
		elements.add(new ToolbarEditorElement(this));
		construct(root);
		
		root.setEditable(true);
		root.setFocusTraversable(true);
		keyColumn = new TableColumn<>("Key");
		valueColumn = new TableColumn<>("Value");
		
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
				
				pair.setValue(evt.getNewValue());
				properties.setProperty(pair.getKey(), pair.getValue());
				setDirty(true);
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
				
				// Remove the old key and its value from properties.
				properties.remove(pair.getKey());
				
				pair.setKey(evt.getNewValue());
				properties.setProperty(pair.getKey(), pair.getValue());
				setDirty(true);
			}
        });
		
		root.getColumns().add(keyColumn);
		root.getColumns().add(valueColumn);
	}

	@Override
	public boolean open(Path file) {
		super.open(file);
		
		// Unable to open file with an inapproriate extension.
		if(!getSupportedExtensions().contains(FileUtils.getExtension(file))) {
			return false;
		}
		
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

	private void loadFromProperties() {
		for(Entry<Object, Object> entry : properties.entrySet()) {
			PropertyPair pair = new PropertyPair(entry.getKey().toString(), entry.getValue().toString());
			root.getItems().add(pair);
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
 
        @Override
        public void startEdit() {
        	super.startEdit();
        	
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
        	
        	textField.setText(text());
            textField.setMinWidth(getWidth() - getGraphicTextGap() * 2);
            textField.setOnKeyReleased(event -> {
            	if(event.getCode() == KeyCode.ENTER) {
            		commitEdit(textField.getText());
            	}
            });
        }
        
        private String text() {
        	return getItem() == null ? "" : getItem().toString();
        }
	}
}
