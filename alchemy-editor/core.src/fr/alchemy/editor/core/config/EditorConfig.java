package fr.alchemy.editor.core.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import fr.alchemy.editor.core.AlchemyEditor;

/**
 * <code>EditorConfig</code> contains the configuration of the Alchemy Editor.
 * 
 * @author GnosticOccultist
 */
public class EditorConfig {

	private static final String WORKSPACE_ALIAS = "workspace";
	private static final String PREF_CURRENT_WORKSPACE = WORKSPACE_ALIAS + "." + "current";
	
	private static final String PREF_EDITOR_COMPONENTS = "opened.components";
	
	/**
	 * The editor configuration instance.
	 */
	private static volatile EditorConfig instance;
	
	/**
	 * Return the configuration of the editor.
	 * 
	 * @return The editor configuration.
	 */
	public static EditorConfig config() {
        if (instance == null) {
            synchronized (EditorConfig.class) {
                if (instance == null) {
                    instance = new EditorConfig();
                }
            }
        }
        return instance;
	}
	
	/**
	 * The currently used workspace folder.
	 */
	private volatile Path currentWorkspace;
	
	/**
	 * Use {@link #config()} instead to access to an instance
	 * of <code>EditorConfig</code>.
	 */
	private EditorConfig() {
		initialize();
	}
	
	/**
	 * Initialize the <code>EditorConfig</code> by retrieving the stored
	 * preferences if any.
	 */
	private void initialize() {
		currentWorkspace = get(PREF_CURRENT_WORKSPACE, null);
	}
	
	/**
	 * Saves the preferences for the <code>AlchemyEditor</code>.
	 */
	public synchronized void save() {
		put(PREF_CURRENT_WORKSPACE, currentWorkspace);
		
        try {
            preferences().flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
	}

	/**
	 * Return the path stored inside the preferences for the 
	 * <code>AlchemyEditor</code> using the key.
	 * 
	 * @param key The key to access the path.
	 * @param def The default value to return.
	 * @return	  The path stored or the default value if not found.
	 */
	public Path get(String key, String def) {
		
		Path path = null;
		try {
			String name = preferences().get(key, def);
			if(name != null) {
				path = Paths.get(new URI(name));
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		if(path != null && !Files.exists(path)) {
			path = null;
		}
		
		return path;
	}
	
	/**
	 * Stores the specified path using the key inside the preferences
	 * for the <code>AlchemyEditor</code>.
	 * <p>
	 * If the given path is null, then it will remove the specified key.
	 * 
	 * @param key  The key to access the path.
	 * @param path The path to store.
	 */
	public void put(String key, Path path) {
		if (path != null && !Files.exists(path)) {
			path = null;
		}
		
		if (path != null) {
			preferences().put(key, path.toUri().toString());       
		} else {
			preferences().remove(key);
		}
	}
	
	/**
	 * Access the {@link Preferences} for the <code>AlchemyEditor</code>.
	 * 
	 * @return The preferences for the editor.
	 */
	public static Preferences preferences() {
		return Preferences.userNodeForPackage(AlchemyEditor.class);
	}
	
	/**
	 * Return the currently used workspace.
	 * 
	 * @return The current workspace.
	 */
	public Path getCurrentWorkspace() {
		return currentWorkspace;
	}
	
	/**
	 * Set the currently used workspace.
	 * 
	 * @param currentWorkspace The current workspace.
	 */
	public void setCurrentWorkspace(Path currentWorkspace) {
		this.currentWorkspace = currentWorkspace;
	}
}
