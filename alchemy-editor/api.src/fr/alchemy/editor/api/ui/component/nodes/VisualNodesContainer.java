package fr.alchemy.editor.api.ui.component.nodes;

import fr.alchemy.utilities.array.Array;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class VisualNodesContainer<E extends VisualNodeElement> extends ScrollPane {

	/**
	 * The current nodes elements present in the container.
	 */
	protected final Array<E> nodeElements;
	/**
	 * The root pane of the nodes container.
	 */
	protected final Pane root;

	public VisualNodesContainer() {
		this.nodeElements = Array.ofType(VisualNodeElement.class);
		this.root = new Pane();
		this.root.prefWidthProperty().bind(widthProperty());
		this.root.prefHeightProperty().bind(heightProperty());
		
		root.getStyleClass().add("visual-nodes-root");
		
		setPannable(true);
		setHbarPolicy(ScrollBarPolicy.NEVER);
		setVbarPolicy(ScrollBarPolicy.NEVER);
		setContent(root);
		setFitToHeight(true);
		setFitToWidth(true);
	}
	
	public void add(E node) {
		root.getChildren().add(node);
		
		nodeElements.add(node);
		
		node.setLayoutX(10D);
		node.setLayoutY(10D);
	}
	
	public void add(int index, E node) {
		root.getChildren().add(index, node);
		
		nodeElements.add(node);
		
		node.setLayoutX(10D);
		node.setLayoutY(10D);
	}
}