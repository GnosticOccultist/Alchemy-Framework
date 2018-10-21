package fr.alchemy.editor.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.alchemy.core.asset.AssetManager;
import javafx.scene.image.Image;

/**
 * <code>EditorManager</code> is a single-instance utility class for  containing all 
 * the managing class such as the {@link AssetManager}.
 * 
 * @author GnosticOccultist
 */
public class EditorManager {
	
	/**
	 * The editor logger.
	 */
	private Logger logger = LoggerFactory.getLogger("alchemy.editor");
	
	/**
	 * The single-instance of the editor manager.
	 */
	private static EditorManager INSTANCE;
	
	/**
	 * Instantiates the single-instance <code>EditorManager</code> with the 
	 * specified {@link AssetManager}.
	 * 
	 * @param assetManager The asset manager to use (not null).
	 * @return			   The instance of the manager.
	 */
	public static EditorManager initialize(AssetManager assetManager) {
		
		if(INSTANCE == null && assetManager != null) {
			INSTANCE = new EditorManager(assetManager);
		}
		
		return INSTANCE;
	}
	
	/**
	 * Return the single-instance <code>EditorManager</code>.
	 * 
	 * @return The editor manager.
	 */
	public static EditorManager editor() {
		return INSTANCE;
	}
	
	/**
	 * The asset manager used by the editor.
	 */
	private final AssetManager assetManager;
	
	/**
	 * Single-instance class. You shouldn't need to call this constructor.
	 * Please use {@link #editor()} to use it.
	 * 
	 * @param assetManager The asset manager to use.
	 */
	private EditorManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	/**
	 * Loads an icon from the specified file path with the
	 * loaded <code>AssetManager</code>.
	 * 
	 * @param name The icon path.
	 * @return	   The image containing the icon.
	 */
	public Image loadIcon(String name) {
		return assetManager.loadIcon(name);
	}
	
	/**
	 * Return the used <code>AssetManager</code> by the manager.
	 * 
	 * @return The asset manager.
	 */
	public AssetManager assetManager() {
		return assetManager;
	}
	
	/**
	 * Return the logger used by the editor.
	 * 
	 * @return The editor's logger.
	 */
	public Logger logger() {
		return logger;
	}
}
