package fr.alchemy.editor.core.ui.editor.graph.skin;

import fr.alchemy.editor.api.editor.graph.element.GraphNode;
import fr.alchemy.editor.api.editor.graph.skin.GraphNodeSkin;
import javafx.geometry.Pos;
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
   
    private static final double MIN_WIDTH = 81;
    private static final double MIN_HEIGHT = 81;

    private static final int BORDER_WIDTH = 1;
	private final Rectangle border = new Rectangle();
	
	public TitledGraphNodeSkin(final GraphNode element) {
		super(element);
		
		
		border.getStyleClass().setAll("titled-node-border");
        border.widthProperty().bind(getRoot().widthProperty());
        border.heightProperty().bind(getRoot().heightProperty());
        
        getRoot().getChildren().add(border);
        getRoot().setMinSize(MIN_WIDTH, MIN_HEIGHT);
        
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
        contentRoot.getChildren().add(header);
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
		// TODO Auto-generated method stub

	}

	@Override
	protected void selectionChanged(boolean selected) {
		// TODO Auto-generated method stub

	}
}
