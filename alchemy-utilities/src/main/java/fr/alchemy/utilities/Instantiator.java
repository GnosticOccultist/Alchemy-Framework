package fr.alchemy.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;

/**
 * <code>Instantiator</code> is a utility class designed to create an instance of a class easily and without handling the exceptions.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public final class Instantiator {
	
	/**
	 * Private constructor to inhibit instantiation of <code>Instantiator</code>.
	 */
	private Instantiator() {}
	
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
	 * Creates a new instance with the provided class and performs the provided
	 * action with it.
	 * <p>
	 * The class cannot be null.
	 * 
	 * @param clazz  The class to create the instance from.
	 * @param action The action to perform on the new instance.
	 * @return	     A new instance of the class or null if an error occured.
	 */
	public static <T> T fromClassAndApply(Class<T> clazz, Consumer<T> action) {
		T obj = fromClass(clazz);
		action.accept(obj);
		return obj;
	}
	
	/**
	 * Creates a new instance with the provided class and performs the provided
	 * action with it.
	 * <p>
	 * The class cannot be null.
	 * 
	 * @param clazz  The class to create the instance from.
	 * @param map	 The action to perform on the new instance.
	 * @return	     A new instance of the class or null if an error occured.
	 */
	public static <T, C> C fromClass(Class<T> clazz, Function<? super T, ? extends C> map) {
		Validator.nonNull(map);
		
		T obj = fromClass(clazz);
		return map.apply(obj);
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
		Validator.nonNull(clazz);
		
		T obj = null;
		try {			
			obj = (T) clazz.getConstructor().newInstance();
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | 
				InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			System.err.println("Instantiation failed for the component '" + clazz.getName() + 
					"'! Make sure the constructor is empty.");
		}
		
		return obj;
	}
	
	/**
	 * Creates a new instance with the provided {@link ClassLoader} and {@link JarEntry} and execute the given
	 * {@link Predicate} on the new instance before returning it.
	 * 
	 * @param loader The class loader to load the class from a name (not null).
	 * @param entry  The JAR entry corresponding to the class file to instantiate.
	 * @param test	 The predicate to execute on the new instance.
	 * @return	     A new instance of the class corresponding to the JAR entry or null if failed.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJarEntry(ClassLoader loader, JarEntry entry, Predicate<Class<?>> test) {
		Validator.nonNull(entry, "The JAR entry to load class from can't be null!");
		Validator.check(entry.getName().endsWith(".class"), "The JAR entry must be a class file!");
		
		String translatedName = asReadableClassName(entry.getName());
		try {
			Class<?> clazz = loader.loadClass(translatedName);
			if(test.test(clazz)) {
				return (T) Instantiator.fromClass(clazz);
			}
		} catch (ClassNotFoundException ex) {
			System.err.println("Instantiation failed, impossible to found class '" + translatedName + "'!");
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Return a readable class name using the given initial name.
	 * 
	 * @param name The name of the class to translate (not null, not empty).
	 * @return	   A readable class name which can be used to instantiate a class.
	 */
	private static String asReadableClassName(String name) {
		Validator.nonEmpty(name, "The class name can't be null!");
		StringBuilder result = new StringBuilder(name.length() - ".class".length());
		for(int i = 0, length = name.length() - ".class".length(); i < length; i++) {
			char ch = name.charAt(i);
			if(ch == '/' || ch == '\\') {
				ch = '.';
			}
			result.append(ch);
		}

		return result.toString();
	}
	
	/**
	 * Invokes the specified {@link Method} contained in the given class instance using
	 * the argument. The function also return whether the invokation has been successfully made. 
	 * 
	 * @param method		The method to invoke (should be accessible).
	 * @param classInstance The instance of the class containing the method.
	 * @param arg			The argument to use for the method.
	 * @return				Whether the invokation has been successfully made.
	 */
	public static boolean invokeMethod(Method method, Object classInstance, Object arg) {
		try {
			method.invoke(classInstance, arg);
			return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			System.err.println("Invokation of the method: " + method.getName() + " failed, for the specified class instance " 
					+ classInstance.getClass().getSimpleName() + " and for the argument " + arg + "!");
		}
		return false;
	}
}
