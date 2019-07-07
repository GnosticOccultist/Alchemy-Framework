package fr.alchemy.editor.core.ui.component.asset.tree;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.editor.core.ui.FXUtils;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElementFactory;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetFolderElement;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetFoldersElement;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.LoadingElement;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.ContextMenuFiller;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.ContextMenuFillerRegistry;
import fr.alchemy.editor.core.ui.component.asset.tree.filler.items.OpenFileItem;
import fr.alchemy.utilities.file.FileUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeItem.TreeModificationEvent;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * <code>AssetTree</code> is an implementation of {@link TreeView} designed to
 * display a hierarchy of {@link AssetElement}.
 * <p>
 * The tree can be interactive, but you will need to construct a {@link ContextMenu} using
 * a {@link ContextMenuFiller} to fill a specific element with menu content.
 * 
 * @author GnosticOccultist
 */
public class AssetTree extends TreeView<AssetElement> {
	
	/**
	 * The default opening action for an asset element.
	 */
	private static final Consumer<AssetElement> DEFAULT_OPEN_FUNCTION = 
			element -> new OpenFileItem(element)
				.getOnAction().handle(null); 
	
	/**
	 * The asset tree event handler.
	 */
	private final EventHandler<TreeModificationEvent<AssetElement>> treeItemEventHandler;
	/**
	 * Whether to only show the folders.
	 */
	private boolean onlyFolders;
	/**
	 * The optional function to open the asset element, it will use the default if not specified.
	 */
	private Optional<Consumer<AssetElement>> openFunction;
	/**
	 * The post-loading handler.
	 */
	private Consumer<Boolean> postLoad;
	/**
	 * The handler for listening expand items.
	 */
	private BiConsumer<Integer, AssetTree> expandHandler;
	/**
	 * The list of extension filters
	 */
	private Array<String> extensionFilter;
	/**
	 * Whether to use lazy mode.
	 */
	private boolean lazyMode;
	
	/**
	 * Instantiates a new <code>AssetTree</code> with the default opening action for {@link AssetElement}.
	 */
	public AssetTree() {
		this(null);
	}
	
