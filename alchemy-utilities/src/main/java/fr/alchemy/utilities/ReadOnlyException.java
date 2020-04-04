package fr.alchemy.utilities;

/**
 * <code>ReadOnlyException</code> is a {@link RuntimeException} raised when a readable-only
 * object is trying to be modified.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public final class ReadOnlyException extends RuntimeException {

	private static final long serialVersionUID = 6748636991058980087L;
	
	/**
	 * Instantiates a new <code>ReadOnlyException</code>.
	 */
	public ReadOnlyException() {
		super();
	}
	
	/**
	 * Instantiates a new <code>ReadOnlyException</code> with the provided
	 * error message.
	 * 
	 * @param message The error message to display.
	 */
	public ReadOnlyException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new <code>ReadOnlyException</code> with the provided
	 * cause.
	 * 
	 * @param cause The cause that has been throwing the exception.
	 */
	public ReadOnlyException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Instantiates a new <code>ReadOnlyException</code> with the provided
	 * error message and cause.
	 * 
	 * @param message The error message to display.
	 * @param cause   The cause that has been throwing the exception.
	 */
	public ReadOnlyException(String message, Throwable cause) {
		super(message, cause);
	}
}
