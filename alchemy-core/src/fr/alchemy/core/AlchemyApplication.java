package fr.alchemy.core;

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

import fr.alchemy.core.annotation.FXThread;
import fr.alchemy.core.entity.Entity;
import fr.alchemy.core.input.InputManager;
import fr.alchemy.core.listener.ExitListener;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * <code>AlchemyApplication</code> is the class to extend to create your own application.
 * You also need to implement all the necessary methods.
 * <p>
 * To actually launch the application, you need to call {@link #launch(String...)} to start
 * the FX thread inside a main method.
 *
 * @author GnosticOccultist
 */
public abstract class AlchemyApplication extends Application {
	
	/**
	 * The logger.
	 */
	private Logger logger = LoggerFactory.getLogger("alchemy.app");
	/**
	 * A second in nanoseconds.
	 */
	public static final long SECOND = 1_000_000_000;
	/**
	 * The timer per single frame in nanoseconds.
	 */
	public static final long TIME_PER_FRAME = SECOND / 60;
	/**
	 * The root for all the entities.
	 */
	private Pane appRoot = new Pane(); 
	/**
	 * The overlay root above {@link #appRoot}. Contains all the UI elements, native JavaFX nodes.
	 * May also contain some entities. The UI root isn't affected by viewport movement.
	 */
	private Pane uiRoot = new Pane();
	/**
	 * The root of the {@link #mainScene}. 
	 * Contains {@link #appRoot} and {@link #uiRoot} in this order.
	 */
	private Pane mainRoot = new Pane(appRoot, uiRoot);
	/**
	 * The application window.
	 */
	private Window window;
	/**
	 * The application scene.
	 */
	protected Scene mainScene = new Scene(mainRoot);
	/**
	 * The main loop timer.
	 */
	private AnimationTimer timer = new AnimationTimer() {
		
		@Override
		public void handle(long internalTime) {
			try {
				internalUpdate(internalTime);
			} catch (Throwable t) {
				logger().error("A fatal error has occured !", t);
				exit();
			}
		}
	};
	/**
	 * The input manager.
	 */
	protected final InputManager inputManager = new InputManager(this);
	/**
	 * FPS counter to approximate FPS values.
	 */
	private FPSCounter fpsCounter = new FPSCounter();
	private FPSCounter fpsPerformanceCounter = new FPSCounter();
	/**
	 * Average render FPS.
	 */
	protected int fps = 0;
	/**
	 * Average performance FPS.
	 */
	protected int fpsPerformance = 0;
	/**
	 * Used as delta from internal JavaFX time-stamp to calculate renderFPS.
	 */
	protected long fpsTime = 0; 
	/**
	 * The current time this tick in nanoseconds. Also time elapsed
	 * from the start of the app. This time doesn't change while the app is paused.
	 */
	protected long currentTime;
	
	private List<ExitListener> listeners = new ArrayList<>();

	@Override
	@FXThread
	public void start(Stage primaryStage) throws Exception {
		logger().info("Starting " + getClass().getSimpleName());
		
		final AlchemySettings settings = AlchemySettings.settings();
		
		initializeSettings(settings);
		
		this.mainRoot.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		this.mainRoot.setPrefSize(settings.getWidth(), settings.getHeight());
		
		this.window = new Window(this);
		this.window.initialize(primaryStage, mainScene);
		
		this.inputManager.initialize(mainScene);
		
		initialize(appRoot, uiRoot);
		
		postInitialize();
		
		window.show();
		timer.start();
	}
	
	/**
	 * This method is the internal <code>AlchemyApplication</code> update tick.
	 * It's executed 60 times a second ~ every 0.166 (6) seconds.
	 * 
	 * @param internalTime The time-stamp of the current frame given in nanoseconds (from JavaFX).
	 */
	@FXThread
	private void internalUpdate(final long internalTime) {
		long startNanos = System.nanoTime();
		long realFPS = internalTime - fpsTime;
		fpsTime = internalTime;
		
		inputManager.update(currentTime);
		
		update();
		
		appRoot.getChildren().stream().map(node -> (Entity) node).forEach(entity -> entity.update(currentTime));
		
		fpsPerformance = Math.round(fpsPerformanceCounter.count(SECOND / (System.nanoTime() - startNanos)));
		fps = Math.round(fpsCounter.count(SECOND / realFPS));
		
		currentTime += TIME_PER_FRAME;
	}
	
	/**
	 * @return The viewport origin in the top-left corner.
	 */
	public Point2D getViewportOrigin() {
		return new Point2D(-appRoot.getLayoutX(), -appRoot.getLayoutY());
	}
	
	/**
	 * Pauses the <code>AlchemyApplication</code>.
	 */
	protected final void pause() {
		timer.stop();
	}
	
	/**
	 * Resumes the <code>AlchemyApplication</code>.
	 */
	protected final void resume() {
		timer.start();
	}
	
	/**
	 * Called automatically when the close is requested. If you want to add an exiting task, use
	 * {@link AlchemyApplication#registerListener(ExitListener)} to register an {@link ExitListener}.
	 * <p>
	 * You can however call the method to manually quit the application.
	 */
	protected final void exit() {
		logger().info("Closing " + getClass().getSimpleName());
		
		listeners.forEach(ExitListener::exit);
		Platform.exit();
	}
	
	/**
	 * Registers the provided listener for the <code>AlchemyApplication</code>.
	 * 
	 * @param listener The listener to register.
	 */
	public void registerListener(final ExitListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Saves a screenshot of the <code>AlchemyApplication</code> into a '.png' file.
	 * 
	 * @return Whether the screenshot has been correctly saved.
	 */
	protected boolean screenshot() {
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
	 * Initializes the <code>AlchemySettings</code> before starting the <code>Application</code>.
	 * It can be used to change the default settings.
	 * 
	 * @param settings The settings of the application.
	 */
	protected abstract void initializeSettings(AlchemySettings settings);
	
	/**
	 * Initializes the <code>AlchemyApplication</code> by setting up the UI and the application pane. 
	 * 
	 * @param appRoot The application pane.
	 * @param uiRoot  The UI pane.
	 */
	protected abstract void initialize(final Pane appRoot, final Pane uiRoot);
	
	/**
	 * Called <strong>AFTER</strong> all initialize methods complete and <strong>BEFORE</strong>
	 * the main loop starts.
	 */
	protected void postInitialize() {}
	
	/**
	 * Updates the <code>AlchemyApplication</code>.
	 */
	protected abstract void update();
	
	/**
	 * @return The logger of the <code>AlchemyApplication</code>.
	 */
	protected Logger logger() {
		return logger;
	}
}
