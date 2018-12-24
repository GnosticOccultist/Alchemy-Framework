package fr.alchemy.utilities;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <code>SystemUtils</code> provides utilities functions concerning the system and
 * its properties.
 * 
 * @author GnosticOccultist
 */
public final class SystemUtils {
	
	/**
	 * Return the currently operating system name.
	 * 
	 * @return The current operating system name.
	 */
	public static String getOS() {
		return System.getProperty("os.name");
	}
	
	/**
	 * Return the user home directory as a string.
	 * 
	 * @return The user home directory.
	 */
	public static String getUserHome() {
		return System.getProperty("user.home");
	}
	
	/**
	 * Return a path to the user home directory.
	 * 
	 * @return The user home directory.
	 */
	public static Path pathToUserHome() {
		return Paths.get(getUserHome());
	}
}
