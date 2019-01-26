package fr.alchemy.editor.core.ui.component.dialog;

import java.awt.Point;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import com.ss.rlib.common.util.array.Array;

import fr.alchemy.editor.api.AlchemyDialog;
import fr.alchemy.editor.core.ui.component.asset.tree.AssetTree;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import fr.alchemy.utilities.Validator;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;

/**
 * <code>ExternalFileBrowserDialog</code> is an implementation of the {@link AlchemyDialog}.
 * It is used to browse and select a folder in the disk hierarchy.
 * 
 * @author GnosticOccultist
 */
public class ExternalFileBrowserDialog extends AlchemyDialog {
	
	/**
	 * The dialog size (500;500).
	 */
	protected static final Point DIALOG_SIZE = new Point(500, 500);
	
	/**
	 * The consumer which handles the selection.
	 */
	protected final Consumer<Path> consumer;
	/**
	 * The asset tree.
	 */
	protected AssetTree tree;
	/**
	 * The initial directory to look for.
	 */
	protected Path initDirectory;

	/**
	 * Instantiates a new <code>ExternalFileBrowserDialog</code> with
	 * the specified consumer to be used during selection.
	 * 
	 * @param consumer The consumer called when selecting a folder.
	 */
	public ExternalFileBrowserDialog(Consumer<Path> consumer) {
		this.consumer = consumer;
	}
	
	/**
	 * Creates the content of the <code>ExternalFileBrowserDialog</code>, mainly
	 * adding an {@link AssetTree} to it.
	 */
	@Override
	protected void createContent(VBox container) {
		
		this.tree = new AssetTree().setOnlyFolders(true).enableLazyMode();
		this.tree.prefHeightProperty().bind(heightProperty());
		this.tree.prefWidthProperty().bind(widthProperty());
		this.tree.setShowRoot(false);
		this.tree.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> processSelected(newValue));
		
		container.getChildren().add(tree);
	}
	
	/**
	 * Adds the selection processor for the <code>AssetTree</code>.
	 * 
	 * @param newValue The selected tree item.
	 */
	private void processSelected(TreeItem<AssetElement> newValue) {
		AssetElement element = newValue == null ? null : newValue.getValue();
		Path file = element == null ? null : element.getFile();
		
		okButton.setDisable(file == null || !Files.isWritable(file));
	}

	/**
	 * Shows the <code>ExternalFileBrowserDialog</code> and fill the
	 * <code>AssetTree</code>.
	 */
	@Override
	public void show() {
		super.show();
		
		Path directory = getInitDirectory();
		if(directory == null) {
			directory = Paths.get(System.getProperty("user.home"));
		}
	
		Path toExpand = directory;
		Array<Path> rootFolders = Array.ofType(Path.class);
		
		FileSystem fileSystem = FileSystems.getDefault();
        fileSystem.getRootDirectories().forEach(rootFolders::add);
		
        AssetTree tree = getTree();
        tree.setPostLoad(finished -> {
            if (finished) {
            	tree.expandTo(toExpand, true);
            }
        });
        
        if (rootFolders.size() < 2) {
        	tree.fill(rootFolders.first());
        } else {
        	tree.fill(rootFolders);
        }
        
		tree.requestFocus();
	}
	
	/**
	 * Specify the action when the 'OK' button is pressed.
	 * <p>
	 * The function will run the {@link #consumer} with the selected 
	 * folder from the <code>AssetTree</code>.
	 */
	@Override
	protected void processOK() {
		super.processOK();
		
		Path folder = getTree().getSelectionModel()
				.getSelectedItem().getValue().getFile();
		
		consumer.accept(folder);
	}
	
	/**
	 * Specify the <code>ExternalFileBrowserDialog</code> that an 'OK' button is needed.
	 */
	@Override
	protected boolean needOKButton() {
		return true;
	}
	
	/**
	 * Specify the <code>ExternalFileBrowserDialog</code> that it can't be resized.
	 */
	@Override
	protected boolean isResizable() {
		return false;
	}
	
	/**
	 * Specify the <code>ExternalFileBrowserDialog</code> that a 'Close' button is needed.
	 */
	@Override
	protected boolean needCloseButton() {
		return true;
	}
	
	/**
	 * Specify the <code>ExternalFileBrowserDialog</code> to set the size to {@value #DIALOG_SIZE}.
	 */
	@Override
	protected Point getSize() {
		return DIALOG_SIZE;
	}
	
	/**
	 * Return the asset tree used by the <code>ExternalFileBrowserDialog</code>.
	 * The tree cannot be null. 
	 * 
	 * @return The tree used by the dialog.
	 */
	private AssetTree getTree() {
		return Validator.nonNull(tree);
	}
	
	/**
	 * Return the initial directory of the <code>ExternalFileBrowserDialog</code>.
	 * 
	 * @return The initial directory for the tree.
	 */
	private Path getInitDirectory() {
		return initDirectory;
	}
	
	/**
	 * Sets the initial directory of the <code>ExternalFileBrowserDialog</code>, 
	 * to specify which path the tree should show first.
	 * 
	 * @param initDirectory The initial directory for the tree.
	 */
	public void setInitDirectory(Path initDirectory) {
		this.initDirectory = initDirectory;
	}
}
