package fr.alchemy.editor.core.ui.component.dialog;

import java.awt.Point;

import fr.alchemy.editor.api.AlchemyDialog;
import fr.alchemy.editor.api.undo.UndoableFileEditor;
import fr.alchemy.editor.core.ui.editor.text.PropertiesEditor.PropertyPair;
import fr.alchemy.editor.core.ui.editor.undo.ModifyCountPropertyOperation;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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
	private final UndoableFileEditor editor;
	
	public AddPropertyDialog(UndoableFileEditor editor) {
		this.editor = editor;
	}

	@Override
	protected void createContent(VBox root) {
		super.createContent(root);
		
		final Label customNameLabel = new Label("Name" + ":");
        customNameLabel.prefWidthProperty().bind(root.widthProperty().multiply(0.5));
        
        propertyNameField = new TextField();
        propertyNameField.prefWidthProperty().bind(root.widthProperty().multiply(0.5));
        
        final Label customValueLabel = new Label("Value" + ":");
        customValueLabel.prefWidthProperty().bind(root.widthProperty().multiply(0.5));
        
        propertyValueField = new TextField();
        propertyValueField.prefWidthProperty().bind(root.widthProperty().multiply(0.5));
        
        root.getChildren().addAll(customNameLabel, propertyNameField, 
        		customValueLabel, propertyValueField);
	}
	
	@Override
	protected void processOK() {
		super.processOK();
	
		editor.perform(new ModifyCountPropertyOperation(new PropertyPair(
				propertyNameField.getText(), propertyValueField.getText()), false));
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
