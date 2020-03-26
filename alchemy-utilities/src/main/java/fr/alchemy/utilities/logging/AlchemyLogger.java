package fr.alchemy.utilities.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

import fr.alchemy.utilities.Validator;

/**
 * <code>AlchemyLogger</code> is an implementation of {@link Logger} for the <code>Alchemy-Framework</code>,
 * and can be reused over multiple projects.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public class AlchemyLogger implements Logger {

	/**
	 * The array containing the activity of each level.
	 */
	private final Boolean[] activities;
	/**
	 * The name of the logger.
	 */
	private String name;
	
	/**
	 * Protected constructor to inhibit instantiation of <code>AlchemyLogger</code>. 
	 * Please use {@link FactoryLogger#getLogger(String)} to create a new logger instance.
	 */
	protected AlchemyLogger() {
		this.name = "unknown";
		this.activities = new Boolean[LoggerLevel.values().length];
	}
	
	/**
	 * Return the name of the <code>AlchemyLogger</code>.
	 * 
	 * @return The name of the logger (not null, not empty).
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the <code>AlchemyLogger</code>.
	 * 
	 * @param name The name of the logger (not null, not empty).
	 */
	@Override
	public void setName(String name) {
		Validator.nonEmpty(name, "The logger name can't be empty or null!");
		this.name = name;
	}
	
	/**
	 * Return whether the indicated {@link LoggerLevel} is active for the <code>AlchemyLogger</code>.
	 * 
	 * @param level The level to check activity (not null).
	 * @return		Whether the level is active.
	 */
	@Override
	public boolean isActive(LoggerLevel level) {
		Validator.nonNull(level, "The logger level can't be null!");
		Boolean value = activities[level.ordinal()];
		if(value == null) {
			return level.isActive();
		}
		return value;
	}
	
	/**
	 * Sets whether the indicated {@link LoggerLevel} should be active for the <code>AlchemyLogger</code>.
	 * 
	 * @param level  The level to modify activity (not null).
	 * @param active Whether the level should be active.
	 * @return		 Whether the modification has been applied.
	 */
	@Override
	public boolean setActive(LoggerLevel level, boolean active) {
		Validator.nonNull(level, "The logger level can't be null!");
		activities[level.ordinal()] = active;
        return true;
	}

	/**
	 * Prints the specified message at the given {@link LoggerLevel}.
	 * 
	 * @param level		The level of the message (not null).
	 * @param message   The message to print.
	 */
	@Override
	public void print(LoggerLevel level, String message) {
		if(isActive(level)) {
			FactoryLogger.print(level, getName(), message);
        }
	}

	/**
	 * Prints the specified exception at the given {@link LoggerLevel}.
	 * 
	 * @param level		The level of the message (not null).
	 * @param exception The exception to print (not null). 
	 */
	@Override
	public void print(LoggerLevel level, Throwable exception) {
		Validator.nonNull("The exception to print can't be null!");
		if(isActive(level)) {
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			exception.printStackTrace(printWriter);
			
			FactoryLogger.print(level, getName(), writer.toString());
        }
	}

	/**
	 * Prints the specified message and the exception at the given {@link LoggerLevel}.
	 * 
	 * @param level		The level of the message (not null).
	 * @param message   The message to print.
	 * @param throwable The exception to print (not null).
	 */
	@Override
	public void print(LoggerLevel level, String message, Throwable exception) {
		if(isActive(level)) {
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			exception.printStackTrace(printWriter);
			
			FactoryLogger.print(level, getName(), message + " " + writer.toString());
        }
	}
}
