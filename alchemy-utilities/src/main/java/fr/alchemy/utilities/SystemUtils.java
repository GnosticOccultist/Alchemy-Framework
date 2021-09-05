package fr.alchemy.utilities;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <code>SystemUtils</code> provides utility functions concerning the system and its properties.
 * 
 * @version 0.2.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public final class SystemUtils {
	
	/**
	 * Private constructor to inhibit instantiation of <code>SystemUtils</code>.
	 */
	private SystemUtils() {}
	
	/**
	 * Return the currently operating system name.
	 * 
	 * @return The current operating system name.
	 */
	public static String getOS() {
		return System.getProperty("os.name");
	}
	
	/**
	 * Return the current working directory as a string.
	 * 
	 * @return The current working directory.
	 */
	public static String getWorkingDirectory() {
		return System.getProperty("user.dir");
	}
	
	/**
	 * Return a path to the current working directory.
	 * 
	 * @return The current working directory.
	 */
	public static Path pathToWorkingDirectory() {
		return Paths.get(getWorkingDirectory());
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
