package fr.alchemy.core;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.alchemy.core.annotation.FXThread;
import fr.alchemy.core.asset.AssetManager;
import fr.alchemy.core.input.InputManager;
import fr.alchemy.core.listener.ApplicationListener;
import fr.alchemy.core.scene.AlchemyScene;
import fr.alchemy.core.util.NanoTimer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
	 * The application logger.
	 */
	private Logger logger = LoggerFactory.getLogger("alchemy.app");
	/**
	 * The main loop timer.
	 */
	private AnimationTimer loop = new AnimationTimer() {
		
		@Override
		public void handle(long internalTime) {
			try {
				if(!hasStarted()) {
					return;
				}
				
				internalUpdate(internalTime);
			} catch (Throwable t) {
				logger().error("A fatal error has occured !", t);
				exit();
			}
		}
	};
	/**
	 * The application scene.
	 */
	protected AlchemyScene scene = new AlchemyScene(this); 
	/**
	 * The application window.
	 */
	private final Window window = new Window(this);
	/**
	 * The input manager.
	 */
	protected final InputManager inputManager = new InputManager(this);
	/**
	 * The asset manager.
	 */
	protected final AssetManager assetManager = new AssetManager();
	/**
	 * The timer in nanoseconds.
	 */
	private final NanoTimer timer = new NanoTimer();
	/**
	 * The application listeners.
	 */
	private List<ApplicationListener> listeners = new ArrayList<>();
	/**
	 * Whether the application has been correctly started.
	 */
	private BooleanProperty started = new SimpleBooleanProperty(false);
	
	@Override
	@FXThread
	public final void start(Stage primaryStage) throws Exception {
		logger().info("Starting " + getClass().getSimpleName());
		
		final AlchemySettings settings = AlchemySettings.settings();
		
		initializeSettings(settings);
		
		preInitialize();
		
		getScene().initialize(settings.getWidth(), settings.getHeight());
		getWindow().initialize(primaryStage, scene.getFXScene());
		getInputManager().initialize(scene.getFXScene());
		
		initialize();
		
		postInitialize();
		
		window.show();
		
		started.set(true);
		loop.start();
	}
	
	/**
	 * This method is the internal <code>AlchemyApplication</code> update tick.
	 * It's executed 60 times a second ~ every 0.166 (6) seconds.
	 * 
	 * @param internalTime The time-stamp of the current frame given in nanoseconds (from JavaFX).
	 */
	@FXThread
	private void internalUpdate(final long internalTime) {
		// Setup the current tick and the current time.
		timer.tickStart(internalTime);
		
		// Updates the input event processing.
		inputManager.update(timer.getNow());
		
		// Updates the entities in the scene graph.
		getScene().update(timer.getNow());
		
		// Update implementation of the app.
		update();
		
		// Ends the current tick.
		timer.tickEnd();
	}
	
	/**
	 * Pauses the <code>AlchemyApplication</code>. If you want to add a pausing task, 
	 * use {@link #registerListener(ApplicationListener)} to register an {@link ApplicationListener#pause()}
	 */
	public final void pause() {
		listeners.forEach(ApplicationListener::pause);
		loop.stop();
	}
	
	/**
	 * Resumes the <code>AlchemyApplication</code>. If you want to add a resuming task, 
	 * use {@link #registerListener(ApplicationListener)} to register an {@link ApplicationListener#resume()}
	 */
	public final void resume() {
		listeners.forEach(ApplicationListener::resume);
		loop.start();
	}
	
	@Override
	@FXThread
	public void stop() throws Exception {
		super.stop();
		exit();
	}
	
	/**
	 * Called automatically when the close is requested. If you want to add an exiting task, use
	 * {@link AlchemyApplication#registerListener(ApplicationListener)} to register an {@link ApplicationListener#exit()}
	 * <p>
	 * You can however call the method to manually quit the application.
	 */
	public final void exit() {
		logger().info("Closing " + getClass().getSimpleName());
		
		listeners.forEach(ApplicationListener::exit);
		Platform.exit();
	}
	
	/**
	 * Registers the provided listener for the <code>AlchemyApplication</code>.
	 * 
	 * @param listener The listener to register.
	 */
	public void registerListener(final ApplicationListener listener) {
		this.listeners.add(listener);
	}
	
	/**
	 * Unregisters the provided listener from the <code>AlchemyApplication</code>.
	 * 
	 * @param listener The listener to unregister.
	 */
	public void unregisterListener(final ApplicationListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Initializes the <code>AlchemySettings</code> before starting the <code>Application</code>.
	 * It can be used to change the default settings.
	 * 
	 * @param settings The settings of the application.
	 */
	protected abstract void initializeSettings(AlchemySettings settings);
	
	/**
	 * Called <strong>BEFORE</strong> all internal initialize methods are called and <strong>AFTER</strong>
	 * the settings were initialized.
	 */
	protected void preInitialize() {}
	
	/**
	 * Initializes the <code>AlchemyApplication</code> by setting up the UI and the application pane. 
	 * 
	 * @param appRoot The application pane.
	 * @param uiRoot  The UI pane.
	 */
	protected abstract void initialize();
	
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
	 * This scene can be changed to an other implementation if needed.
	 * 
	 * @return The scene of the <code>AlchemyApplication</code>.
	 */
	public AlchemyScene getScene() {
		return scene;
	}
	
	/**
	 * @return The listeners of the <code>AlchemyApplication</code>.
	 */
	protected List<ApplicationListener> getListeners() {
		return listeners;
	}
	
	/**
	 * @return Whether the application has been correctly started, meaning
	 * 		   that the main loop can start.
	 */
	protected boolean hasStarted() {
		return started.get();
	}
	
	/**
	 * @return The asset manager of the <code>AlchemyApplication</code>.
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}
	
	/**
	 * @return The window of the <code>AlchemyApplication</code>.
	 */
	private Window getWindow() {
		return window;
	}
	
	/**
	 * @return The input manager of the <code>AlchemyApplication</code>.
	 */
	private InputManager getInputManager() {
		return inputManager;
	}
	
	/**
	 * @return The timer of the <code>AlchemyApplication</code>.
	 */
	public NanoTimer getTimer() {
		return timer;
	}
	
	/**
	 * @return The logger of the <code>AlchemyApplication</code>.
	 */
	protected Logger logger() {
		return logger;
	}
}
