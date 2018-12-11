package fr.alchemy.utilities.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class AlchemyLogger implements Logger {

	/**
	 * The array containing the activity of each level.
	 */
	private final Boolean[] activities;
	/**
	 * The name of the logger.
	 */
	private String name;
	
	public AlchemyLogger() {
		this.name = "unknown";
		this.activities = new Boolean[LoggerLevel.values().length];
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean isActive(LoggerLevel level) {
		Boolean value = activities[level.ordinal()];	
		return value != null && value || level.isActive();
	}
	
	@Override
	public boolean setActive(LoggerLevel level, boolean active) {
		activities[level.ordinal()] = active;
        return true;
	}

	@Override
	public void print(LoggerLevel level, String message) {
		if (isActive(level)) {
			FactoryLogger.print(level, getName(), message);
        }
	}

	@Override
	public void print(LoggerLevel level, Throwable exception) {
		if (isActive(level)) {
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			exception.printStackTrace(printWriter);
			
			FactoryLogger.print(level, getName(), writer.toString());
        }
	}

	@Override
	public void print(LoggerLevel level, String message, Throwable exception) {
		if (isActive(level)) {
			StringWriter writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			exception.printStackTrace(printWriter);
			
			FactoryLogger.print(level, getName(), message + writer.toString());
        }
	}
}
