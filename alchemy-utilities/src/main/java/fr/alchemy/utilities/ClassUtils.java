package fr.alchemy.utilities;

/**
 * <code>ClassUtils</code> provides utilities functions concerning classes.
 * 
 * @version 0.2.1
 * @since 0.2.1
 * 
 * @author GnosticOccultist
 */
public final class ClassUtils {

	/**
	 * Private constructor to inhibit instantiation of <code>ClassUtils</code>.
	 */
	private ClassUtils() {
	}

	/**
	 * Performs an unsafe cast of the provided object to an expected type.
	 *
	 * @param object The object to cast.
	 * @param <T>    The expected type.
	 * @return The casted object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T unsafeCast(Object object) {
		return (T) object;
	}
}
