package fr.alchemy.utilities.logging;

/**
 * <code>LoggerLevel</code> is an enumeration of all the existent logging levels, from
 * which the log trace is visible.
 * 
 * @author GnosticOccultist
 */
public enum LoggerLevel {
	/**
	 * Generic information level.
	 */
	INFO(true),
	/**
	 * Debugging level.
	 */
	DEBUG(false),
	/**
	 * TO-DO level.
	 */
	TODO(false),
	/**
	 * Warning level.
	 */
	WARNING(true),
	/**
	 * Error level.
	 */
	ERROR(true);
	
	/**
	 * Whether the level is active.
	 */
	private boolean active;
	
	private LoggerLevel(boolean active) {
		this.active = active;
	}
	
	/**
	 * Return whether the <code>LoggerLevel</code> is active.
	 * 
	 * @return The activity of the logging level.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Sets whether the <code>LoggerLevel</code> is active.
	 * 
	 * @param active The activity of the logging level.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return name();
	}
}
