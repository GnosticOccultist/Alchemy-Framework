package fr.alchemy.editor.core.ui.component;

import java.nio.file.Path;

import fr.alchemy.editor.api.editor.EditorTool;
import fr.alchemy.editor.core.config.EditorConfig;
import fr.alchemy.editor.core.ui.component.asset.tree.AssetTree;
import fr.alchemy.utilities.Validator;
import javafx.scene.layout.VBox;

/**
 * <code>WorkspaceComponent</code> is the component displaying the currently used
 * workspace by using an {@link AssetTree}. It can be used to create, open or delete files.
 * 
 * @author GnosticOccultist
 */
public class WorkspaceComponent extends VBox implements EditorTool {
	
	/**
	 * The asset tree representing the workspace folder.
	 */
	private AssetTree workspaceTree;
	
	/**
	 * Instantiates a new <code>WorkspaceComponent</code> used as a graphic interface
	 * for the currently used workspace and interacting with files.
	 */
	public WorkspaceComponent() {
		
	}

	/**
	 * Creates the content needed for the <code>WorkspaceComponent</code>.
	 */
	public void createContent() {
		
		this.workspaceTree = new AssetTree();
		this.workspaceTree.prefHeightProperty().bind(heightProperty());
		
		getChildren().add(workspaceTree);
	}

	/**
	 * Switches to a new workspace by updating the <code>AssetTree</code> of the
	 * <code>WorkspaceComponent</code>.
	 */
	public void switchWorkspace() {
		Path currentAsset = EditorConfig.config()
				.getCurrentWorkspace();
       
        if (currentAsset == null) {
            return;
        }

        getWorkspaceTree().fill(currentAsset);
	}
	
	/**
	 * Return the name used by the component.
	 * 
	 * @return The name of the component.
	 */
	@Override
	public String getName() {
		return "Workspace";
	}
	
	/**
	 * Loads the workspace folder into the <code>AssetTree</code>, if
	 * any exists in the {@link EditorConfig}.
	 */
	@Override
	public void finish() {
		switchWorkspace();
	}
	
	/**
	 * Return the <code>AssetTree</code> used by the <code>WorkspaceComponent</code>.
	 * 
	 * @return The asset tree representing the workspace folder.
	 */
	public AssetTree getWorkspaceTree() {
		return Validator.nonNull(workspaceTree);
	}
}
