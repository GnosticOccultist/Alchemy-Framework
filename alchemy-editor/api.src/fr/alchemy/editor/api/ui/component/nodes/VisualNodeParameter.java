package fr.alchemy.editor.api.ui.component.nodes;

import fr.alchemy.utilities.Validator;
import javafx.scene.layout.GridPane;

public abstract class VisualNodeParameter<E extends VisualNodeElement> extends GridPane {

	/**
	 * The visual node element.
	 */
	protected final E element;
	/**
	 * The connector of the parameter.
	 */
	protected final VisualNodeConnector socket;
	
	public VisualNodeParameter(E element) {
		Validator.nonNull(element, "The node element can't be null!");
		
		this.element = element;
		this.socket = createConnector();
	}
	
	protected abstract VisualNodeConnector createConnector();
	
	protected abstract void createContent();
	
	protected abstract boolean isInput();
	
	/**
	 * Return the {@link VisualNodeElement} describing the <code>VisualNodeParameter</code>.
	 * 
	 * @return The visual node element describing the parameter.
	 */
	public E getElement() {
		return element;
	}
}
