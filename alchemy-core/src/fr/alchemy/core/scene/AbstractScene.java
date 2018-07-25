package fr.alchemy.core.scene;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.alchemy.core.AlchemyApplication;
import fr.alchemy.core.AlchemySettings;
import fr.alchemy.core.scene.entity.Entity;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;

/**
 * <code>AbstractScene</code> is an abstraction layer of the actual {@link AlchemyScene}, which can
 * be used to implement your own type of scene for your applications.
 * <p>
 * Therefore it contains the useful structure like setting up the size of the scene, initializing the viewport
 * and the two root layers of the scene: {@link #root} & {@link #contentRoot}.
 * 
 * @author GnosticOccultist
 */
public abstract class AbstractScene {
	
	/**
	 * The scene logger.
	 */
	private Logger logger = LoggerFactory.getLogger("alchemy.scene");
	/**
	 * The application.
	 */
	protected AlchemyApplication application;
	/**
	 * The list of entities in the scene graph.
	 */
	protected List<Entity> entities = new ArrayList<Entity>();
	/**
	 * The viewport of the scene graph.
	 */
	protected final Viewport viewport = new Viewport();
	/**
	 * The root of the {@link #mainScene}.
	 * Contains {@link #contentRoot} and is supposed to be leaved untouched.
	 */
	protected Pane root = new Pane();
	/**
	 * The content root contains all the content of the scene.
	 * In {@link AlchemyScene} it's {@link AlchemyScene#sceneRoot} and {@link AlchemyScene#uiRoot}
	 */
	protected Pane contentRoot = new Pane();
	/**
	 * The application scene.
	 */
	protected Scene mainScene = new Scene(root);
	/**
	 * Equals user system width / target width 
	 */
	private double sizeRatio = 1.0;
	
	public AbstractScene(final AlchemyApplication application) {
		this.application = application;
	}
	
	public void initialize(final double width, final double height) {
		setPrefSize(width, height);
		
		initializeViewport();
	
		getRoot().getChildren().add(contentRoot);
		getRoot().requestFocus();
		
		if (needShowFPS()) {
	        final Text fpsText = new Text();
	        fpsText.setFill(Color.WHITE);
	        fpsText.setFont(Font.font(24));
	        fpsText.setTranslateY(height - 40);
	        fpsText.textProperty().bind(application.getTimer().fpsProperty().asString("FPS: [%d]\n")
	                .concat(application.getTimer().performanceFPSProperty().asString("Performance: [%d]")));
	        addUINode(fpsText);
		}
	}
	
	public void initializeViewport() {
		getSceneRoot().layoutXProperty().bind(viewport.xProperty().negate()); 
		getSceneRoot().layoutYProperty().bind(viewport.yProperty().negate()); 
		
		Scale scale = new Scale();
		scale.pivotXProperty().bind(viewport.xProperty());
		scale.pivotYProperty().bind(viewport.yProperty());
		scale.xProperty().bind(viewport.zoomProperty());
		scale.yProperty().bind(viewport.zoomProperty());
		getSceneRoot().getTransforms().add(scale);
	}
	
	/**
	 * Sets the preferred size for the <code>AlchemyScene</code>.
	 * 
	 * @param width  The width.
	 * @param height The height.
	 */
	public void setPrefSize(final double width, final double height) {
		final AlchemySettings settings = AlchemySettings.settings();
		
		final Rectangle2D bounds = settings.boolValue("Fullscreen") ? 
				Screen.getPrimary().getBounds() : 
				Screen.getPrimary().getVisualBounds();
		
		if(settings.getWidth() <= bounds.getWidth() && settings.getHeight() <= bounds.getHeight()) {
			root.setPrefSize(settings.getWidth(), settings.getHeight());
		} else {
			double ratio = settings.getWidth() * 1.0 / settings.getHeight();
			
			for(int newWidth = (int) bounds.getWidth(); newWidth > 0; newWidth--) {
				if(newWidth / ratio <= bounds.getHeight()) {
					root.setPrefSize(newWidth, (int) (newWidth / ratio));
					
					double newSizeRatio = newWidth * 1.0 / settings.getWidth();
					root.getTransforms().add(new Scale(newSizeRatio, newSizeRatio));
					sizeRatio = newSizeRatio;
					break;
				}
			}
		}
	}
	
