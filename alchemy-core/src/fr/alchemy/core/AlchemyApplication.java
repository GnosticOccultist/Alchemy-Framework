package fr.alchemy.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.alchemy.core.annotation.FXThread;
import fr.alchemy.core.entity.Entity;
import fr.alchemy.core.input.Mouse;
import fr.alchemy.core.listener.ExitListener;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
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
	
	private Logger logger = LoggerFactory.getLogger("alchemy.app");
	
	private AlchemySettings settings = new AlchemySettings(true);
	
	private Pane mainRoot, appRoot, uiRoot;
	/**
	 * The application window.
	 */
	protected Stage mainStage;
	/**
	 * The application scene.
	 */
	protected Scene mainScene;
	
	private AnimationTimer timer;
	/**
	 * The current time in nanoseconds.
	 */
	protected long currentTime = 0; 
	/**
	 * Holds the mouse informations.
	 */
	protected Mouse mouse = new Mouse(); 
	private Map<KeyCode, Boolean> keys = new HashMap<>(); 
	private Map<KeyCode, Runnable> keyPressActions = new HashMap<>(); 
	private Map<KeyCode, Runnable> keyTypedActions = new HashMap<>(); 
	
	private List<ExitListener> listeners = new ArrayList<>();

	@Override
	@FXThread
	public void start(Stage primaryStage) throws Exception {
		initializeSettings(settings);
		
		this.mainStage = primaryStage;
		
		this.appRoot = new Pane();
		this.uiRoot = new Pane();
		this.mainRoot = new Pane(appRoot, uiRoot);
		this.mainRoot.setPrefSize(settings.getWidth(), settings.getHeight());
		
		initialize(appRoot, uiRoot);
		
		mainScene = new Scene(mainRoot);
		mainScene.setOnKeyPressed(event -> {
			if(!isPressed(event.getCode()) && keyTypedActions.containsKey(event.getCode())) {
				keys.put(event.getCode(), true);
				keyTypedActions.get(event.getCode()).run();
			} else {
				keys.put(event.getCode(), true);
			}
		});
		mainScene.setOnKeyReleased(event -> keys.put(event.getCode(), false));
		mainScene.setOnMousePressed(mouse::update);
		mainScene.setOnMouseReleased(mouse::update);
		mainScene.setOnMouseDragged(mouse::update);
		mainScene.setOnMouseMoved(mouse::update);
		
		primaryStage.setScene(mainScene);
		primaryStage.setWidth(settings.getWidth() + 6);
		primaryStage.setHeight(settings.getHeight() + 29);
		primaryStage.setTitle(settings.getTitle() + " " + settings.getVersion());
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(event -> {
			exit();
		});
		primaryStage.show();
		
		timer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				currentTime = now;
				processInput();
				
				update(now);
				appRoot.getChildren().stream().map(node -> (Entity) node).forEach(entity -> entity.update(now));
			}
		};
		postInitialize();
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

	private void processInput() { 
		keyPressActions.forEach((key, action) -> {if (isPressed(key)) action.run();}); 
	} 
	
	/**
	 * @return Whether the specified key is currently pressed.
	 */
	private boolean isPressed(KeyCode key) {
		return keys.getOrDefault(key, false);
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
	 * 
	 * @param now The current time.
	 */
	protected abstract void update(long now);
	
	/**
	 * @return The logger of the <code>AlchemyApplication</code>.
	 */
	protected Logger logger() {
		return logger;
	}
}
