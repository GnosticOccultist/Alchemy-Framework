package fr.alchemy.utilities;

import java.util.Objects;

/**
 * <code>Validator</code> contains utility methods to throw exceptions 
 * for invalid arguments.
 * <p>
 * The methods are intended for checking arguments for <code>public</code>/<code>protected</code>
 * methods. For <code>private</code>/<code>package</code>, you should use assertions instead.
 * 
 * @author Stickxy
 */
public final class Validator {
	
	/**
	 * No instance of this class.
	 */
	private Validator() {}
	
	/**
	 * Validate a non-null reference. In many methods, validation can be omitted
	 * because the object in question is about to be dereferenced.
	 * <p>
	 * While it might seem more logical to throw an {@link IllegalArgumentException} in
	 * the case of a method argument, the javadoc for {@link NullPointerException} says,
	 * <code>"Applications should throw instances of this class..."</code>
	 * <p>
	 * Note that the function is just using: {@link Objects#requireNonNull(Object)}.
	 * 
	 * @param object 				The reference to validate (not null).
	 * @return		 				The object validated.
	 * @throws NullPointerException If the reference is null.
	 */
	public static <T> T nonNull(T object) {
		return Objects.requireNonNull(object);
	}
	
	/**
	 * Validate a non-null reference. In many methods, validation can be omitted
	 * because the object in question is about to be dereferenced.
	 * <p>
	 * While it might seem more logical to throw an {@link IllegalArgumentException} in
	 * the case of a method argument, the javadoc for {@link NullPointerException} says,
	 * <code>"Applications should throw instances of this class..."</code>
	 * <p>
	 * Note that the function is just using: {@link Objects#requireNonNull(Object, String)}.
	 * 
	 * @param object 				The reference to validate (not null).
	 * @param message				The message to be thrown with the exception.
	 * @return		 				The object validated.
	 * @throws NullPointerException If the reference is null, with the specified message.
	 */
	public static <T> T nonNull(T object, String message) {
		return Objects.requireNonNull(object, message);
	}
	
	/**
	 * Validate a non-negative number as a method argument.
	 * 
	 * @param value	  					The value to validate (&ge;0)
	 * @throws IllegalArgumentException If the value is negative.
	 */
	public static <T extends Number> T nonNegative(T value) {
		if(value.intValue() < 0) {
			throw new IllegalArgumentException();
		}
		
		return value;
	}
	
	/**
	 * Validate a non-negative number as a method argument.
	 * 
	 * @param value	  					The value to validate (&ge;0)
	 * @param message 					The message to be thrown with the exception.
	 * @throws IllegalArgumentException If the value is negative, with the specified message.
	 */
	public static <T extends Number> T nonNegative(T value, String message) {
		if(value.intValue() < 0) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a non-null and non-empty string as a method argument.
	 * 
	 * @param value	  					The value to validate (not null or empty)
	 * @throws IllegalArgumentException If the value is null or empty.
	 */
	public static String nonEmpty(String value) {
		nonNull(value);
		
		if(value.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		return value;
	}
	
	/**
	 * Validate a non-null and non-empty string as a method argument.
	 * 
	 * @param value	  					The value to validate (not null or empty)
	 * @param message 					The message to be thrown with the exception.
	 * @throws IllegalArgumentException If the value is null or empty, with the specified message.
	 */
	public static String nonEmpty(String value, String message) {
		nonNull(value);
		
		if(value.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a non-null and non-empty array as a method argument.
	 * 
	 * @param value	  					The value to validate (not null or empty)
	 * @throws IllegalArgumentException If the value is null or empty.
	 */
	public static <T> T[] nonEmpty(T[] value) {
		nonNull(value);
		
		if(value.length == 0) {
			throw new IllegalArgumentException();
		}
		
		return value;
	}
	
	/**
	 * Validate a non-null and non-empty array as a method argument.
	 * 
	 * @param value	  					The value to validate (not null or empty)
	 * @param message 					The message to be thrown with the exception.
	 * @throws IllegalArgumentException If the value is null or empty, with the specified message.
	 */
	public static <T> T[] nonEmpty(T[] value, String message) {
		nonNull(value, message);
		
		if(value.length == 0) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a limited integer as a method argument.
	 * 
	 * @param value   The value to validate (&le;max, &ge;min).
	 * @param message The message to throw with the exception.
	 * @param min	  The smallest valid value (&le;max).
	 * @param max     The largest valid value (&ge;max).
	 * @throws IllegalArgumentException If the value is outside of the range [min, max].
	 */
	public static void inRange(int value, String message, int min, int max) {
		if(value < min) {
			throw new IllegalArgumentException(message);
		}
		
		if(value > max) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Validate a limited single-precision value as a method argument.
	 * 
	 * @param value   The value to validate (&le;max, &ge;min).
	 * @param message The message to throw with the exception.
	 * @param min	  The smallest valid value (&le;max).
	 * @param max     The largest valid value (&ge;max).
	 * @throws IllegalArgumentException If the value is outside of the range [min, max].
	 */
	public static void inRange(float value, String message, float min, float max) {
		if(!(value >= min)) {
			throw new IllegalArgumentException(message);
		}
		
		if(!(value <= max)) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Validate a limited double-precision value as a method argument.
	 * 
	 * @param value   The value to validate (&le;max, &ge;min).
	 * @param message The message to throw with the exception.
	 * @param min	  The smallest valid value (&le;max).
	 * @param max     The largest valid value (&ge;max).
	 * @throws IllegalArgumentException If the value is outside of the range [min, max].
	 */
	public static void inRange(double value, String message, double min, double max) {
		if(!(value >= min)) {
			throw new IllegalArgumentException(message);
		}
		
		if(!(value <= max)) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Validate a positive single-precision value as a method argument.
	 * 
	 * @param value   The value to validate (&gt;0).
	 * @throws IllegalArgumentException If the value isn't positive.
	 */
	public static void positive(float value) {
		if(!(value > 0f)) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Validate a positive integer value as a method argument.
	 * 
	 * @param value   The value to validate (&gt;0).
	 * @throws IllegalArgumentException If the value isn't positive.
	 */
	public static void positive(int value) {
		if(!(value > 0)) {
			throw new IllegalArgumentException();
		}
	}
}
