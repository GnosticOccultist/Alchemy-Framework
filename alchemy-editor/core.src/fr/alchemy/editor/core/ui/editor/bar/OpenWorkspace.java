package fr.alchemy.editor.core.ui.editor.bar;

import java.nio.file.Path;

import fr.alchemy.core.event.AlchemyEventManager;
import fr.alchemy.editor.core.config.EditorConfig;
import fr.alchemy.editor.core.event.AlchemyEditorEvent;
import fr.alchemy.editor.core.ui.component.dialog.ExternalFileBrowserDialog;
import fr.alchemy.utilities.SystemUtils;
import javafx.scene.control.MenuItem;

/**
 * <code>OpenWorkspace</code> is an implementation of {@link MenuItem} designed
 * to open a new workspace folder.
 * <p>
 * It uses an {@link ExternalFileBrowserDialog} to let you choose which folder should 
 * be used as a workspace.
 * 
 * @author GnosticOccultist
 */
public class OpenWorkspace extends MenuItem {
	
	/**
	 * Instantiates a new <code>OpenWorkspace</code> menu which can be
	 * used for the {@link AlchemyEditorBar}.
	 */
	protected OpenWorkspace() {
		super("Open Workspace");
		setOnAction(event -> openFileBrowser());
	}

	/**
	 * Open an <code>ExternalFileBrowserDialog</code> to decide which folder use as
	 * a workspace.
	 */
	private void openFileBrowser() {
		ExternalFileBrowserDialog dialog = new ExternalFileBrowserDialog(this::openWorkspace);
		dialog.setTitleText("Choose a workspace folder");
		
		Path currentAsset = EditorConfig.config().getCurrentWorkspace();
		
		if (currentAsset == null) {
			dialog.setInitDirectory(SystemUtils.pathToUserHome());
		} else {
			dialog.setInitDirectory(currentAsset);
		}
		
		dialog.show();
	}
	
	/**
	 * Open the chosen workspace if it isn't already and saves it into the {@link EditorConfig}.
	 * A {@link ChangedCurrentWorkspaceEvent} is raised at the end to notify about the new workspace.
	 * 
	 * @param workspace The chosen workspace.
	 */
	private void openWorkspace(Path workspace) {
		EditorConfig config = EditorConfig.config();
		
		config.setCurrentWorkspace(workspace);
		config.save();
		
		AlchemyEventManager.events().notify(AlchemyEditorEvent.newChangedCurrentWorkspaceEvent(workspace));
	}
}
