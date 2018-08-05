package fr.alchemy.utilities;

/**
 * <code>SystemUtils</code> provides utilities functions concerning the system and
 * its properties.
 * 
 * @author GnosticOccultist
 */
public class SystemUtils {
	
	/**
	 * @return The current operating system name.
	 */
	public static String getOS() {
		return System.getProperty("os.name");
	}
}
