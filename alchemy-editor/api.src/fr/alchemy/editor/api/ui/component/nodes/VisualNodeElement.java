package fr.alchemy.editor.api.ui.component.nodes;

import java.util.UUID;

import fr.alchemy.utilities.Validator;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * <code>VisualNodeElement</code> is a visual node representing a type of element inside a {@link VisualNodesContainer}. 
 *
 * @param <E> The type of element the node represent.
 * 
 * @author GnosticOccultist
 */
public class VisualNodeElement<E> extends VBox {

	/**
	 * The unique identifier of the node.
	 */
	protected final UUID id;
	/**
	 * The element wrapped in the node.
	 */
	protected final E element;
	/**
	 * The parameters container of the node.
	 */
	protected final VBox parametersContainer;
	
	/**
	 * Instantiates a new <code>VisualNodeElement</code> to represent the provided element.
	 * 
	 * @param element The element to represent (not null).
	 */
	protected VisualNodeElement(E element) {
		Validator.nonNull(element, "The element can't be null!");
		
		this.element = element;
		this.id = UUID.randomUUID();
		this.parametersContainer = new VBox();
		
		setPrefWidth(200D);
		getStyleClass().add("visual-node");
		
		createContent();
	}
	
	/**
	 * Creates the content of the <code>VisualNodeElement</code> by adding a header with the
	 * title and a container for parameters to add using {@link #constructParameters(VBox)}.
	 */
	protected void createContent() {
		StackPane header = new StackPane();
		Label titleLabel = new Label(getTitleText());
		
		header.getChildren().add(titleLabel);
		header.getStyleClass().add("header");
		
		getChildren().add(header);
		
		constructParameters(parametersContainer);
		getChildren().add(parametersContainer);
	}
	
	/**
	 * Constructs the parameters for the <code>VisualNodeElement</code>. Override this method
	 * to implement your own parameters into an implementation of this class.
	 * 
	 * @param container The container of the parameters (not null).
	 */
	protected void constructParameters(VBox container) {}

	/**
	 * Return the title text of the <code>VisualNodeElement</code>, which is displayed
	 * in the header of the node representation.
	 * 
	 * @return The title text of the node.
	 */
	protected String getTitleText() {
		return "Title";
	}
}