	/**
	 * Instantiates a new <code>AssetTree</code>.
	 */
	public AssetTree(Consumer<AssetElement> openFunction) {
		this.openFunction = Optional.ofNullable(openFunction);
		this.extensionFilter = Array.ofType(String.class);
		this.treeItemEventHandler = this::processChangedExpands;
		
		getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		setShowRoot(true);
		setFocusTraversable(true);
		setContextMenu(new ContextMenu());
		setCellFactory(param -> new AssetTreeCell());
		
		rootProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != null) {
				oldValue.removeEventHandler(TreeItem.treeNotificationEvent(), treeItemEventHandler);
			}
			if (newValue != null) {
				newValue.addEventHandler(TreeItem.treeNotificationEvent(), treeItemEventHandler);
			}
		});
	}
	
	/**
	 * Fills the <code>AssetTree</code> with the specified root folder.
	 * 
	 * @param rootFolder The list of root folder.
	 */
	public void fill(Path rootFolder) {
		prepareToFill();
		ForkJoinPool.commonPool().execute(() -> startBackgroundFill(rootFolder));
	}
	
	/**
	 * Fills the <code>AssetTree</code> with the specified list of root folders.
	 * 
	 * @param rootFolder The list of root folders.
	 */
	public void fill(Array<Path> rootFolder) {
		prepareToFill();
		ForkJoinPool.commonPool().execute(() -> startBackgroundFill(rootFolder));
	}
	
	/**
	 * Prepares the <code>AssetTree</code> before performing 
	 * the filling action.
	 */
	protected void prepareToFill() {
		
		Consumer<Boolean> postLoadHandler = getPostLoad();
		if (postLoadHandler != null) {
			postLoadHandler.accept(Boolean.FALSE);
		}
		
        TreeItem<AssetElement> currentRoot = getRoot();
        if (currentRoot != null) {
            setRoot(null);
        }

        showLoading();
	}
	
	/**
	 * Starts the background process of filling the <code>AssetTree</code>.
	 * 
	 * @param path The root folder to fill from.
	 */
    private void startBackgroundFill(Path path) {
    	AssetElement rootElement = AssetElementFactory.createFor(path);
    	TreeItem<AssetElement> newRoot = new TreeItem<AssetElement>(rootElement);
        newRoot.setExpanded(true);

        fill(newRoot);

        FXUtils.performFXThread(() -> applyNewRoot(newRoot));
    }
    
    /**
	 * Starts the background process of filling the <code>AssetTree</code>.
	 * 
	 * @param path The list of root folders to fill from.
	 */
    private void startBackgroundFill(Array<Path> paths) {
    	AssetElement rootElement = new AssetFoldersElement(paths);
    	TreeItem<AssetElement> newRoot = new TreeItem<AssetElement>(rootElement);
        newRoot.setExpanded(true);

        fill(newRoot);

        FXUtils.performFXThread(() -> applyNewRoot(newRoot));
    }
    
    /**
     * Applies the new root for the <code>AssetTree</code> and perform the 
     * post loading action if specified using {@link #setPostLoad(Consumer)}.
     * 
     * @param newRoot The new root.
     */
    private void applyNewRoot(TreeItem<AssetElement> newRoot) {
        setRoot(newRoot);

        Consumer<Boolean> onLoadHandler = getPostLoad();
        if (onLoadHandler != null) {
            onLoadHandler.accept(Boolean.TRUE);
        }
    }
	
	/**
     * Show the <code>LoadingElement</code> inside the <code>AssetTree</code>.
     */
    private void showLoading() {
        setRoot(new TreeItem<>(LoadingElement.getInstance()));
    }
	
	/**
	 * Acquires a new {@link ContextMenu} for the specified <code>AssetElement</code>.
	 * 
	 * @param element The element to get the menu for.
	 * @return		  The created context menu for the element.
	 */
	protected ContextMenu acquireContextMenu(AssetElement element) {
		
		ContextMenu menu = new ContextMenu();
		ObservableList<TreeItem<AssetElement>> selectedItems = getSelectionModel().getSelectedItems();
		
		if(selectedItems.size() == 1) {
			ContextMenuFillerRegistry.filler().fill(element, menu.getItems());
		}
		
		if(menu.getItems().isEmpty()) {
			return null;
		}
		
		return menu;
	}
	
	/**
	 * Handles the changed count of expanded elements.
	 * 
	 * @param event The event about expanded or collapsed elements.
	 */
    private void processChangedExpands(TreeModificationEvent<?> event) {
        if (!(event.wasExpanded() || event.wasCollapsed())) {
            return;
        }
        
        if(isLazyMode()) {
        	lazyLoadChildren();
        }
        
        getExpandHandler().ifPresent(handler -> handler.accept(getExpandedItemCount(), this));
    }
    
    /**
     * Expands the specified file in the <code>AssetTree</code>.
     * 
     * @param file		 The file to expand to.
     * @param needSelect Whether the file needs to be selected.
     */
    public void expandTo(Path file, boolean needSelect) {
    	
    	if(isLazyMode()) {
    		
    		TreeItem<AssetElement> targetItem = FXUtils.findItemForValue(getRoot(), file);
        	if (targetItem == null) {

        		TreeItem<AssetElement> parentItem = null;
                Path parent = file.getParent();

                while (parent != null) {
                    parentItem = FXUtils.findItemForValue(getRoot(), parent);
                    if (parentItem != null) {
                        break;
                    }

                    parent = parent.getParent();
                }

                if (parentItem == null) {
                    parentItem = getRoot();
                }

                TreeItem<AssetElement> toLoad = parentItem;
                ForkJoinPool.commonPool().execute(() -> lazyLoadChildren(toLoad, item -> expandTo(file, needSelect)));
                return;
        	}
            ObservableList<TreeItem<AssetElement>> children = targetItem.getChildren();
            if (children.size() == 1 && children.get(0).getValue() == LoadingElement.getInstance()) {
            	ForkJoinPool.commonPool().execute(() -> lazyLoadChildren(targetItem, item -> expandTo(file, needSelect)));
                return;
            }
    	}
    
        AssetElement element = AssetElementFactory.createFor(file);
        TreeItem<AssetElement> treeItem = FXUtils.findItemForValue(getRoot(), element);
        if (treeItem == null) {
            return;
        }

        TreeItem<AssetElement> parent = treeItem;
        while (parent != null) {
            parent.setExpanded(true);
            parent = parent.getParent();
        }

        if (needSelect) {
            scrollToAndSelect(treeItem);
        }
    }
    
    /**
     * Scrolls to the specified item from the <code>AssetTree</code> and 
     * select it.
     * 
     * @param treeItem The tree item to select.
     */
    private void scrollToAndSelect(TreeItem<AssetElement> treeItem) {
        Platform.runLater(() -> {
            MultipleSelectionModel<TreeItem<AssetElement>> selectionModel = getSelectionModel();
            selectionModel.clearSelection();
            selectionModel.select(treeItem);
            scrollTo(getRow(treeItem));
        });
    }
    
    private void lazyLoadChildren() {

        List<TreeItem<AssetElement>> expanded = new ArrayList<>();

        FXUtils.allItems(getRoot())
                .filter(TreeItem::isExpanded)
                .filter(treeItem -> !treeItem.isLeaf())
                .filter(item -> item.getChildren().size() == 1)
                .filter(item -> item.getChildren().get(0).getValue() == LoadingElement.getInstance())
                .forEach(expanded::add);

        for (TreeItem<AssetElement> treeItem : expanded) {
        	ForkJoinPool.commonPool().execute(() -> lazyLoadChildren(treeItem, null));
        }
    }

    /**
     * Load children of the tree item in the background.
     *
     * @param treeItem the tree item.
     */
    private void lazyLoadChildren(TreeItem<AssetElement> treeItem, Consumer<TreeItem<AssetElement>> callback) {

    	AssetElement element = treeItem.getValue();
    	Array<AssetElement> children = element.getChildren(extensionFilter, isOnlyFolders());
        if (children == null) {
            return;
        }

      
       Platform.runLater(() -> lazyLoadChildren(treeItem, children, callback));
    }

    /**
     * Show loaded children in the tree.
     *
     * @param treeItem the tree item.
     * @param children the loaded children.
     * @param callback the loading callback.
     */
    private void lazyLoadChildren(TreeItem<AssetElement> treeItem, Array<AssetElement> children, Consumer<TreeItem<AssetElement>> callback) {

    	ObservableList<TreeItem<AssetElement>> items = treeItem.getChildren();
        if (items.size() != 1 || items.get(0).getValue() != LoadingElement.getInstance()) {
            if (callback != null) callback.accept(treeItem);
            return;
        }

        children.forEach(child -> items.add(new TreeItem<>(child)));

        items.remove(0);
        items.forEach(this::fill);

        if (callback != null) {
            callback.accept(treeItem);
        }
    }
	
    /**
     * Fills the specified {@link AssetElement} from the <code>AssetTree</code>.
     * 
     * @param treeItem The tree item to fill.
     */
	private void fill(TreeItem<AssetElement> treeItem) {
		AssetElement element = treeItem.getValue();
		Array<String> extensionFilter = getExtensionFilter();
		
		if(!element.hasChildren(extensionFilter, isOnlyFolders())) {
			return;
		}
		
		ObservableList<TreeItem<AssetElement>> items = treeItem.getChildren();
		
		if(isLazyMode()) {
			items.add(new TreeItem<>(LoadingElement.getInstance()));
		} else {
			
			// Get the children of the element to fill them as well.
            Array<AssetElement> children = element.getChildren(extensionFilter, isOnlyFolders());
            if (children == null || children.isEmpty()) {
                return;
            }

            children.forEach(child -> items.add(new TreeItem<>(child)));

            items.forEach(this::fill);
		}
	}
	
	public Consumer<Boolean> getPostLoad() {
		return postLoad;
	}
	
	public void setPostLoad(final Consumer<Boolean> postLoad) {
		this.postLoad = postLoad;
	}
	
	protected boolean isOnlyFolders() {
		return onlyFolders;
	}
	
	public AssetTree setOnlyFolders(boolean onlyFolders) {
		this.onlyFolders = onlyFolders;
		return this;
	}
	
	public Array<String> getExtensionFilter() {
		return extensionFilter;
	}
	
	public boolean isLazyMode() {
		return lazyMode;
	}
	
	public AssetTree enableLazyMode() {
		this.lazyMode = true;
		return this;
	}
	
	public Optional<BiConsumer<Integer, AssetTree>> getExpandHandler() {
		return Optional.ofNullable(expandHandler);
	}
	
	public void setExpandHandler(BiConsumer<Integer, AssetTree> expandHandler) {
		this.expandHandler = expandHandler;
	}
	
	/**
	 * <code>AssetTreeCell</code> is an implementation of {@link TreeCell} for the {@link AssetElement}.
	 * 
	 * @author GnosticOccultist
	 */
	public class AssetTreeCell extends TreeCell<AssetElement> {
		
		/**
		 * The icon for the asset element cell.
		 */
		private final ImageView icon;
		
		/**
		 * Instantiates a new <code>AssetTreeCell</code>.
		 */
		public AssetTreeCell() {
			this.icon = new ImageView();
			setOnMouseClicked(this::handleMouseClickEvent);
		}
		
		/**
		 * Update the asset element of the <code>AssetTreeCell</code>, by
		 * setting a text and an icon.
		 */
		@Override
		protected void updateItem(AssetElement item, boolean empty) {
			super.updateItem(item, empty);
			
			if(item == null) {
				setText("");
				setGraphic(null);
				return;
			} else if(item instanceof LoadingElement) {
				setText("Loading...");
				setGraphic(createIndicator());
				return;
			}
			
			Path file = item.getFile();
			Path fileName = file.getFileName();
			boolean folder = item instanceof AssetFolderElement;
			
			if(FileUtils.getExtension(file).equals("properties")) {
				icon.setImage(EditorManager.editor().loadIcon("resources/icons/property.png"));
			} else if(FileUtils.getExtension(file).equals("xml")) {
				icon.setImage(EditorManager.editor().loadIcon("resources/icons/graph-node.png"));
			} else {
				icon.setImage(EditorManager.editor().loadIcon(
						folder ? "resources/icons/folder.png" : "resources/icons/file.png"));
			}
			
			setText(fileName == null ? file.toString() : fileName.toString());
			setGraphic(icon);
		}
		
		private void handleMouseClickEvent(MouseEvent event) {
			AssetElement item = getItem();
			if(item == null) {
				return;
			}
			
			AssetTree treeView = (AssetTree) getTreeView();
			
			if (event.getButton() == MouseButton.SECONDARY) {
				ContextMenu menu = treeView.acquireContextMenu(item);
				if(menu == null) {
					return;
				}
				
				menu.show(this, Side.BOTTOM, 0, 0);	
				
			}  else if ((treeView.isOnlyFolders() || !(item instanceof AssetFolderElement)) 
					&& event.getButton() == MouseButton.PRIMARY && event.getClickCount() > 1) {
				
				openFunction.orElse(DEFAULT_OPEN_FUNCTION).accept(item);
			}
		}
		
		/**
		 * Creates the {@link ProgressIndicator} for the {@link LoadingElement}.
		 * 
		 * @return The indicator used for the loading element.
		 */
		private ProgressIndicator createIndicator() {
			ProgressIndicator indicator = new ProgressIndicator();
			indicator.setMaxHeight(20);
			indicator.setMaxWidth(20);
			return indicator;
		}
	}
}
