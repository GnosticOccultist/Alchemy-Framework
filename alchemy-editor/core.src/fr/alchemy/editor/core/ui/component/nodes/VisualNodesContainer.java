package fr.alchemy.editor.core.ui.component.nodes;

import javafx.scene.control.ScrollPane;

public class VisualNodesContainer extends ScrollPane {

	public VisualNodesContainer() {
		
		setPannable(true);
		setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		setFitToHeight(true);
		setFitToWidth(true);
	}
}
