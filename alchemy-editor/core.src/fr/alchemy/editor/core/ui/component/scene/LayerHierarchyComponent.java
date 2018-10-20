package fr.alchemy.editor.core.ui.component.scene;

import fr.alchemy.core.scene.AlchemyScene;
import fr.alchemy.core.scene.SceneLayer;
import fr.alchemy.editor.api.AbstractListCell;
import fr.alchemy.editor.api.editor.EditorComponent;
import fr.alchemy.editor.core.ui.editor.scene.AlchemyEditorScene;
import fr.alchemy.utilities.Validator;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * <code>LayerHierarchyComponent</code> is an implementation of {@link EditorComponent}
 * which is used to display the available {@link SceneLayer} of an {@link AlchemyScene}.
 * <p>
 * It also allows you to discard or lower the opacity of a specific layer in order to let
 * you focus on the more important ones.
 * 
 * @author GnosticOccultist
 */
public class LayerHierarchyComponent extends VBox implements EditorComponent {
	
	/**
	 * The visual list of scene layers.
	 */
	private ListView<SceneLayer> layersView;
	/**
	 * The scene bounded to the hierarchy.
	 */
	private final AlchemyEditorScene scene;
	
	/**
	 * Instantiates a new <code>LayerHierarchyComponent</code>.
	 */
	public LayerHierarchyComponent(AlchemyEditorScene scene) {
		this.scene = scene;
		createComponents();
	}
	
	/**
	 * Creates the components for the <code>LayerHierarchyComponent</code>.
	 */
	protected void createComponents() {
		
		layersView = new ListView<>();
		layersView.setCellFactory(param -> new LayerListCell(this));
		layersView.setEditable(false);
		layersView.setFocusTraversable(true);
		layersView.prefHeightProperty().bind(heightProperty());
		layersView.prefWidthProperty().bind(widthProperty());
		layersView.setFixedCellSize(26);
		
		getChildren().add(layersView);
	}
	
	/**
	 * Fills the <code>LayerHierarchyComponent</code> with the {@link SceneLayer} contained
	 * in the <code>AlchemyEditorScene</code>.
	 */
	public LayerHierarchyComponent fillWithScene() {
		Validator.nonNull(scene);
		
		layersView.getItems().clear();
		scene.getRenderLayers().forEach(this::fill);
		
		return this;
	}
	
	/**
	 * Fills the <code>LayerHierarchyComponent</code> with the {@link SceneLayer} contained
	 * in the <code>AlchemyEditorScene</code>.
	 * 
	 * @param scene The scene which contains layers to fill.
	 */
	public LayerHierarchyComponent fill(AlchemyEditorScene scene) {
		Validator.nonNull(scene);
		
		layersView.getItems().clear();
		scene.getRenderLayers().forEach(this::fill);
		
		return this;
	}
	
	/**
	 * Fills the <code>LayerHierarchyComponent</code> with the specified {@link SceneLayer}.
	 * 
	 * @param layer The layer to fill.
	 */
	public LayerHierarchyComponent fill(SceneLayer layer) {
		layersView.getItems().add(layer);
		return this;
	}
	
	/**
	 * Return the name used by the component.
	 * 
	 * @return The name of the component.
	 */
	@Override
	public String getName() {
		return "Layers";
	}
	
	/**
	 * Return the editor scene.
	 * 
	 * @return The editor scene.
	 */
	protected AlchemyEditorScene getEditorScene() {
		return scene;
	}
	
	public class LayerListCell extends AbstractListCell<SceneLayer> {
		
		private LayerHierarchyComponent component;
		
		public LayerListCell(LayerHierarchyComponent component) {
			this.component = component;
		}
		
		@Override
		protected double getOpacity(SceneLayer item) {
			return item.visibility();
		}
		
		@Override
		protected void hide(MouseEvent event) {
			SceneLayer layer = getItem();
			
			if(event.getButton().equals(MouseButton.PRIMARY)) {
				changeTransparency(layer);
			}
			
			if(event.getButton().equals(MouseButton.SECONDARY)) {
				hide(layer);
			}
			
			visibilityIcon.setOpacity(layer.visibility());
			component.getEditorScene().layer(layer).setOpacity(layer.visibility());
		}
		
		private void changeTransparency(SceneLayer layer) {
			if(layer.visibility() >= 1.0D || layer.visibility() <= 0.0D) {
				layer.visibilityProperty().set(0.3D);
			} else if(layer.visibility() == 0.3D) {
				layer.visibilityProperty().set(1.0D);
			}
		}

		private void hide(SceneLayer layer) {
			if(layer.visibility() <= 0.0D) {
				layer.visibilityProperty().set(1.0D);
			} else if(layer.visibility() > 0.0D) {
				layer.visibilityProperty().set(0.0D);
			}
		}
		
		@Override
		protected String getName(SceneLayer item) {
			return item.name();
		}
	};
}