	/**
	 * Saves a screenshot of the <code>AlchemyApplication</code> into a '.png' file.
	 * 
	 * @return Whether the screenshot has been correctly saved.
	 */
	public boolean screenshot() {
		AlchemySettings settings = AlchemySettings.settings();
		
		Image image = mainScene.snapshot(null);
		BufferedImage img = SwingFXUtils.fromFXImage(image, null);
		
		String fileName = "./" + settings.getTitle() + settings.getVersion()
				+ LocalDateTime.now() + ".png";
		fileName = fileName.replace(':', '_');
		
		try (OutputStream os = Files.newOutputStream(Paths.get(fileName))) {
			return ImageIO.write(img, "png", os);
		} catch (Exception e) {
			logger().error("Exception occured during screenshot - " + e.getMessage());
		}
		
		return false;
	}
	
	/**
	 * Sets the cursor image for the scene.
	 * 
	 * @param name	  The image file.
	 * @param hotSpot The location where the pointer ends on the image.
	 */
	public void setCursor(final String name, final Point2D hotSpot) {
		getFXScene().setCursor(new ImageCursor(application.getAssetManager().loadImage(name),
				hotSpot.getX(), hotSpot.getY()));
	}
	
	/**
	 * Adds an <code>Entity</code> to the scene. It only adds the <code>Entity</code> to the list,
	 * so you <strong>NEED TO BE OVERRIDEN</strong>
	 * 
	 * @param entity The entity.
	 */
	protected void addEntity(final Entity entity) {
		logger().info("Added " + entity.name() + "-(Entity) to the " + getClass().getSimpleName());
		this.entities.add(entity);
	}
	
	/**
	 * Adds a UI node to the scene.
	 * <strong>NEED TO BE OVERRIDEN</strong>
	 * 
	 * @param uiNode The UI node.
	 */
	protected void addUINode(final Node uiNode) {
		logger().info("Added " + uiNode.getClass().getSimpleName() + "-(UI) to the " + getClass().getSimpleName());
	}
	
	/**
	 * @return The root containing all the visible elements of the scene,
	 * 		   mainly {@link Entity entities}.
	 */
	protected abstract Parent getSceneRoot();
	
	/**
	 * @return The root containing all the UI elements of the scene.
	 */
	protected abstract Parent getUIRoot();
	
	/**
	 * @return Whether the scene needs to show the FPS.
	 */
	protected boolean needShowFPS() {
		final AlchemySettings settings = AlchemySettings.settings();
		return settings.boolValue("ShowFPS");
	}
	
	/**
	 * @return The root layer of the scene.
	 */
	protected final Pane getRoot() {
		return root;
	}
	
	/**
	 * @return The content root layer of the scene.
	 */
	protected final Pane getContentRoot() {
		return contentRoot;
	}
	
	/**
	 * @return The <code>AlchemyApplication</code>.
	 */
	protected final AlchemyApplication getApplication() {
		return application;
	}
	
	/**
	 * @return The viewport of the <code>AchemyScene</code>.
	 */
	public final Viewport getViewport() {
		return viewport;
	}
	
	/**
	 * @return The JavaFX scene.
	 */
	public final Scene getFXScene() {
		return mainScene;
	}
	
	/**
	 * @return The size ratio of the scene.
	 */
	public final double getSizeRatio() {
		return sizeRatio;
	}
	
	/**
	 * @return The scene logger.
	 */
	protected Logger logger() {
		return logger;
	}
}
