package fr.alchemy.editor.core.config;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import fr.alchemy.editor.api.editor.EditorComponent;
import fr.alchemy.editor.core.AlchemyEditor;
import fr.alchemy.utilities.ByteUtils;
import fr.alchemy.utilities.collections.dictionnary.ObjectDictionary;

/**
 * <code>EditorConfig</code> contains the configuration of the Alchemy Editor.
 * 
 * @author GnosticOccultist
 */
public class EditorConfig {

	private static final String WORKSPACE_ALIAS = "workspace";
	private static final String PREF_CURRENT_WORKSPACE = WORKSPACE_ALIAS + "." + "current";
	private static final String PREF_LAYOUT_NAME = "layout.names";
	
	public static final String PREF_DIVIDER_POS = "editor.dividers";
	public static final String PREF_EDITOR_WIDTH = "editor.width";
	public static final String PREF_EDITOR_HEIGHT = "editor.height";
	public static final String PREF_EDITOR_MAXIMIZED = "editor.maximized";
	
	private static final String PREF_OPENED_COMPONENTS = ".components";
	private static final String PREF_OPENED_FILES = ".files";
	
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
	 * The list of layout names present in the editor.
	 */
	private final List<String> layoutNames;
	/**
	 * The lastly opened components by the editor for each layout.
	 */
	private final ObjectDictionary<String, ArrayList<String>> openedComponents = ObjectDictionary.ofType(String.class, ArrayList.class);
	/**
	 * The lastly opened files by the editor for each layout.
	 */
	private final ObjectDictionary<String, ArrayList<String>> openedFiles = ObjectDictionary.ofType(String.class, ArrayList.class);
	/**
	 * The currently used workspace folder.
	 */
	private volatile Path currentWorkspace;
	
	/**
	 * Use {@link #config()} instead to access to an instance
	 * of <code>EditorConfig</code>.
	 */
	private EditorConfig() {
		this.layoutNames = new ArrayList<String>();
		initialize();
	}
	
	/**
	 * Initialize the <code>EditorConfig</code> by retrieving the stored
	 * preferences if any.
	 */
	private void initialize() {
		currentWorkspace = get(PREF_CURRENT_WORKSPACE, null);
		
		byte[] byteArray = preferences().getByteArray(PREF_LAYOUT_NAME, null);
		if(byteArray == null) {
			return;
		}
		
		layoutNames.addAll(ByteUtils.deserialize(byteArray));
		
		for(String layout : layoutNames) {
			byte[] componentsByteArray = preferences().getByteArray(layout + PREF_OPENED_COMPONENTS, null);
			if(componentsByteArray == null) {
				return;
			}
			
			byte[] filesByteArray = preferences().getByteArray(layout + PREF_OPENED_FILES, null);
			if(filesByteArray == null) {
				return;
			}
			
			openedComponents.getOrCompute(layout, () -> new ArrayList<>()).addAll(ByteUtils.deserialize(componentsByteArray));
			openedFiles.getOrCompute(layout, () -> new ArrayList<>()).addAll(ByteUtils.deserialize(filesByteArray));
		}
	}
	
	/**
	 * Saves the preferences for the <code>AlchemyEditor</code>.
	 */
	public synchronized void save() {
		put(PREF_CURRENT_WORKSPACE, currentWorkspace);
		
		preferences().putByteArray(PREF_LAYOUT_NAME,
                ByteUtils.serialize((Serializable) layoutNames));
		
		layoutNames.forEach(l -> preferences().putByteArray(l + PREF_OPENED_COMPONENTS,
                ByteUtils.serialize(openedComponents.get(l))));
		
		layoutNames.forEach(l -> preferences().putByteArray(l + PREF_OPENED_FILES,
                ByteUtils.serialize(openedFiles.get(l))));
		
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
		} catch (IllegalArgumentException | URISyntaxException e) {
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
		if(path != null && !Files.exists(path)) {
			path = null;
		}
		
		if (path != null) {
			preferences().put(key, path.toUri().toString());       
		} else {
			preferences().remove(key);
		}
	}
	
