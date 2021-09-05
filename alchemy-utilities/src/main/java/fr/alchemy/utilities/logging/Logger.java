package fr.alchemy.utilities.logging;

import java.io.PrintStream;

/**
 * <code>Logger</code> represents an interface to implement a logging device for an application.
 * A logger is capable of printing message at different {@link LoggerLevel} to a console using a {@link PrintStream} for example.
 * 
 * @version 0.2.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public interface Logger {
	
	/**
	 * Return the name of the <code>Logger</code>.
	 * 
	 * @return The name of the logger (not null, not empty).
	 */
	String getName();
	
	/**
	 * Sets the name of the <code>Logger</code>.
	 * 
	 * @param name The name of the logger (not null, not empty).
	 */
	void setName(String name);
	
	/**
	 * Return whether the indicated {@link LoggerLevel} is active for the <code>Logger</code>.
	 * 
	 * @param level The level to check activity (not null).
	 * @return		Whether the level is active.
	 */
	default boolean isActive(LoggerLevel level) {
		return level.isActive();
	}
	
	/**
	 * Sets whether the indicated {@link LoggerLevel} should be active for the <code>Logger</code>.
	 * 
	 * @param level  The level to modify activity (not null).
	 * @param active Whether the level should be active.
	 * @return		 Whether the modification has been applied.
	 */
	default boolean setActive(LoggerLevel level, boolean active) {
		return false;
	}
	
	/**
	 * Prints the specified message at the {@link LoggerLevel#INFO} level.
	 * 
	 * @param message The message to print.
	 */
	default void info(String message) {
		print(LoggerLevel.INFO, message);
	}
	
	/**
	 * Prints the specified message at the {@link LoggerLevel#DEBUG} level.
	 * 
	 * @param message The message to print.
	 */
	default void debug(String message) {
		print(LoggerLevel.DEBUG, message);
	}

	/**
	 * Prints the specified message at the {@link LoggerLevel#TODO} level.
	 * 
	 * @param message The message to print.
	 */
	default void todo(String message) {
		print(LoggerLevel.TODO, message);
	}
	
	/**
	 * Prints the specified exception at the {@link LoggerLevel#TODO} level.
	 * 
	 * @param exception The exception to print (not null).
	 */
	default void todo(Throwable exception) {
		print(LoggerLevel.TODO, exception);
	}
	
	/**
	 * Prints the specified message and exception at the {@link LoggerLevel#TODO} level.
	 * 
	 * @param message   The message to print.
	 * @param exception The exception to print (not null).
	 */
	default void todo(String message, Throwable exception) {
		print(LoggerLevel.TODO, message, exception);
	}
	
	/**
	 * Prints the specified message at the {@link LoggerLevel#WARNING} level.
	 * 
	 * @param message The message to print.
	 */
	default void warning(String message) {
		print(LoggerLevel.WARNING, message);
	}
	
	/**
	 * Prints the specified exception at the {@link LoggerLevel#WARNING} level.
	 * 
	 * @param exception The exception to print (not null).
	 */
    default void warning(Throwable exception) {
        print(LoggerLevel.WARNING, exception);
    }
    
    /**
	 * Prints the specified message and exception at the {@link LoggerLevel#WARNING} level.
	 * 
	 * @param message   The message to print.
	 * @param exception The exception to print (not null).
	 */
	default void warning(String message, Throwable exception) {
		print(LoggerLevel.WARNING, message, exception);
	}
	
	/**
	 * Prints the specified message at the {@link LoggerLevel#ERROR} level.
	 * 
	 * @param message The message to print.
	 */
	default void error(String message) {
		print(LoggerLevel.ERROR, message);
	}
	
	/**
	 * Prints the specified exception at the {@link LoggerLevel#ERROR} level.
	 * 
	 * @param exception The exception to print (not null).
	 */
	default void error(Throwable exception) {
		print(LoggerLevel.ERROR, exception);
	}
	
	/**
	 * Prints the specified exception and message at the {@link LoggerLevel#ERROR} level.
	 * 
	 * @param message   The message to print.
	 * @param exception The exception to print (not null).
	 */
	default void error(String message, Throwable exception) {
		print(LoggerLevel.ERROR, message, exception);
	}
	
	/**
	 * Prints the specified message at the given {@link LoggerLevel}.
	 * 
	 * @param level		The level of the message (not null).
	 * @param message   The message to print.
	 */
	void print(LoggerLevel level, String message);
	
	/**
	 * Prints the specified exception at the given {@link LoggerLevel}.
	 * 
	 * @param level		The level of the message (not null).
	 * @param exception The exception to print (not null).
	 */
	void print(LoggerLevel level, Throwable exception);
	
	/**
	 * Prints the specified message and the exception at the given {@link LoggerLevel}.
	 * 
	 * @param level		The level of the message (not null).
	 * @param message   The message to print.
	 * @param exception The exception to print (not null).
	 */
	void print(LoggerLevel level, String message, Throwable exception);
}
