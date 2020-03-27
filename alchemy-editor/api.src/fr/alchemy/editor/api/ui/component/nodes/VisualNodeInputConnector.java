package fr.alchemy.editor.api.ui.component.nodes;

import fr.alchemy.utilities.Validator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.scene.input.DragEvent;

public class VisualNodeInputConnector extends VisualNodeConnector {

	/**
	 * The interactability of the connector. 
	 */
	private final BooleanProperty interactable = new BooleanPropertyBase(false) {

		@Override
        public void invalidated() {
            pseudoClassStateChanged(PseudoClass.getPseudoClass("interactable"), get());
        }
		
		@Override
		public Object getBean() {
			return VisualNodeInputConnector.this;
		}

		@Override
		public String getName() {
			return "interactable";
		}
	};
	
	public VisualNodeInputConnector(VisualNodeParameter parameter) {
		super(parameter);
		
		setOnDragOver(this::processDragOver);
		setOnDragExited(this::processDragExited);
	}

	/**
	 * Process the given {@link DragEvent} over the <code>VisualNodeInputConnector</code> by checking if the source can be attached.
	 * 
	 * @param event The drag event that occured (not null).
	 */
	protected void processDragOver(DragEvent event) {
		Validator.nonNull(event, "The drag event can't be null!");
		
		VisualNodeElement element = parameter.getElement();
		
		Object source = event.getSource();
		if(!(source instanceof VisualNodeConnector)) {
			return;
		}
		
		VisualNodeConnector connector = (VisualNodeConnector) source;
		VisualNodeParameter outParameter = connector.getParameter();
		
		if(!element.canAttach(parameter, outParameter)) {
			return;
		}
		
		interactable.set(true);
	}
	
	/**
	 * Process the given {@link DragEvent} which exited the <code>VisualNodeInputConnector</code> by disabling its interactibility.
	 * 
	 * @param event The drag event that occured (not null).
	 */
	protected void processDragExited(DragEvent event) {
		Validator.nonNull(event, "The drag event can't be null!");
		interactable.set(false);
	}
}
