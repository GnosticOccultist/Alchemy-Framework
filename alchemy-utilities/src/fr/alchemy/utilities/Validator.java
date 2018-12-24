package fr.alchemy.utilities;

import java.util.Objects;

/**
 * <code>Validator</code> contains utility methods to throw exceptions for invalid arguments.
 * <p>
 * The methods are intended for checking arguments for <code>public</code>/<code>protected</code>
 * methods. For <code>private</code>/<code>package</code>, you should use assertions instead.
 * 
 * @author GnosticOccultist
 */
public final class Validator {
	
	/**
	 * The default message to use for <code>nonNull</code> validators.
	 */
	public static final String DEFAULT_NULL_MESSAGE = "The provided value can't be null!";
	/**
	 * The default message to use for <code>nonNegative</code> validators.
	 */
	public static final String DEFAULT_NEGATIVE_MESSAGE = "The provided value can't be negative (>=0)!";
	/**
	 * The default message to use for <code>nonEmpty</code> validators.
	 */
	public static final String DEFAULT_EMPTY_MESSAGE = "The provided value can't be empty!";
	/**
	 * The default message to use for <code>inRange</code> validators.
	 */
	public static final String DEFAULT_IN_RANGE_MESSAGE = "The provided value can't be outside of range!";
	/**
	 * The default message to use for <code>positive</code> validators.
	 */
	public static final String DEFAULT_POSITIVE_MESSAGE = "The provided value must be strictly positive (>0)!";
	
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
	 * @param object The reference to validate (not null).
	 * @return		 The object validated.
	 * 
	 * @throws NullPointerException If the reference is null.
	 */
	public static <T> T nonNull(T object) {
		return Objects.requireNonNull(object, DEFAULT_NULL_MESSAGE);
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
	 * @param object  The reference to validate (not null).
	 * @param message The message to be thrown with the exception.
	 * @return		  The object validated.
	 * 
	 * @throws NullPointerException If the reference is null, with the specified message.
	 */
	public static <T> T nonNull(T object, String message) {
		return Objects.requireNonNull(object, message);
	}
	
	/**
	 * Validate a non-negative number as a method argument.
	 * 
	 * @param value	The value to validate (&ge;0).
	 * @return 		The number value validated.
	 * 
	 * @throws IllegalArgumentException If the value is negative.
	 */
	public static <T extends Number> T nonNegative(T value) {
		return nonNegative(value, DEFAULT_NEGATIVE_MESSAGE);
	}
	
	/**
	 * Validate a non-negative number as a method argument.
	 * 
	 * @param value	  The value to validate (&ge;0).
	 * @param message The message to be thrown with the exception.
	 * @return 		  The number value validated.
	 * 
	 * @throws IllegalArgumentException If the value is negative, with the specified message.
	 */
	public static <T extends Number> T nonNegative(T value, String message) {
		if(value.intValue() < 0) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a non-null and non-empty {@link String} as a method argument.
	 * 
	 * @param value	The string value to validate (not null, not empty).
	 * @return		The string value validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty.
	 */
	public static String nonEmpty(String value) {
		return nonEmpty(value, DEFAULT_EMPTY_MESSAGE);
	}
	
	/**
	 * Validate a non-null and non-empty {@link String} as a method argument.
	 * 
	 * @param value	  The string value to validate (not null, not empty).
	 * @param message The message to be thrown with the exception.
	 * @return		  The string value validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty, with the specified message.
	 */
	public static String nonEmpty(String value, String message) {
		nonNull(value, DEFAULT_NULL_MESSAGE);
		
		if(value.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a non-null and non-empty object array as a method argument.
	 * 
	 * @param value	The object array to validate (not null, not empty).
	 * @return      The object array validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty.
	 */
	public static <T> T[] nonEmpty(T[] value) {
		return nonEmpty(value, DEFAULT_EMPTY_MESSAGE);
	}
	
	/**
	 * Validate a non-null and non-empty object array as a method argument.
	 * 
	 * @param value	  The object array to validate (not null, not empty).
	 * @param message The message to be thrown with the exception.
	 * @return 		  The object array validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty, with the specified message.
	 */
	public static <T> T[] nonEmpty(T[] value, String message) {
		nonNull(value, DEFAULT_NULL_MESSAGE);
		
		if(value.length == 0) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a non-null and non-empty single-precision array as a method argument.
	 * 
	 * @param value	The single-precision array to validate (not null, not empty).
	 * @return 		The single-precision array validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty.
	 */
	public static float[] nonEmpty(float[] value) {
		return nonEmpty(value, DEFAULT_EMPTY_MESSAGE);
	}
	
	/**
	 * Validate a non-null and non-empty single-precision array as a method argument.
	 * 
	 * @param value	  The single-precision array to validate (not null, not empty).
	 * @param message The message to be thrown with the exception.
	 * @return 		  The single-precision array validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty, with the specified message.
	 */
	public static float[] nonEmpty(float[] value, String message) {
		nonNull(value, DEFAULT_NULL_MESSAGE);
		
		if(value.length == 0) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a non-null and non-empty integer array as a method argument.
	 * 
	 * @param value	The integer array to validate (not null, not empty).
	 * @return 		The integer array validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty.
	 */
	public static int[] nonEmpty(int[] value) {
		return nonEmpty(value, DEFAULT_EMPTY_MESSAGE);
	}
	
	/**
	 * Validate a non-null and non-empty integer array as a method argument.
	 * 
	 * @param value	  The integer array to validate (not null, not empty).
	 * @param message The message to be thrown with the exception.
	 * @return 		  The integer array validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty, with the specified message.
	 */
	public static int[] nonEmpty(int[] value, String message) {
		nonNull(value, DEFAULT_NULL_MESSAGE);
		
		if(value.length == 0) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a non-null and non-empty double-precision array as a method argument.
	 * 
	 * @param value	The double-precision array to validate (not null, not empty).
	 * @return 		The double-precision array validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty.
	 */
	public static double[] nonEmpty(double[] value) {
		return nonEmpty(value, DEFAULT_EMPTY_MESSAGE);
	}
	
	/**
	 * Validate a non-null and non-empty double-precision array as a method argument.
	 * 
	 * @param value	  The double-precision array to validate (not null, not empty).
	 * @param message The message to be thrown with the exception.
	 * @return 		  The double-precision array validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty, with the specified message.
	 */
	public static double[] nonEmpty(double[] value, String message) {
		nonNull(value, DEFAULT_NULL_MESSAGE);
		
		if(value.length == 0) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a non-null and non-empty long array as a method argument.
	 * 
	 * @param value	The long array to validate (not null, not empty).
	 * @return 		The long array validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty.
	 */
	public static long[] nonEmpty(long[] value) {
		return nonEmpty(value, DEFAULT_EMPTY_MESSAGE);
	}
	
	/**
	 * Validate a non-null and non-empty long array as a method argument.
	 * 
	 * @param value	  The long array to validate (not null, not empty).
	 * @param message The message to be thrown with the exception.
	 * @return 		  The long array validated.
	 * 
	 * @throws IllegalArgumentException If the value is null or empty, with the specified message.
	 */
	public static long[] nonEmpty(long[] value, String message) {
		nonNull(value, DEFAULT_NULL_MESSAGE);
		
		if(value.length == 0) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a limited integer as a method argument.
	 * 
	 * @param value The value to validate (&le;max, &ge;min).
	 * @param min	The smallest valid value (&le;max).
	 * @param max   The largest valid value (&ge;max).
	 * @return 		The integer value validated.
	 * 
	 * @throws IllegalArgumentException If the value is outside of the range [min, max].
	 */
	public static int inRange(int value, int min, int max) {
		return inRange(value, DEFAULT_IN_RANGE_MESSAGE, min, max);
	}
	
	/**
	 * Validate a limited integer as a method argument.
	 * 
	 * @param value   The value to validate (&le;max, &ge;min).
	 * @param message The message to throw with the exception.
	 * @param min	  The smallest valid value (&le;max).
	 * @param max     The largest valid value (&ge;max).
	 * @return 		  The integer value validated.
	 * 
	 * @throws IllegalArgumentException If the value is outside of the range [min, max].
	 */
	public static int inRange(int value, String message, int min, int max) {
		if(value < min) {
			throw new IllegalArgumentException(message);
		}
		
		if(value > max) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a limited single-precision value as a method argument.
	 * 
	 * @param value The value to validate (&le;max, &ge;min).
	 * @param min	The smallest valid value (&le;max).
	 * @param max   The largest valid value (&ge;max).
	 * @return 		The single-precision value validated.
	 * 
	 * @throws IllegalArgumentException If the value is outside of the range [min, max].
	 */
	public static float inRange(float value, float min, float max) {
		return inRange(value, DEFAULT_IN_RANGE_MESSAGE, min, max);
	}
	
	/**
	 * Validate a limited single-precision value as a method argument.
	 * 
	 * @param value   The value to validate (&le;max, &ge;min).
	 * @param message The message to throw with the exception.
	 * @param min	  The smallest valid value (&le;max).
	 * @param max     The largest valid value (&ge;max).
	 * @return 		  The single-precision value validated.
	 * 
	 * @throws IllegalArgumentException If the value is outside of the range [min, max].
	 */
	public static float inRange(float value, String message, float min, float max) {
		if(!(value >= min)) {
			throw new IllegalArgumentException(message);
		}
		
		if(!(value <= max)) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a limited double-precision value as a method argument.
	 * 
	 * @param value The value to validate (&le;max, &ge;min).
	 * @param min	The smallest valid value (&le;max).
	 * @param max   The largest valid value (&ge;max).
	 * @return		The double-precision value validated.
	 * 
	 * @throws IllegalArgumentException If the value is outside of the range [min, max].
	 */
	public static double inRange(double value, double min, double max) {
		return inRange(value, DEFAULT_IN_RANGE_MESSAGE, min, max);
	}
	
	/**
	 * Validate a limited double-precision value as a method argument.
	 * 
	 * @param value   The value to validate (&le;max, &ge;min).
	 * @param message The message to throw with the exception.
	 * @param min	  The smallest valid value (&le;max).
	 * @param max     The largest valid value (&ge;max).
	 * @return		  The double-precision value validated.
	 * 
	 * @throws IllegalArgumentException If the value is outside of the range [min, max].
	 */
	public static double inRange(double value, String message, double min, double max) {
		if(!(value >= min)) {
			throw new IllegalArgumentException(message);
		}
		
		if(!(value <= max)) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a positive single-precision value as a method argument.
	 * 
	 * @param value The value to validate (&gt;0).
	 * @return		The single-precision value validated.
	 * 
	 * @throws IllegalArgumentException If the value isn't positive.
	 */
	public static float positive(float value) {
		return positive(value, DEFAULT_POSITIVE_MESSAGE);
	}
	
	/**
	 * Validate a positive single-precision value as a method argument.
	 * 
	 * @param value   The value to validate (&gt;0).
	 * @param message The message to be thrown with the exception.
	 * @return		  The single-precision value validated.
	 * 
	 * @throws IllegalArgumentException If the value isn't positive, with the specified message.
	 */
	public static float positive(float value, String message) {
		if(!(value > 0f)) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
	
	/**
	 * Validate a positive integer value as a method argument.
	 * 
	 * @param value The value to validate (&gt;0).
	 * @return		The integer value validated.
	 * 
	 * @throws IllegalArgumentException If the value isn't positive.
	 */
	public static int positive(int value) {
		return positive(value, DEFAULT_POSITIVE_MESSAGE);
	}
	
	/**
	 * Validate a positive integer value as a method argument.
	 * 
	 * @param value   The value to validate (&gt;0).
	 * @param message The message to be thrown with the exception.
	 * @return		  The integer value validated.
	 * 
	 * @throws IllegalArgumentException If the value isn't positive, with the specified message.
	 */
	public static int positive(int value, String message) {
		if(!(value > 0)) {
			throw new IllegalArgumentException(message);
		}
		
		return value;
	}
}
