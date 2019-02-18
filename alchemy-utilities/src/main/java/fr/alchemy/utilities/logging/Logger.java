package fr.alchemy.utilities.logging;

/**
 * <code>Logger</code> represents an interface to implement a logging device
 * for an application.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public interface Logger {
	
	/**
	 * Return the name of the <code>Logger</code>.
	 * 
	 * @return The name of the logger.
	 */
	String getName();
	
	/**
	 * Sets the name of the <code>Logger</code>.
	 * 
	 * @param name The name of the logger.
	 */
	void setName(String name);
	
	/**
	 * Return whether the indicated <code>LoggerLevel</code> is active.
	 * 
	 * @param level The level to check activity.
	 * @return		Whether the level is active.
	 */
	default boolean isActive(LoggerLevel level) {
		return level.isActive();
	}
	
	/**
	 * Sets whether the indicated <code>LoggerLevel</code> should be active.
	 * 
	 * @param level  The level to modify activity.
	 * @param active Whether the level is active.
	 * @return		 Whether the modification has been applied.
	 */
	default boolean setActive(LoggerLevel level, boolean active) {
		return false;
	}
	
	/**
	 * Prints the specified message at the {@link LoggerLevel#INFO}.
	 * 
	 * @param message The message to print.
	 */
	default void info(String message) {
		print(LoggerLevel.INFO, message);
	}
	
	/**
	 * Prints the specified message at the {@link LoggerLevel#DEBUG}
	 * 
	 * @param message The message to print.
	 */
	default void debug(String message) {
		print(LoggerLevel.DEBUG, message);
	}

	/**
	 * Prints the specified message at the {@link LoggerLevel#TODO}
	 * 
	 * @param message The message to print.
	 */
	default void todo(String message) {
		print(LoggerLevel.TODO, message);
	}
	
	/**
	 * Prints the specified message at the {@link LoggerLevel#WARNING}
	 * 
	 * @param message The message to print.
	 */
	default void warning(String message) {
		print(LoggerLevel.WARNING, message);
	}
	
	/**
	 * Prints the specified exception at the {@link LoggerLevel#WARNING}.
	 * 
	 * @param exception The exception to print.
	 */
    default void warning(Throwable exception) {
        print(LoggerLevel.WARNING, exception);
    }
	
	/**
	 * Prints the specified message at the {@link LoggerLevel#ERROR}.
	 * 
	 * @param message The message to print.
	 */
	default void error(String message) {
		print(LoggerLevel.ERROR, message);
	}
	
	/**
	 * Prints the specified exception at the {@link LoggerLevel#ERROR}.
	 * 
	 * @param exception The exception to print.
	 */
	default void error(Throwable exception) {
		print(LoggerLevel.ERROR, exception);
	}
	
	/**
	 * Prints the specified exception and message at the {@link LoggerLevel#ERROR}.
	 * 
	 * @param message   The message to print.
	 * @param exception The exception to print.
	 */
	default void error(String message, Throwable exception) {
		print(LoggerLevel.ERROR, message, exception);
	}
	
	/**
	 * Prints the specified message at the given level.
	 * 
	 * @param level		The level of the message.
	 * @param message   The message to print.
	 */
	void print(LoggerLevel level, String message);
	
	/**
	 * Prints the specified exception at the given level.
	 * 
	 * @param level		The level of the message.
	 * @param exception The exception to print.
	 */
	void print(LoggerLevel level, Throwable exception);
	
	/**
	 * Prints the specified message at the given level and the exception.
	 * 
	 * @param level		The level of the message.
	 * @param message   The message to print.
	 * @param throwable The exception to print.
	 */
	void print(LoggerLevel level, String message, Throwable exception);
}
