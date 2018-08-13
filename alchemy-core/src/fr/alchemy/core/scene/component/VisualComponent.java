package fr.alchemy.core.scene.component;

import java.io.IOException;
import java.io.OutputStream;

import fr.alchemy.core.annotation.CoreComponent;
import fr.alchemy.core.asset.Texture;
import fr.alchemy.core.asset.binary.BinaryReader;
import fr.alchemy.core.scene.SceneLayer;
import fr.alchemy.core.scene.entity.Entity;
import fr.alchemy.core.scene.entity.EntityView;
import fr.alchemy.utilities.ByteUtils;
import javafx.scene.Node;

/**
 * <code>VisualComponent</code> handles the {@link EntityView visual aspect} of an {@link Entity entity}.
 * It can be used to add visual nodes for the entity.
 * 
 * @author GnosticOccultist
 */
@CoreComponent
public final class VisualComponent extends Component {
	/**
	 * The view bounded to the entity.
	 */
	private final EntityView view = new EntityView();
	/**
	 * The scene layer in which the view will be rendered on.
	 */
	private SceneLayer sceneLayer = SceneLayer.DEFAULT;
	
	/**
	 * Instantiates a new empty <code>VisualComponent</code>.
	 */
	public VisualComponent() {}
	
	/**
	 * Instantiates a new <code>VisualComponent</code> with the specified
	 * graphic node for the <code>EntityView</code>.
	 * 
	 * @param graphic The graphic node to add.
	 */
	public VisualComponent(final Node graphic) {
		this(graphic, SceneLayer.DEFAULT);
	}
	
	/**
	 * Instantiates a new <code>VisualComponent</code> with the specified
	 * <code>SceneLayer</code> for the <code>EntityView</code>.
	 * 
	 * @param layer The layer for the view.
	 */
	public VisualComponent(final SceneLayer layer) {
		this(null, layer);
	}
	
	/**
	 * Instantiates a new <code>VisualComponent</code> with the specified
	 * graphic node for the <code>EntityView</code> and the <code>SceneLayer</code>.
	 * 
	 * @param graphic The graphic node to add.
	 * @param layer   The layer for the view.
	 */
	public VisualComponent(final Node graphic, final SceneLayer layer) {
		getView().addNode(graphic);
		setSceneLayer(sceneLayer);
	}
	
	@Override
	public void enable() {
		show();
	}
	
	@Override
	public void disable() {
		hide();
	}
	
	/**
	 * Shows the <code>EntityView</code> and its graphic nodes.
	 */
	public void show() {
		view.setVisible(true);
	}
	
	/**
	 * Hides the <code>EntityView</code> and its graphic nodes.
	 */
	public void hide() {
		view.setVisible(false);
	}
	
	/**
	 * @return The scene layer in which the view will be rendered on.
	 */
	public SceneLayer getSceneLayer() {
		return sceneLayer;
	}
	
	/**
	 * Sets the scene layer in which the view will be rendered on.
	 * 
	 * @param sceneLayer The scene layer.
	 */
	public void setSceneLayer(final SceneLayer sceneLayer) {
		if(sceneLayer == null) {
			this.sceneLayer = SceneLayer.DEFAULT;
			return;
		}
		
		this.sceneLayer = sceneLayer;
	}
	
	/**
	 * @return The <code>EntityView</code>.
	 */
	public EntityView getView() {
		return view;
	}
	
	public void setView(final EntityView view) {
		if(view == null) {
			return;
		}
		
		view.getNodes().setAll(view.getNodes());
	}
	
	/**
	 * Sets the opacity to all the graphic nodes of the <code>VisualComponent</code>.
	 * 
	 * @param opacity The opacity value.
	 */
	public void setOpacity(final double opacity) {
		view.getNodes().forEach(node -> node.setOpacity(opacity));
	}
	
	/**
	 * Apply a grayscale effect to all the <code>Texture</code> graphic node
	 * of the <code>VisualComponent</code>.
	 */
	public void grayscale() {
		view.getNodes().stream().filter(Texture.class::isInstance)
			.map(Texture.class::cast).forEach(Texture::grayscale);
	}
	
	/**
	 * Apply a saturation effect to all the <code>Texture</code> graphic node
	 * of the <code>VisualComponent</code>.
	 */
	public void saturate() {
		view.getNodes().stream().filter(Texture.class::isInstance)
			.map(Texture.class::cast).forEach(Texture::saturate);
	}
	
	/**
	 * Apply a desaturation effect to all the <code>Texture</code> graphic node
	 * of the <code>VisualComponent</code>.
	 */
	public void desaturate() {
		view.getNodes().stream().filter(Texture.class::isInstance)
			.map(Texture.class::cast).forEach(Texture::desaturate);
	}
	
	/**
	 * Apply a brighten effect to all the <code>Texture</code> graphic node
	 * of the <code>VisualComponent</code>.
	 */
	public void brighter() {
		view.getNodes().stream().filter(Texture.class::isInstance)
			.map(Texture.class::cast).forEach(Texture::brighter);
	}
	
	/**
	 * Apply a darken effect to all the <code>Texture</code> graphic node
	 * of the <code>VisualComponent</code>.
	 */
	public void darker() {
		view.getNodes().stream().filter(Texture.class::isInstance)
			.map(Texture.class::cast).forEach(Texture::darker);
	}
	
	@Override
	public void onAttached(final Entity entity) {
		super.onAttached(entity);
		
		bindView();
	}
	
	@Override
	public void onDetached(final Entity entity) {
		unbindView();
		
		super.onDetached(entity);
	}
	
	@Override
	public void cleanup() {
		unbindView();
		getView().clear();
		
		super.cleanup();
	}
	
	public void refresh() {
		bindView();
	}
	
	/**
	 * Binds the <code>EntityView</code> to the <code>Transform</code> component
	 * of the <code>Entity</code>.
	 */
	private void bindView() {
		final Transform transform = getOwner().getComponent(Transform.class);
		
		getView().translateXProperty().bind(transform.posXProperty());
		getView().translateYProperty().bind(transform.posYProperty());
		
		getView().rotateProperty().bind(transform.rotationProperty());
		
		getView().scaleXProperty().bind(transform.scaleXProperty());
		getView().scaleYProperty().bind(transform.scaleYProperty());
	}
	
	/**
	 * Unbinds the <code>EntityView</code> from the <code>Transform</code> component
	 * of the <code>Entity</code>.
	 */
	private void unbindView() {
		getView().translateXProperty().unbind();
		getView().translateYProperty().unbind();
		
		getView().rotateProperty().unbind();
		
		getView().scaleXProperty().unbind();
		getView().scaleYProperty().unbind();
	}
	
	public void set(final VisualComponent other) {
		view.getNodes().setAll(other.view.getNodes());
		sceneLayer = new SceneLayer(other.sceneLayer.name(), other.sceneLayer.index());
	}
	
	@Override
	public String toString() {
		return "Visual: " + view + " Scene Layer: " + sceneLayer;
	}
	
	@Override
	public void export(final OutputStream os) throws IOException {
		super.export(os);
		
		os.write(ByteUtils.toBytes(getClass().getName().length()));
		os.write(ByteUtils.toBytes(getClass().getName()));
		
		sceneLayer.export(os);
		view.export(os);
	}
	
	@Override
	public void insert(final BinaryReader reader) throws IOException {
		super.insert(reader);
		
		setSceneLayer(reader.readExportable(SceneLayer.class));
		setView(reader.readExportable(EntityView.class));
		
		// TODO: Store the constant scene layer if needed.
	}
}
