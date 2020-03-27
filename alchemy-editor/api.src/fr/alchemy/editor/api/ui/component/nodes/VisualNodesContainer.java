package fr.alchemy.editor.api.ui.component.nodes;

import java.util.Collection;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.collections.array.Array;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public abstract class VisualNodesContainer<E extends VisualNodeElement> extends ScrollPane {

	/**
	 * The current nodes elements present in the container.
	 */
	protected final Array<E> nodeElements;
	/**
	 * The root pane of the nodes container.
	 */
	protected final Pane root;
	/**
	 * The context menu of the container.
	 */
	protected final ContextMenu contextMenu;

	public VisualNodesContainer() {
		this.nodeElements = Array.ofType(VisualNodeElement.class);
		this.root = new Pane();
		this.root.prefWidthProperty().bind(widthProperty());
		this.root.prefHeightProperty().bind(heightProperty());
		this.root.setOnContextMenuRequested(this::handleContextMenu);
		this.root.setOnMousePressed(this::processMousePressed);
		
		this.contextMenu = new ContextMenu();
		
		root.getStyleClass().add("visual-nodes-root");
		
		setPannable(true);
		setHbarPolicy(ScrollBarPolicy.NEVER);
		setVbarPolicy(ScrollBarPolicy.NEVER);
		setContent(root);
		setFitToHeight(true);
		setFitToWidth(true);
	}
	
	/**
	 * Handles the provided {@link ContextMenuEvent} which occured on the <code>VisualNodesContainer</code> 
	 * or in one of its {@link VisualNodeElement}.
	 * <p>
	 * Use {@link #createAddActions(Point2D)} to register actions to add node elements on your own implementation 
	 * of the container.
	 * 
	 * @param event The context menu event (not null).
	 * 
	 * @see #createAddActions(Point2D)
	 * @see VisualNodeElement#fillAdditionalActions(ContextMenuEvent, ObservableList)
	 */
	protected void handleContextMenu(ContextMenuEvent event) {
		Validator.nonNull(event, "The context menu event can't be null!");
		
		if(contextMenu.isShowing()) {
			contextMenu.hide();
		}
		
		Object source = event.getSource();
		
		ObservableList<MenuItem> items = contextMenu.getItems();
		items.clear();
		
		Point2D location = new Point2D(event.getX(), event.getY());
		
		if(source == root) {
			Menu menu = new Menu("Add");
			menu.getItems().addAll(createAddActions(location));
			items.addAll(menu);
		} else if(source instanceof VisualNodeElement) {
			VisualNodeElement<?> element = (VisualNodeElement) source;
			element.fillAdditionalActions(event, items);
		}
		
		if(!items.isEmpty()) {
			contextMenu.show(root, event.getScreenX(), event.getScreenY());
			event.consume();
		}
	}
	
	/**
	 * Override this method to create your own actions to add a {@link VisualNodeElement} to the
	 * <code>VisualNodesContainer</code>.
	 * 
	 * @param location The location in which the context menu was requested (not null).
	 * @return		   A collection of actions to add visual nodes to the container.
	 */
	protected abstract Collection<MenuItem> createAddActions(Point2D location);
	
	/**
	 * Process the given pressed {@link MouseEvent} by saving hiding the {@link ContextMenu} and clearing selection
	 * from all {@link VisualNodeElement} contained in the <code>VisualNodesContainer</code>.
	 * 
	 * @param event The mouse event that occured (not null).
	 */
	public void processMousePressed(MouseEvent event) {
		Validator.nonNull(event, "The mouse event can't be null!");
		
		if(event.getButton() == MouseButton.PRIMARY && contextMenu.isShowing()) {
			contextMenu.hide();
		}
		
		Object source = event.getTarget();
		
		if(source == root) {
			root.getChildren().stream()
				.filter(VisualNodeElement.class::isInstance)
				.map(node -> (VisualNodeElement<?>) node)
				.forEach(element -> element.onSelected(false));
			
			event.consume();
		}
	}
	
	/**
	 * Request the selection of the provided {@link VisualNodeElement} and clear any other previously
	 * selected node element from the <code>VisualNodesContainer</code>.
	 * 
	 * @param requester 	The node element that requested the selection (not null).
	 * @param clearAnyOther Whether to clear any other selection apart from the requester.
	 */
	public void requestSelection(VisualNodeElement requester, boolean clearAnyOther) {
		Validator.nonNull(requester, "The visual node element can't be null!");
		
		if(clearAnyOther) {
			root.getChildren().stream()
				.filter(VisualNodeElement.class::isInstance)
				.map(node -> (VisualNodeElement<?>) node)
				.filter(node -> node.getID() != requester.getID())
				.forEach(element -> element.onSelected(false));
		}
		
		requester.onSelected(true);
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
	
	public void remove(E node) {
		root.getChildren().remove(node);
		
		nodeElements.remove(node);
	}
}
