package fr.alchemy.editor.core.ui.component.asset.tree.filler.items;

import fr.alchemy.core.event.AlchemyEventManager;
import fr.alchemy.editor.core.EditorManager;
import fr.alchemy.editor.core.event.AlchemyEditorEvent;
import fr.alchemy.editor.core.ui.component.asset.tree.elements.AssetElement;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

public class OpenFileItem extends MenuItem {
	
	private AssetElement element;
	
	public OpenFileItem(AssetElement element) {
		
		this.element = element;
		
		setText("Open file");
		setOnAction(this::execute);
		setGraphic(new ImageView(EditorManager.editor().loadIcon("/resources/icons/visible.png")));
	}
	
	private void execute(ActionEvent event) {
		AlchemyEventManager.events().notify(AlchemyEditorEvent.newOpenFileEvent(element.getFile()));
	}
}
