package fr.alchemy.editor.core.ui.component.dialog;

import java.awt.Point;

import fr.alchemy.editor.api.AlchemyDialog;
import fr.alchemy.editor.api.undo.OperationConsumer;
import fr.alchemy.editor.core.ui.editor.text.PropertiesEditor;
import fr.alchemy.editor.core.ui.editor.text.PropertiesEditor.PropertyPair;
import fr.alchemy.editor.core.ui.editor.undo.ModifyCountPropertyOperation;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * <code>AddPropertyDialog</code> is an implementation of {@link AlchemyDialog} to add a new property in the {@link PropertiesEditor},
 * using a dialog. The user can input the property name as well as its value and the data will be sent to the editor when the 'OK' button
 * is being clicked or the ENTER key is being pressed.
 * 
 * @author GnosticOccultist
 */
public class AddPropertyDialog extends AlchemyDialog {

	/**
	 * The dialog size.
	 */
	private static final Point DIALOG_SIZE = new Point(300, 0);
	
	/**
	 * The property name text field.
	 */
	private TextField propertyNameField;
	/**
	 * The property value text field.
	 */
	private TextField propertyValueField;
	/**
	 * The consumer of the operation.
	 */
	private final OperationConsumer consumer;
	
	/**
	 * Instantiates a new <code>AddPropertyDialog</code> for the provided {@link OperationConsumer} to handle
	 * the operation changes.
	 * 
	 * @param consumer The consumer to handle the operation changes.
	 */
	public AddPropertyDialog(OperationConsumer consumer) {
		this.consumer = consumer;
	}

	@Override
	protected void createContent(GridPane root) {
		super.createContent(root);
		
		final Label propertyNameLabel = new Label("Name" + ":");
		propertyNameLabel.prefWidthProperty().bind(root.widthProperty().multiply(0.5));
        
		propertyNameField = new TextField();
		propertyNameField.prefWidthProperty().bind(root.widthProperty().multiply(0.5));
        
		final Label propertyValueLabel = new Label("Value" + ":");
        propertyValueLabel.prefWidthProperty().bind(root.widthProperty().multiply(0.5));
        
        propertyValueField = new TextField();
        propertyValueField.prefWidthProperty().bind(root.widthProperty().multiply(0.5));
        
        root.add(propertyNameLabel, 0, 0);
        root.add(propertyNameField, 1, 0);
        root.add(propertyValueLabel, 0, 1);
        root.add(propertyValueField, 1, 1);
	}
	
	@Override
	protected void processOK() {
		super.processOK();
	
		consumer.perform(new ModifyCountPropertyOperation(new PropertyPair(
				propertyNameField.getText(), propertyValueField.getText()), false));
	}
	
	@Override
	protected boolean useGridStructure() {
		return true;
	}
	
	@Override
	protected boolean needCloseButton() {
		return true;
	}
	
	@Override
	protected boolean needOKButton() {
		return true;
	}
	
	@Override
	protected boolean isResizable() {
		return false;
	}
	
	@Override
	protected Point getSize() {
		return DIALOG_SIZE;
	}
	
	@Override
	protected String getTitleText() {
		return "Add a property pair";
	}
}
