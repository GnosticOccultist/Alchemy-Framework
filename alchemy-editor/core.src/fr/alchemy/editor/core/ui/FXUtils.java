package fr.alchemy.editor.core.ui;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.collections.array.Array;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseEvent;

public final class FXUtils {

	/**
	 * Private constructor to inhibit instantiation of <code>FXUtils</code>.
	 */
	private FXUtils() {}
	
	/**
	 * Finds the {@link TreeItem} containing the provided object value if any in the given {@link TreeView}. 
	 * The method will do a recursive check starting from the root of the tree to all descending children in the hierarchy.
	 * 
	 * @param treeView The tree view in which to search for the item (not null).
	 * @param object   The object to find in the tree.
	 * @return		   The tree item containing the provided object, or null if none.
	 */
	public static <T> TreeItem<T> findItemForValue(TreeView<T> treeView, Object object) {
		Validator.nonNull(treeView, "The tree view can't be null!");
		return findItemForValue(treeView.getRoot(), object);
	}
	
	/**
	 * Finds the {@link TreeItem} containing the provided object value if any. The method will do a 
	 * recursive check starting from the provided item to all descending children in the hierarchy.
	 * 
	 * @param root	 The root tree item to start from.
	 * @param object The object to find in the tree.
	 * @return		 The tree item containing the provided object, or null if none.
	 */
    public static <T> TreeItem<T> findItemForValue(TreeItem<T> root, Object object) {
        if(object == null) {
            return null;
        } else if (Objects.equals(root.getValue(), object)) {
            return root;
        }

        ObservableList<TreeItem<T>> children = root.getChildren();
        if(!children.isEmpty()) {
            for(TreeItem<T> treeItem : children) {
                TreeItem<T> result = findItemForValue(treeItem, object);
                if(result != null) {
                    return result;
                }
            }
        }
        return null;
    }
    
    /**
     * Return a {@link Stream} of all items present in a hierarchy starting from the given {@link TreeItem}.
     * Note that it will only add non-null item to the stream.
     * 
     * @param root The root tree item to start from.
     * @return	   A stream of all the tree items (not null).
     */
    public static <T> Stream<TreeItem<T>> allItems(TreeItem<T> root) {
        return collectAllItems(null, root).stream();
    }

    /**
     * Return an {@link Array} containing all items present in a hierarchy starting from the given {@link TreeItem}.
     * Note that it will only add non-null item to the array.
     * 
     * @param store The array to store the items in, or null to create a new array.
     * @param root  The root tree item to start from.
     * @return		An array of all the tree items either the store or a new instance one.
     */
    public static <T> Array<TreeItem<T>> collectAllItems(Array<TreeItem<T>> store, TreeItem<T> root) {
    	if(store == null) {
    		store = Array.ofType(TreeItem.class);
    	}
    	if(root != null) {
    		store.add(root);
    	}

    	ObservableList<TreeItem<T>> children = root.getChildren();
        for(TreeItem<T> child : children) {
            collectAllItems(store, child);
        }
        
        return store;
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
    	performFXThread(task, false);
    }
    
    /**
     * Performs the specified {@link Runnable} task inside the <b>JavaFX-Thread</b>, by either running it 
     * directly if the function was already called from the right thread or at some unspecified time in the future.
     * <p>
     * Note that each task are executed in the order they are submitted. It's also encouraged that long-running tasks
     * should be done on a background thread when possible.
     * 
     * @param task  The task to be performed in the FX-Thread.
     * @param force Whether to force the task to be executed in the JavaFX thread.
     */
    public static void performFXThread(Runnable task, boolean force) {
    	if(!Platform.isFxApplicationThread() || force) {
    		Platform.runLater(task);
    	} else {
    		task.run();
    	}
    }
}
