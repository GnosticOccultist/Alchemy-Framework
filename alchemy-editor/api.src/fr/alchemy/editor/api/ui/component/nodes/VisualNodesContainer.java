package fr.alchemy.editor.api.ui.component.nodes;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class VisualNodesContainer<E extends VisualNodeElement> extends ScrollPane {

	/**
	 * The root pane of the nodes container.
	 */
	private final Pane root;

	public VisualNodesContainer() {
		this.root = new Pane();
		this.root.prefWidthProperty().bind(widthProperty());
		this.root.prefHeightProperty().bind(heightProperty());
		
		root.getStyleClass().add("visual-nodes-root");
		
		setPannable(true);
		setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		setContent(root);
		setFitToHeight(true);
		setFitToWidth(true);
	}
	
	public void add(E node) {
		root.getChildren().add(node);
		
		node.setLayoutX(10D);
		node.setLayoutY(10D);
	}
	
	public void add(int index, E node) {
		root.getChildren().add(index, node);
		
		node.setLayoutX(10D);
		node.setLayoutY(10D);
	}
}
