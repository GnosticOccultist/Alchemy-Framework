package fr.alchemy.core;

import java.util.HashMap;

/**
 * <code>AlchemySettings</code> gathers all the settings for the <code>AlchemyApplication</code>.
 * 
 * @author GnosticOccultist
 */
public final class AlchemySettings extends HashMap<String, Object> {
	
	private static final long serialVersionUID = -8759245099782840800L;
	
	/**
	 * The default settings instance.
	 */
	private static final AlchemySettings DEFAULT_SETTINGS = new AlchemySettings(false);
	/**
	 * The settings instance.
	 */
	private static AlchemySettings SETTINGS_INSTANCE;
	/**
	 * @return The application settings instance.
	 */
	public static AlchemySettings settings() {
		if(SETTINGS_INSTANCE == null) {
			SETTINGS_INSTANCE = new AlchemySettings(true);
		}
		return SETTINGS_INSTANCE;
	}
	
	static {
		DEFAULT_SETTINGS.put("Title", "Alchemy Application");
		DEFAULT_SETTINGS.put("Version", "0.1");
		DEFAULT_SETTINGS.put("Width", 800);
		DEFAULT_SETTINGS.put("Height", 600);
		DEFAULT_SETTINGS.put("Resizable", false);
		DEFAULT_SETTINGS.put("Maximized", false);
		DEFAULT_SETTINGS.put("Fullscreen", false);
		DEFAULT_SETTINGS.put("ShowFPS", false);
		DEFAULT_SETTINGS.put("IconPaths", new String[] {"resources/icons/logo_colored_x32.png"});
	}
	
	private AlchemySettings(boolean loadDefault) {
		if(loadDefault) {
			putAll(DEFAULT_SETTINGS);
		}
	}
	
	/**
	 * @return The value of the specified key.
	 */
	@SuppressWarnings("unchecked")
	public <T> T value(final String key) {
		return (T) super.get(key);
	}
	
	/**
	 * @return The value of the specified key as a boolean.
	 */
	public boolean boolValue(final String key) {
		return value(key);
	}
	
	/**
	 * @return The width of the application.
	 */
	public int getWidth() {
		return value("Width");
	}
	
	/**
	 * @return The height of the application.
	 */
	public int getHeight() {
		return value("Height");
	}
	
	/**
	 * @return The title of the application.
	 */
	public String getTitle() {
		return value("Title");
	}
	
	/**
	 * @return The version of the application.
	 */
	public String getVersion() {
		return value("Version");
	}
	
	/**
	 * @return The icon paths of the application window.
	 */
	public String[] getIconPaths() {
		return value("IconPaths");
	}
}
