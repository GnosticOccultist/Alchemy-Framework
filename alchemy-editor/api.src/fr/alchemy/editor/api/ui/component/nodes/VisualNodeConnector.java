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
	 * The parameter owning the connector.
	 */
	protected final VisualNodeParameter parameter;
	/**
	 * The X coordinate property of the connector.
	 */
	protected final DoubleProperty xProperty;
	/**
	 * The Y coordinate property of the connector.
	 */
	protected final DoubleProperty yProperty;
	
	/**
	 * Instantiates a new <code>VisualNodeConnector</code> for the provided {@link VisualNodeParameter}.
	 * 
	 * @param parameter The node parameter for which to create the connector (not null).
	 */
	public VisualNodeConnector(VisualNodeParameter parameter) {
		Validator.nonNull(parameter, "The node parameter can't be null!");
		
		this.parameter = parameter;
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
	
	/**
	 * Return the {@link VisualNodeParameter} owning the <code>VisualNodeConnector</code>.
	 * 
	 * @return The parameter owning the connector.
	 */
	public VisualNodeParameter getParameter() {
		return parameter;
	}
}
