package fr.alchemy.editor.core.ui.editor.graph.skin;

import java.util.ArrayList;
import java.util.List;

import fr.alchemy.editor.api.editor.graph.GraphNodeEditor;
import fr.alchemy.editor.api.editor.graph.element.GraphConnector;
import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.skin.GraphConnectorSkin;
import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import fr.alchemy.editor.core.ui.FXUtils;
import javafx.css.PseudoClass;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class TitledGraphNodeSkin extends GraphNodeSkin {

    private VBox contentRoot = new VBox();
    
    private HBox header = new HBox();

    private Label title = new Label();
    
    private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");
   
    private static final double MIN_WIDTH = 81;
    private static final double MIN_HEIGHT = 81;

    private static final int BORDER_WIDTH = 1;
    private static final double HALO_OFFSET = 5;
    private static final double HALO_CORNER_SIZE = 10;
    private static final int HEADER_HEIGHT = 20;
    
	private final Rectangle border = new Rectangle();
	
	private final Rectangle selectionHalo = new Rectangle();
	
    private final List<GraphConnectorSkin> inputConnectorSkins = new ArrayList<>();
    private final List<GraphConnectorSkin> outputConnectorSkins = new ArrayList<>();
	
	public TitledGraphNodeSkin(final GraphNode element) {
		super(element);
		
		border.getStyleClass().setAll("titled-node-border");
        border.widthProperty().bind(getRoot().widthProperty());
        border.heightProperty().bind(getRoot().heightProperty());
        
        getRoot().getChildren().add(border);
        getRoot().setMinSize(MIN_WIDTH, MIN_HEIGHT);
        
        addSelectionHalo();
        
        createContent();
	}
	
	@Override
	public void initialize() {
		super.initialize();
		title.setText("Node");
	}

	private void createContent() {

		header.getStyleClass().setAll("titled-node-header");
        header.setAlignment(Pos.CENTER);
        
        title.getStyleClass().setAll("titled-node-title");
        
        final Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        
        header.getChildren().addAll(title, filler);
        contentRoot.getChildren().addAll(header);
        getRoot().getChildren().add(contentRoot);
        
        contentRoot.minWidthProperty().bind(getRoot().widthProperty());
        contentRoot.prefWidthProperty().bind(getRoot().widthProperty());
        contentRoot.maxWidthProperty().bind(getRoot().widthProperty());
        contentRoot.minHeightProperty().bind(getRoot().heightProperty());
        contentRoot.prefHeightProperty().bind(getRoot().heightProperty());
        contentRoot.maxHeightProperty().bind(getRoot().heightProperty());

        contentRoot.setLayoutX(BORDER_WIDTH);
        contentRoot.setLayoutY(BORDER_WIDTH);
        contentRoot.getStyleClass().setAll("titled-node-background");
	}

	@Override
	public void layoutConnectors() {
		layoutLeftAndRightConnectors();
		layoutSelectionHalo();
	}

	private void layoutLeftAndRightConnectors() {
        final int inputCount = inputConnectorSkins.size();
        final double inputOffsetY = (getRoot().getHeight() - HEADER_HEIGHT) / (inputCount + 1);

        for (int i = 0; i < inputCount; i++) {

            final GraphConnectorSkin inputSkin = inputConnectorSkins.get(i);
            final Node connectorRoot = inputSkin.getRoot();

            final double layoutX = FXUtils.moveOnPixel(0 - inputSkin.getWidth() / 2);
            final double layoutY = FXUtils.moveOnPixel((i + 1) * inputOffsetY - inputSkin.getHeight() / 2);
            
            connectorRoot.setLayoutX(layoutX);
            connectorRoot.setLayoutY(layoutY + HEADER_HEIGHT);
        }

        final int outputCount = outputConnectorSkins.size();
        final double outputOffsetY = (getRoot().getHeight() - HEADER_HEIGHT) / (outputCount + 1);

        for (int i = 0; i < outputCount; i++) {

            final GraphConnectorSkin outputSkin = outputConnectorSkins.get(i);
            final Node connectorRoot = outputSkin.getRoot();

            final double layoutX = FXUtils.moveOnPixel(getRoot().getWidth() - outputSkin.getWidth() / 2);
            final double layoutY = FXUtils.moveOnPixel((i + 1) * outputOffsetY - outputSkin.getHeight() / 2);

            connectorRoot.setLayoutX(layoutX);
            connectorRoot.setLayoutY(layoutY + HEADER_HEIGHT);
        }
	}

    private void addSelectionHalo() {

        getRoot().getChildren().add(selectionHalo);

        selectionHalo.setManaged(false);
        selectionHalo.setMouseTransparent(false);
        selectionHalo.setVisible(false);

        selectionHalo.setLayoutX(-HALO_OFFSET);
        selectionHalo.setLayoutY(-HALO_OFFSET);

        selectionHalo.getStyleClass().add("titled-node-selection-halo");
    }
    
    private void layoutSelectionHalo() {

        if (selectionHalo.isVisible()) {

            selectionHalo.setWidth(getRoot().getWidth() + 2 * HALO_OFFSET);
            selectionHalo.setHeight(getRoot().getHeight() + 2 * HALO_OFFSET);

            final double cornerLength = 2 * HALO_CORNER_SIZE;
            final double xGap = getRoot().getWidth() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;
            final double yGap = getRoot().getHeight() - 2 * HALO_CORNER_SIZE + 2 * HALO_OFFSET;

            selectionHalo.setStrokeDashOffset(HALO_CORNER_SIZE);
            selectionHalo.getStrokeDashArray().setAll(cornerLength, yGap, cornerLength, xGap);
        }
    }

	@Override
	protected void selectionChanged(boolean selected) {
		if(isSelected()) {
			selectionHalo.setVisible(true);
			layoutSelectionHalo();
			contentRoot.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, true);
			getRoot().toFront();
		} else {
			selectionHalo.setVisible(false);
			contentRoot.pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, false);
		}
		setConnectorsSelected(isSelected());
	}

	@Override
	public void setConnectorSkins(List<GraphConnectorSkin> connectorSkins) {
		removeAllConnectors();
		
		inputConnectorSkins.clear();
		outputConnectorSkins.clear();
		
		if(connectorSkins != null) {
			for(final GraphConnectorSkin connectorSkin : connectorSkins) {
				
				final boolean isInput = connectorSkin.getElement().getType().contains("input");
				final boolean isOutput = connectorSkin.getElement().getType().contains("output");
				
				if(isInput) {
					inputConnectorSkins.add(connectorSkin);
				} else if(isOutput) {
					outputConnectorSkins.add(connectorSkin);
				}
				
				if(isInput || isOutput) {
					getRoot().getChildren().add(connectorSkin.getRoot());
				}
			}
		}
		
		setConnectorsSelected(isSelected());
	}

	@Override
	public Point2D getConnectorPosition(GraphConnectorSkin connectorSkin) {
        final Node connectorRoot = connectorSkin.getRoot();

        final double x = connectorRoot.getLayoutX() + connectorSkin.getWidth() / 2;
        final double y = connectorRoot.getLayoutY() + connectorSkin.getHeight() / 2;

        if (inputConnectorSkins.contains(connectorSkin)) {
            return new Point2D(x, y);
        } else {
            // Subtract 1 to align start-of-connection correctly. Compensation for rounding errors?
            return new Point2D(x - 1, y);
        }
	}
	
	private void setConnectorsSelected(boolean selected) {
		final GraphNodeEditor editor = getGraphEditor();
		if(editor == null) {
			return;
		}
		
		for(final GraphConnectorSkin skin : inputConnectorSkins) {
			if(skin instanceof TitledGraphConnectorSkin) {
				editor.select(skin.getElement());
			}
		}
		
		for(final GraphConnectorSkin skin : outputConnectorSkins) {
			if(skin instanceof TitledGraphConnectorSkin) {
				editor.select(skin.getElement());
			}
		}
	}
	
	/**
	 * Removes any input or output {@link GraphConnector} from the list of children, if they exist.
	 */
    private void removeAllConnectors() {

        for (final GraphConnectorSkin connectorSkin : inputConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }

        for (final GraphConnectorSkin connectorSkin : outputConnectorSkins) {
            getRoot().getChildren().remove(connectorSkin.getRoot());
        }
    }
}
