package fr.alchemy.editor.core.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.editor.EditorComponent;
import fr.alchemy.editor.api.editor.graph.GraphSkinDictionary;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectorSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import fr.alchemy.utilities.Validator;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;

public final class FXUtils {

	/**
	 * No instantiation of <code>FXUtils</code>.
	 */
	private FXUtils() {}
	
    public static <T> TreeItem<T> findItemForValue(final TreeItem<T> root, final Object object) {

        if (object == null) {
            return null;
        } else if (Objects.equals(root.getValue(), object)) {
            return root;
        }

        final ObservableList<TreeItem<T>> children = root.getChildren();

        if (!children.isEmpty()) {
            for (final TreeItem<T> treeItem : children) {
                final TreeItem<T> result = findItemForValue(treeItem, object);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }
    
    public static <T> Stream<TreeItem<T>> allItems(TreeItem<T> root) {
    	List<TreeItem<T>> container = new ArrayList<>();
        collectAllItems(container, root);
        return container.stream();
    }

    public static <T> void collectAllItems(final List<TreeItem<T>> container, final TreeItem<T> root) {
        container.add(root);

        final ObservableList<TreeItem<T>> children = root.getChildren();

        for (final TreeItem<T> child : children) {
            collectAllItems(container, child);
        }
    }
    
    public static void fillComponents(Array<EditorComponent> container, Node node) {

        if (node instanceof EditorComponent) {
            container.add((EditorComponent) node);
        }

        if (node instanceof SplitPane) {
        	ObservableList<Node> items = ((SplitPane) node).getItems();
            items.forEach(child -> fillComponents(container, child));
        } else if (node instanceof TabPane) {
        	ObservableList<Tab> tabs = ((TabPane) node).getTabs();
            tabs.forEach(tab -> fillComponents(container, tab.getContent()));
        }
        
        if (!(node instanceof Parent)) {
            return;
        }

        ObservableList<Node> nodes = ((Parent) node).getChildrenUnmodifiable();
        nodes.forEach(child -> fillComponents(container, child));
    }
    
    public static <T extends Node> void fillComponents(Array<T> container, Node node, Class<T> type) {

    	if (type.isInstance(container)) {
    		container.add(type.cast(node));
    	}

    	if (!(node instanceof Parent)) {
    		return;
    	}

    	final ObservableList<Node> nodes = ((Parent) node).getChildrenUnmodifiable();
    	nodes.forEach(child -> fillComponents(container, child, type));
    }
    
    /**
     * Return the position of the cursor relative to the provided {@link Node}.
     * 
     * @param event The mouse event storing the cursor position.
     * @param node	The node to get the relative position from.
     * @return		The cursor's position relative to the node origin.
     */
    public static Point2D cursorPosition(final MouseEvent event, final Node node) {
    	Validator.nonNull(event, "The mouse event can't be null");
    	Validator.nonNull(node, "The node can't be null");
    	
        final double sceneX = event.getSceneX();
        final double sceneY = event.getSceneY();

        final Point2D containerScene = node.localToScene(0, 0);
        return new Point2D(sceneX - containerScene.getX(), sceneY - containerScene.getY());
    }
    
    public static Point2D getConnectorPosition(final GraphConnector connector, final GraphSkinDictionary skinManager) {
        final GraphConnectorSkin connectorSkin = skinManager.retrieveConnector(connector);
        final GraphNode parent = connector.getParent();

        final GraphNodeSkin nodeSkin = skinManager.retrieveNode(parent);
        if (nodeSkin == null) {
            return null;
        }

        nodeSkin.layoutConnectors();

        final double nodeX = nodeSkin.getRoot().getLayoutX();
        final double nodeY = nodeSkin.getRoot().getLayoutY();

        final Point2D connectorPosition = nodeSkin.getConnectorPosition(connectorSkin);

        final double connectorX = connectorPosition.getX();
        final double connectorY = connectorPosition.getY();

        return new Point2D(moveOnPixel(nodeX + connectorX), moveOnPixel(nodeY + connectorY));
    }
    
    /**
     * Moves an X or Y position value on-pixel.
     * Lines drawn off-pixel look blurry. They should therefore have integer X and Y values.
     * 
     * @param position The position to move on-pixel.
     * @return		   The position rounded to the nearest integer.
     */
    public static double moveOnPixel(final double position) {
    	return Math.ceil(position);
    }
    
    /**
     * Moves an X or Y position value off-pixel.
     * This is useful for a 1-pixel-wide stroke with a stroke-type of centered. The X and Y
     * coordinates need to be off-pixel so that the stroke is on-pixel.
     * 
     * @param position The position to move on-pixel.
     * @return		   The position rounded to the nearest integer.
     */
    public static double moveOffPixel(final double position) {
    	return Math.ceil(position) - 0.5D;
    }
    
    /**
     * Return whether the {@link Clipboard} has file in its content.
     * 
     * @return Whether the clipboard contains files.
     */
    @SuppressWarnings("unchecked")
    public static boolean hasFileInClipboard() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard == null) return false;
		final List<File> files = (List<File>) clipboard.getContent(DataFormat.FILES);
        return !(files == null || files.isEmpty());
    }
    
    /**
     * Performs the specified {@link Runnable} task inside the <b>JavaFX-Thread</b>, by either running it 
     * directly if the function was already called from the right thread or at some unspecified time in the future.
     * <p>
     * Note that each task are executed in the order they are submitted. It's also encouraged that long-running tasks
     * should be done on a background thread when possible.
     * 
     * @param task The task to be performed in the FX-Thread.
     */
    public static void performFXThread(Runnable task) {
    	if(Platform.isFxApplicationThread()) {
    		task.run();
    	} else {
    		Platform.runLater(task);
    	}
    }
}
