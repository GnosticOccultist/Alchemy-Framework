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
import fr.alchemy.core.event.AlchemyEventManager;
import fr.alchemy.core.event.AlchemySceneEvent;
import fr.alchemy.core.scene.component.VisualComponent;
import fr.alchemy.core.scene.entity.Entity;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;

/**
 * <code>Scene</code> represents a basic scene in the <code>AlchemyApplication</code>.
 * It can be used to add/remove/update {@link Entity entities} or UI components.
 * 
 * @author Stickxy
 */
public class AlchemyScene {
	
	/**
	 * The scene logger.
	 */
	private Logger logger = LoggerFactory.getLogger("alchemy.scene");
	/**
	 * The application.
	 */
	private AlchemyApplication application;
	/**
	 * The root for all the entities.
	 */
	protected Group appRoot = new Group(); 
	/**
	 * The overlay root above {@link #appRoot}. Contains all the UI elements, native JavaFX nodes.
	 * May also contain some entities. The UI root isn't affected by viewport movement.
	 */
	protected Group uiRoot = new Group();
	/**
	 * The sub-root of the {@link #mainScene}. 
	 * Contains {@link #appRoot} and {@link #uiRoot} in this order,
	 * {@link #root} as its parent.
	 */
	protected Region subRoot = new Pane(new Pane(appRoot, uiRoot));
	/**
	 * The root of the {@link #mainScene}.
	 * Contains {@link #subRoot} and is supposed to be leaved untouched.
	 */
	protected Pane root = new Pane(subRoot);
	/**
	 * The application scene.
	 */
	protected Scene mainScene = new Scene(root);
	/**
	 * Equals user system width / target width 
	 */
	private double sizeRatio = 1.0;
	/**
	 * The list of entities in the scene graph.
	 */
	public List<Entity> entities = new ArrayList<Entity>();
	/**
	 * The viewport of the scene graph.
	 */
	private final Viewport viewport = new Viewport();
	
	public AlchemyScene(final AlchemyApplication application) {
		this.application = application;
	}
	
	public void initialize(final double width, final double height) {
		setPrefSize(width, height);
		setBackgroundColor(Color.BLACK);
		mainScene.setRoot(root);
	
		getSubRoot().prefWidthProperty().bind(root.widthProperty());
		getSubRoot().prefHeightProperty().bind(root.heightProperty());
		
		root.requestFocus();
		
		appRoot.layoutXProperty().bind(viewport.xProperty().negate()); 
		appRoot.layoutYProperty().bind(viewport.yProperty().negate()); 
		
		Scale scale = new Scale();
		scale.pivotXProperty().bind(viewport.xProperty());
		scale.pivotYProperty().bind(viewport.yProperty());
		scale.xProperty().bind(viewport.zoomProperty());
		scale.yProperty().bind(viewport.zoomProperty());
		appRoot.getTransforms().add(scale);
		
		final AlchemySettings settings = AlchemySettings.settings();
		if(settings.boolValue("ShowFPS")) {
	        final Text fpsText = new Text();
	        fpsText.setFill(Color.AZURE);
	        fpsText.setFont(Font.font(24));
	        fpsText.setTranslateY(settings.getHeight() - 40);
	        fpsText.textProperty().bind(application.getTimer().fpsProperty().asString("FPS: [%d]\n")
	                .concat(application.getTimer().performanceFPSProperty().asString("Performance: [%d]")));
	        uiRoot.getChildren().add(fpsText);
		}
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
	 * Adds the specified <code>Entity</code> to the <code>AlchemyScene</code>
	 * and attach its <code>VisualComponent</code>.
	 * 
	 * @param entity The entity to add.
	 */
	public void add(final Entity entity) {
		this.entities.add(entity);
		this.appRoot.getChildren().add(entity.getComponent(VisualComponent.class).getView());
		
		AlchemyEventManager.events().notify(AlchemySceneEvent.entityAdded(entity));
	}
	
	/**
	 * Removes the specified <code>Entity</code> from the <code>AlchemyScene</code>
	 * and detach its <code>VisualComponent</code>.
	 * 
	 * @param entity The entity to remove.
	 */
	public void remove(final Entity entity) {
		this.entities.remove(entity);
		this.appRoot.getChildren().remove(entity.getComponent(VisualComponent.class).getView());
		
		AlchemyEventManager.events().notify(AlchemySceneEvent.entityRemoved(entity));
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
	 * Converts a point on the screen into a point in the <code>AlchemyScene</code>.
	 * 
	 * @param screenPoint The point in UI coordinates.
	 * @return			  The point in scene coordinates.
	 */
	public Point2D screenToGame(final Point2D screenPoint) {
        return screenPoint.multiply(1.0 / getSizeRatio()).add(viewport.getOrigin());
    }
	
	/**
	 * Sets the background color to the specified color.
	 * 
	 * @param color The background color.
	 */
	public void setBackgroundColor(final Color color) {
		mainScene.setFill(color);
		getSubRoot().setBackground(new Background(new BackgroundFill(color, null, null)));
	}
	
	/**
	 * Updates the scene-graph by updating each entities.
	 * 
	 * @param now The current time.
	 */
	public void update(final long now) {
		entities.forEach(entity -> entity.update(now));
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
	 * @return The <code>AlchemyApplication</code>.
	 */
	protected AlchemyApplication getApplication() {
		return application;
	}
	
	/**
	 * @return The sub-root pane of the <code>AlchemyScene</code>.
	 */
	protected Region getSubRoot() {
		return subRoot;
	}
	
	/**
	 * @return The size ratio of the screen resolution
	 * 		   over the target resolution.
	 */
	public final double getSizeRatio() {
		return sizeRatio;
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
	 * @return The scene logger.
	 */
	private Logger logger() {
		return logger;
	}
}
