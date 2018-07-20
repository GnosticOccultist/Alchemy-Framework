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
import fr.alchemy.core.scene.component.Transform;
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
	private Group appRoot = new Group(); 
	/**
	 * The overlay root above {@link #appRoot}. Contains all the UI elements, native JavaFX nodes.
	 * May also contain some entities. The UI root isn't affected by viewport movement.
	 */
	private Group uiRoot = new Group();
	/**
	 * The root of the {@link #mainScene}. 
	 * Contains {@link #appRoot} and {@link #uiRoot} in this order.
	 */
	private Pane mainRoot = new Pane(appRoot, uiRoot);
	/**
	 * The application scene.
	 */
	protected Scene mainScene = new Scene(mainRoot);
	/**
	 * Equals user system width / target width 
	 */
	private double sizeRatio = 1.0;
	/**
	 * The list of entities in the scene graph.
	 */
	public List<Entity> entities = new ArrayList<>();
	
	public void initialize(final AlchemyApplication application, final double width, final double height) {
		this.application = application;
		
		setPrefSize(width, height);
		setBackgroundColor(Color.BLACK);
		mainScene.setRoot(mainRoot);
		
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
			mainRoot.setPrefSize(settings.getWidth(), settings.getHeight());
		} else {
			double ratio = settings.getWidth() * 1.0 / settings.getHeight();
			
			for(int newWidth = (int) bounds.getWidth(); newWidth > 0; newWidth--) {
				if(newWidth / ratio <= bounds.getHeight()) {
					mainRoot.setPrefSize(newWidth, (int) (newWidth / ratio));
					
					double newSizeRatio = newWidth * 1.0 / settings.getWidth();
					mainRoot.getTransforms().add(new Scale(newSizeRatio, newSizeRatio));
					sizeRatio = newSizeRatio;
					break;
				}
			}
		}
	}
	
	public void addEntity(final Entity entity) {
		this.entities.add(entity);
		this.appRoot.getChildren().add(entity.getComponent(VisualComponent.class).getView());
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
        return screenPoint.multiply(1.0 / getSizeRatio()).add(getViewportOrigin());
    }
	
	/**
	 * Sets the background color to the specified color.
	 * 
	 * @param color The background color.
	 */
	public void setBackgroundColor(final Color color) {
		mainScene.setFill(color);
		mainRoot.setBackground(new Background(new BackgroundFill(color, null, null)));
	}
	
	/**
	 * Binds the viewport origin to the provided <code>Entity</code> position. 
	 * A delta distance can be set between the origin and the entity position with
	 * the provided x and y values.
	 * 
	 * @param entity The entity to bind the viewport to.
	 * @param x		 The delta-X value.
	 * @param y		 The delta-Y value.
	 */
	public void bindViewportOrigin(final Entity entity, final int x, final int y) {
		appRoot.layoutXProperty().bind(entity.getComponent(Transform.class).posXProperty().negate().add(x));
		appRoot.layoutYProperty().bind(entity.getComponent(Transform.class).posYProperty().negate().add(y));
	}
	
	/**
	 * Unbinds the viewport origin from its previous property.
	 */
	public void unbindViewportOrigin() {
		appRoot.layoutXProperty().unbind();
		appRoot.layoutYProperty().unbind();
	}
	
	/**
	 * Sets the viewport origin to the provided X and Y coordinate.
	 * It can be used to mimic camera movement.
	 * 
	 * @param x The X coordinate.
	 * @param y The Y coordinate.
	 */
	public void setViewportOrigin(final int x, final int y) {
		appRoot.setLayoutX(-x);
		appRoot.setLayoutY(-y);
	}
	
	/**
	 * @return The viewport origin in the top-left corner.
	 */
	public final Point2D getViewportOrigin() {
		return new Point2D(-appRoot.getLayoutX(), -appRoot.getLayoutY());
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
	 * @return The size ratio of the screen resolution
	 * 		   over the target resolution.
	 */
	public final double getSizeRatio() {
		return sizeRatio;
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
