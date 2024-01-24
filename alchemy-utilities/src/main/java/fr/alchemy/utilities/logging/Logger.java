package fr.alchemy.utilities.logging;

import java.io.PrintStream;
import java.util.Objects;

/**
 * <code>Logger</code> represents an interface to implement a logging device for
 * an application. A logger is capable of printing message at different
 * {@link LoggerLevel} to a console using a {@link PrintStream} for example.
 * 
 * @version 0.2.1
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
	 * Return whether the indicated {@link LoggerLevel} is active for the
	 * <code>Logger</code>.
	 * 
	 * @param level The level to check activity (not null).
	 * @return Whether the level is active.
	 */
	default boolean isActive(LoggerLevel level) {
		return level.isActive();
	}

	/**
	 * Sets whether the indicated {@link LoggerLevel} should be active for the
	 * <code>Logger</code>.
	 * 
	 * @param level  The level to modify activity (not null).
	 * @param active Whether the level should be active.
	 * @return Whether the modification has been applied.
	 */
	default boolean setActive(LoggerLevel level, boolean active) {
		return false;
	}

	/**
	 * Prints the specified object at the {@link LoggerLevel#INFO} level.
	 * 
	 * @param obj The object to print, may be null.
	 */
	default void info(Object obj) {
		print(LoggerLevel.INFO, Objects.toString(obj));
	}

	/**
	 * Prints the specified message at the {@link LoggerLevel#INFO} level.
	 * 
	 * @param message The message to print.
	 */
	default void info(CharSequence message) {
		print(LoggerLevel.INFO, message);
	}

	/**
	 * Prints the specified object at the {@link LoggerLevel#DEBUG} level.
	 * 
	 * @param obj The object to print, may be null.
	 */
	default void debug(Object obj) {
		print(LoggerLevel.DEBUG, Objects.toString(obj));
	}

	/**
	 * Prints the specified message at the {@link LoggerLevel#DEBUG} level.
	 * 
	 * @param message The message to print.
	 */
	default void debug(CharSequence message) {
		print(LoggerLevel.DEBUG, message);
	}

	/**
	 * Prints the specified object at the {@link LoggerLevel#TODO} level.
	 * 
	 * @param obj The object to print, may be null.
	 */
	default void todo(Object obj) {
		print(LoggerLevel.TODO, Objects.toString(obj));
	}

	/**
	 * Prints the specified message at the {@link LoggerLevel#TODO} level.
	 * 
	 * @param message The message to print.
	 */
	default void todo(CharSequence message) {
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
	 * Prints the specified message and exception at the {@link LoggerLevel#TODO}
	 * level.
	 * 
	 * @param message   The message to print.
	 * @param exception The exception to print (not null).
	 */
	default void todo(CharSequence message, Throwable exception) {
		print(LoggerLevel.TODO, message, exception);
	}

	/**
	 * Prints the specified message at the {@link LoggerLevel#WARNING} level.
	 * 
	 * @param message The message to print.
	 */
	default void warning(CharSequence message) {
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
	 * Prints the specified message and exception at the {@link LoggerLevel#WARNING}
	 * level.
	 * 
	 * @param message   The message to print.
	 * @param exception The exception to print (not null).
	 */
	default void warning(CharSequence message, Throwable exception) {
		print(LoggerLevel.WARNING, message, exception);
	}

	/**
	 * Prints the specified message at the {@link LoggerLevel#ERROR} level.
	 * 
	 * @param message The message to print.
	 */
	default void error(CharSequence message) {
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
	 * Prints the specified exception and message at the {@link LoggerLevel#ERROR}
	 * level.
	 * 
	 * @param message   The message to print.
	 * @param exception The exception to print (not null).
	 */
	default void error(CharSequence message, Throwable exception) {
		print(LoggerLevel.ERROR, message, exception);
	}

	/**
	 * Prints the specified object at the given {@link LoggerLevel}.
	 * 
	 * @param level The level of the message (not null).
	 * @param obj   The object to print, may be null.
	 */
	default void print(LoggerLevel level, Object obj) {
		print(level, Objects.toString(obj));
	}

	/**
	 * Prints the specified message at the given {@link LoggerLevel}.
	 * 
	 * @param level   The level of the message (not null).
	 * @param message The message to print.
	 */
	void print(LoggerLevel level, CharSequence message);

	/**
	 * Prints the specified exception at the given {@link LoggerLevel}.
	 * 
	 * @param level     The level of the message (not null).
	 * @param exception The exception to print (not null).
	 */
	void print(LoggerLevel level, Throwable exception);

	/**
	 * Prints the specified message and the exception at the given
	 * {@link LoggerLevel}.
	 * 
	 * @param level     The level of the message (not null).
	 * @param message   The message to print.
	 * @param exception The exception to print (not null).
	 */
	void print(LoggerLevel level, CharSequence message, Throwable exception);
}
