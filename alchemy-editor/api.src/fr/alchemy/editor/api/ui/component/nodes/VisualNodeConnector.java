package fr.alchemy.editor.api.ui.component.nodes;

import fr.alchemy.utilities.Validator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.PseudoClass;
import javafx.scene.layout.Pane;

public class VisualNodeConnector extends Pane {

	/**
	 * The connected state of the connector.
	 */
	private final BooleanProperty connected = new BooleanPropertyBase(false) {
		
		@Override
		public  void invalidated() {
			pseudoClassStateChanged(PseudoClass.getPseudoClass("connected"), get());
		}
		
		@Override
		public Object getBean() {
			return VisualNodeConnector.this;
		}

		@Override
		public String getName() {
			return "connected";
		}
	};
	
	/**
	 * The visual node element owning the connector.
	 */
	protected final VisualNodeElement element;
	/**
	 * The X coordinate property of the connector.
	 */
	protected final DoubleProperty xProperty;
	/**
	 * The Y coordinate property of the connector.
	 */
	protected final DoubleProperty yProperty;
	
	/**
	 * Instantiates a new <code>VisualNodeConnector</code> for the provided {@link VisualNodeElement}.
	 * 
	 * @param element The node element for which to create the connector (not null).
	 */
	public VisualNodeConnector(VisualNodeElement element) {
		Validator.nonNull(element, "The node element can't be null!");
		
		this.element = element;
		this.xProperty = new SimpleDoubleProperty();
		this.yProperty = new SimpleDoubleProperty();
	}
	
	/**
	 * Return whether the <code>VisualNodeConnector</code> is connected to another {@link VisualNodeElement}.
	 * 
	 * @return Whether the connector of the element is connected.
	 */
	public boolean connected() {
		return connected.get();
	}
	
	/**
	 * Sets whether the <code>VisualNodeConnector</code> is connected to another {@link VisualNodeElement}.
	 * 
	 * @param connected Whether the connector of the element is connected.
	 */
	public void setConnected(boolean connected) {
		this.connected.setValue(connected);
	}
}
