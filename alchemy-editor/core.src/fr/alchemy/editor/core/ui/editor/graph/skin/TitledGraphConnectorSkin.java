package fr.alchemy.editor.core.ui.editor.graph.skin;

import fr.alchemy.editor.api.editor.graph.GraphConnectorStyle;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectorSkin;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class TitledGraphConnectorSkin extends GraphConnectorSkin {

	/**
	 * The default size of the connector (width & height).
	 */
	private static final double SIZE = 15;
	
	/**
	 * The root pane of the connector.
	 */
	private final Pane root = new Pane();
	
	/**
	 * Instantiates a new <code>TitledGraphConnectorSkin</code> for the specified 
	 * {@link GraphConnector}.
	 * 
	 * @param connector The graph connector using the graph skin.
	 */
	public TitledGraphConnectorSkin(final GraphConnector connector) {
		super(connector);
		
		root.setMinSize(SIZE, SIZE);
		root.setPrefSize(SIZE, SIZE);
		root.setMaxSize(SIZE, SIZE);
		root.setPickOnBounds(false);
		
		
	}

	@Override
	public Node getRoot() {
		return root;
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
	public void applyStyle(GraphConnectorStyle style) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void selectionChanged(boolean selected) {
		// TODO Auto-generated method stub

	}
}