package fr.alchemy.editor.core.ui.editor.graph.skin;

import fr.alchemy.editor.api.editor.graph.element.GraphJoint;
import fr.alchemy.editor.api.editor.graph.skin.GraphJointSkin;
import javafx.css.PseudoClass;

public class TitledGraphJointSkin extends GraphJointSkin {

	private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");
	
	private static final double SIZE = 12;
	
	
	public TitledGraphJointSkin(GraphJoint joint) {
		super(joint);
		
		getRoot().resize(SIZE, SIZE);
        getRoot().getStyleClass().setAll("default-joint");

        getRoot().setPickOnBounds(false);
	}

	@Override
	public double getWidth() {
		return SIZE;
	}

	@Override
	public double getHeight() {
		return SIZE;
	}

	@Override
	protected void selectionChanged(boolean selected) {
		getRoot().pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, isSelected());
		if(isSelected()) {
			getRoot().toFront();
		}
	}
}
