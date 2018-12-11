package fr.alchemy.editor.core.ui.component.asset.tree.filler;

import fr.alchemy.core.event.AlchemyEventManager;
import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.editor.core.event.EmptySceneCreationEvent;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class NewFileMenu extends Menu {
	
	public NewFileMenu(AssetElement element) {
		setText("New file");
		
		MenuItem item = new MenuItem("Empty scene");
		item.setOnAction(this::execute);
		item.setGraphic(new ImageView(EditorManager.editor().loadIcon("/resources/icons/save.png")));
		
		getItems().add(item);
	}
	
	private void execute(ActionEvent event) {
		AlchemyEventManager.events().notify(new EmptySceneCreationEvent());
	}
}