	/**
	 * Return the saved width for the window in the <code>EditorConfig</code>, or the provided default width
	 * if it hasn't been found.
	 * 
	 * @param defWidth The default width to use if none is saved.
	 * @return		   A width to use for the window either the saved or the default one.
	 */
	public double getSavedWidth(double defWidth) {
		return preferences().getDouble(PREF_EDITOR_WIDTH, defWidth);
	}
	
	/**
	 * Return the saved height for the window in the <code>EditorConfig</code>, or the provided default height
	 * if it hasn't been found.
	 * 
	 * @param defHeight The default height to use if none is saved.
	 * @return			A height to use for the window either the saved or the default one.
	 */
	public double getSavedHeight(double defHeight) {
		return preferences().getDouble(PREF_EDITOR_HEIGHT, defHeight);
	}
	
	/**
	 * Return whether the window dimensions as been saved as maximized in the <code>EditorConfig</code>,
	 * or use the default boolean value if it hasn't been found.
	 * 
	 * @param defMaximized Whether the dimensions should be maximized by default.
	 * @return			   Whether the dimensions for the window should be maximized.
	 */
	public boolean isMaximized(boolean defMaximized) {
		return preferences().getBoolean(PREF_EDITOR_MAXIMIZED, defMaximized);
	}
	
	/**
	 * Save the provided window's width and height in the <code>EditorConfig</code> to be used the next time
	 * the editor is launched. Also save whether the dimensions are maximized.
	 * <p>
	 * Note that the width and height will be saved only if they are greater than zero.
	 * 
	 * @param width  	The window's width to be saved.
	 * @param height 	The window's height to be saved.
	 * @param maximized Whether the window dimensions are maximized.
	 */
	public void saveWindowDimensions(double width, double height, boolean maximized) {
		preferences().putBoolean(PREF_EDITOR_MAXIMIZED, maximized);
		
		if(width > 0D) {
			preferences().putDouble(PREF_EDITOR_WIDTH, width);
		}
		
		if(height > 0D) {
			preferences().putDouble(PREF_EDITOR_HEIGHT, height);
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
	
	/**
	 * Return the opened {@link EditorComponent} class name.
	 * 
	 * @return The array of component's class name.
	 */
	public List<String> getOpenedComponents(String layoutName) {
		if(!layoutNames.contains(layoutName)) {
			layoutNames.add(layoutName);
		}
		
		return openedComponents.getOrCompute(layoutName, () -> new ArrayList<String>());
	}
	
	/**
	 * Add an opened {@link EditorComponent} class name.
	 * 
	 * @param component The class name of the component.
	 * @return 			The updated config.
	 */
	public void addOpenedComponent(String layoutName, String component) {
		getOpenedComponents(layoutName).add(component);
	}
	
	/**
	 * Removes an opened {@link EditorComponent} class name.
	 * 
	 * @param component The class name of the component.
	 * @return 			The updated config.
	 */
	public void removeOpenedComponent(String layoutName, String component) {
		getOpenedComponents(layoutName).remove(component);
	}
	
	/**
	 * Return the opened {@link EditorComponent} class name.
	 * 
	 * @return The array of component's class name.
	 */
	public List<String> getOpenedFiles(String layoutName) {
		if(!layoutNames.contains(layoutName)) {
			layoutNames.add(layoutName);
		}
		
		return openedFiles.getOrCompute(layoutName, () -> new ArrayList<String>());
	}
	
	/**
	 * Add an opened {@link EditorComponent} class name.
	 * 
	 * @param component The class name of the component.
	 * @return 			The updated config.
	 */
	public void addOpenedFile(String layoutName, String file) {
		getOpenedFiles(layoutName).add(file);
	}
	
	/**
	 * Removes an opened {@link EditorComponent} class name.
	 * 
	 * @param component The class name of the component.
	 * @return 			The updated config.
	 */
	public void removeOpenedFile(String layoutName, String file) {
		getOpenedFiles(layoutName).remove(file);
	}
}
