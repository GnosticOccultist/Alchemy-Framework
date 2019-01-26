package fr.alchemy.editor.core.ui.editor.bar;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * <code>AlchemyEditorBar</code> is an implementation of {@link MenuBar} designed
 * for the <code>AlchemyEditor</code>. 
 * <p>
 * It contains a possibility to change the used workspace or exit the editor.
 * 
 * @author GnosticOccultist
 */
public class AlchemyEditorBar extends MenuBar {
	
	/**
	 * Instantiates a new <code>AlchemyEditorBar</code> with its 
	 * menus.
	 */
	public AlchemyEditorBar() {
		createMenus();
	}

	/**
	 * Creates all the menus needed for the <code>AlchemyEditorBar</code>.
	 */
	private void createMenus() {
		getMenus().addAll(createFileMenu());
	}

	/**
	 * Creates the 'file' menu which contains the possibility of changing
	 * the currently used workspace or exiting the editor.
	 * 
	 * @return The 'file' menu.
	 */
	private Menu createFileMenu() {
		Menu menu = new Menu("File");
		
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction(event -> Platform.exit());
		
		menu.getItems().addAll(new OpenWorkspace(), exit);
		
		return menu;
	}
}
