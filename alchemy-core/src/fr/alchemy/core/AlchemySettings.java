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
	
	static {
		DEFAULT_SETTINGS.put("Title", "Alchemy Application");
		DEFAULT_SETTINGS.put("Version", "0.1");
		DEFAULT_SETTINGS.put("Width", 800);
		DEFAULT_SETTINGS.put("Height", 600);
	}
	
	public AlchemySettings(boolean loadDefault) {
		if(loadDefault) {
			putAll(DEFAULT_SETTINGS);
		}
	}
	
	/**
	 * @return The width of the application.
	 */
	public int getWidth() {
		return (int) get("Width");
	}
	
	/**
	 * @return The height of the application.
	 */
	public int getHeight() {
		return (int) get("Height");
	}
	
	/**
	 * @return The title of the application.
	 */
	public String getTitle() {
		return (String) get("Title");
	}
	
	/**
	 * @return The version of the application.
	 */
	public String getVersion() {
		return (String) get("Version");
	}
}
