package fr.alchemy.utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

/**
 * <code>Instantiator</code> is a utility class designed to create an 
 * instance of a class easily and without handling the exceptions.
 * 
 * @author GnosticOccultist
 */
public class Instantiator {
	
	/**
	 * Creates a new instance with the provided class name.
	 * <p>
	 * The class name cannot be empty or null.
	 * 
	 * @param className The name to create the class from.
	 * @return			A new instance of the class or null if an error occured.
	 */
	public static <T> T fromName(String className) {
		return fromNameWith(className, null, null);
	}

	/**
	 * Creates a new instance with the provided class name.
	 * <p>
	 * The class name cannot be empty or null.
	 * 
	 * @param className The name to create the class from.
	 * @return			A new instance of the class or null if an error occured.
	 */
	public static <T> T fromNameWith(String className, Object arg) {
		return fromNameWith(className, null, arg);
	}
	
	/**
	 * Creates a new instance with the provided class name and performs the provided
	 * action with it.
	 * <p>
	 * The class name cannot be empty or null.
	 * 
	 * @param className The name to create the class from.
	 * @param action    The action to perform on the new instance.
	 * @return			A new instance of the class or null if an error occured.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromNameWith(String className, Consumer<T> action, Object arg) {
		Validator.nonEmpty(className);
		
		T obj = null;
		try {
			Class<?> clazz = Class.forName(className);
			if(arg != null) {
				obj = (T) clazz.getConstructor(arg.getClass()).newInstance(arg);
			} else {
				obj = (T) clazz.getConstructor().newInstance();
			}
			
		} catch (ClassNotFoundException e) {
			System.err.println("Unable to find the class: '" + className + "' !");
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | 
				InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			System.err.println("Instantiation failed for the component '" + className + 
					"'! Make sure the constructor is empty.");
		}
		
		if(obj != null && action != null) {
			action.accept(obj);
		}
		return obj;		
	}
	
	/**
	 * Creates a new instance with the provided class.
	 * <p>
	 * The class cannot be null.
	 * 
	 * @param clazz The class to create the instance from.
	 * @return	    A new instance of the class or null if an error occured.
	 */
	public static <T> T fromClass(Class<T> clazz) {
		return fromClass(clazz, null);
	}
	
	/**
	 * Creates a new instance with the provided class and performs the provided
	 * action with it.
	 * <p>
	 * The class cannot be null.
	 * 
	 * @param clazz  The class to create the instance from.
	 * @param action The action to perform on the new instance.
	 * @return	     A new instance of the class or null if an error occured.
	 */
	public static <T> T fromClass(Class<T> clazz, Consumer<T> action) {
		Validator.nonNull(clazz);
		
		T obj = null;
		try {			
			obj = (T) clazz.getConstructor().newInstance();
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | 
				InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			System.err.println("Instantiation failed for the component '" + clazz.getName() + 
					"'! Make sure the constructor is empty.");
		}
		
		if(obj != null && action != null) {
			action.accept(obj);
		}
		return obj;		
	}
}
