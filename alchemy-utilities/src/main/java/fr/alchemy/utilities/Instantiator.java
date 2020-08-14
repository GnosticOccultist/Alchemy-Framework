package fr.alchemy.utilities;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;

import fr.alchemy.utilities.collections.array.ArrayUtil;

/**
 * <code>Instantiator</code> is a utility class designed to create an instance of a class or invoking a method using reflection easily
 * and without having to handle the exceptions.
 * 
 * @version 0.1.1
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
	 * Creates a new instance of the class with the given name using reflection. The class 
	 * must define a public or protected empty constructor.
	 * <p>
	 * The class can also be an {@link Enum} implementation, in this case the given arguments must either
	 * be the enum value, name or ordinal, in order for the method {@link #fromClassAsEnum(Class, Object)} 
	 * to return an enum constant.
	 * 
	 * @param <T> The type of object to instantiate.
	 * 
	 * @param className The name of the class to instantiate (not null, not empty).
	 * @return			A new instance of the class or null if an error occured.
	 * 
	 * @see #fromNameWith(String, Object...)
	 * @see #fromNameWith(String, Consumer, Object...)
	 * @see #fromClassAsEnum(Class, Object)
	 */
	public static <T> T fromName(String className) {
		return fromNameWith(className, null, ArrayUtil.EMPTY_OBJECT_ARRAY);
	}

	/**
	 * Creates a new instance of the class with the given name using reflection. The class 
	 * must define a public or protected constructor with either no arguments or all the arguments 
	 * of the given type in order to work correctly.
	 * <p>
	 * The class can also be an {@link Enum} implementation, in this case the given arguments must either
	 * be the enum value, name or ordinal, in order for the method {@link #fromClassAsEnum(Class, Object)} 
	 * to return an enum constant.
	 * 
	 * @param <T> The type of object to instantiate.
	 * 
	 * @param className The name of the class to instantiate (not null, not empty).
	 * @param args 		The arguments to use in the constructor, or null for none.
	 * @return			A new instance of the class or null if an error occured.
	 * 
	 * @see #fromName(String)
	 * @see #fromNameWith(String, Consumer, Object...)
	 * @see #fromClassAsEnum(Class, Object)
	 */
	public static <T> T fromNameWith(String className, Object... args) {
		return fromNameWith(className, null, args);
	}
	
	/**
	 * Creates a new instance of the class with the given name using reflection. The class 
	 * must define a public or protected constructor with either no arguments or all the arguments 
	 * of the given type in order to work correctly.
	 * <p>
	 * The class can also be an {@link Enum} implementation, in this case the given arguments must either
	 * be the enum value, name or ordinal, in order for the method {@link #fromClassAsEnum(Class, Object)} 
	 * to return an enum constant.
	 * <p>
	 * After the instance has been created the given {@link Consumer} is used on it and the 
	 * instance is returned.
	 * 
	 * @param <T> The type of object to instantiate.
	 * 
	 * @param className The name of the class to instantiate (not null, not empty).
	 * @param action    The action to perform on the new instance (not null).
	 * @param args 		The arguments to use in the constructor, or null for none.
	 * @return			A new instance of the class or null if an error occured.
	 * 
	 * @see #fromName(String)
	 * @see #fromNameWith(String, Object...)
	 * @see #fromNameImplements(String, Class, Consumer, Object...)
	 * @see #fromClassAsEnum(Class, Object)
	 */
	public static <T> T fromNameWith(String className, Consumer<T> action, Object... args) {
		return fromNameImplements(className, null, action, args);
	}
	
	/**
	 * Creates a new instance of the class with the given name using reflection. The class 
	 * must define a public or protected constructor with either no arguments or all the arguments 
	 * of the given type in order to work correctly.
	 * <p>
	 * The class can also be an {@link Enum} implementation, in this case the given arguments must either
	 * be the enum value, name or ordinal, in order for the method {@link #fromClassAsEnum(Class, Object)} 
	 * to return an enum constant.
	 * <p>
	 * After the instance has been created the given {@link Consumer} is used on it and the 
	 * instance is returned.
	 * 
	 * @param <T> The type of object to instantiate.
	 * 
	 * @param className The name of the class to instantiate (not null, not empty).
	 * @param action    The action to perform on the new instance (not null).
	 * @param type   	The type of class to implement or null for none.
	 * @param args 		The arguments to use in the constructor, or null for none.
	 * @return			A new instance of the class or null if an error occured.
	 * 
	 * @see #fromName(String)
	 * @see #fromNameWith(String, Object...)
	 * @see #fromClassAsEnum(Class, Object)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromNameImplements(String className, Class<T> type, Consumer<T> action, Object... args) {
		Validator.nonEmpty(className, "The class name can't be empty or null!");
		
		T obj = null;
		try {
			Class<?> clazz = Class.forName(className);
			if(type != null && !type.isAssignableFrom(clazz)) {
				throw new IllegalArgumentException("The class '" + clazz.getName() + 
						"' isn't implementing or extending '" + type.getName() + "'!");
			}
			
			if(Enum.class.isAssignableFrom(clazz) && args.length == 1) {
				return (T) fromClassAsEnum((Class<Enum<?>>) clazz, args[0]);
			}
			
			Constructor<?> constructor = null;
			if(args != null && args.length > 0) {
				List<Class<?>> argTypes = Arrays.asList(args).stream().map(Object::getClass).collect(Collectors.toList());
				constructor = clazz.getDeclaredConstructor(argTypes.toArray(new Class[argTypes.size()]));
				obj = (T) constructor.newInstance(args);
			} else {
				constructor = clazz.getDeclaredConstructor();
				obj = (T) constructor.newInstance();
			}
			
		} catch (ClassNotFoundException ex) {
			throw new IllegalArgumentException("Unable to find the class: '" + className + "' ! " + ex);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | 
				InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			throw new RuntimeException("Instantiation failed for the component '" + className + 
					"'! Make sure the constructor is empty. " + ex);
		}
		
		if(obj != null && action != null) {
			action.accept(obj);
		}
		
		return obj;		
	}
	
	/**
	 * Creates a new instance of the provided class using reflection. The class must define
	 * a public or protected empty constructor in order to work correctly.
	 * <p>
	 * The class can also be an {@link Enum} implementation, in this case the given arguments must either
	 * be the enum value, name or ordinal, in order for the method {@link #fromClassAsEnum(Class, Object)} 
	 * to return an enum constant.
	 * <p>
	 * After the instance has been created the given {@link Consumer} is used on it and the 
	 * instance is returned.
	 * 
	 * @param <T> The type of object to instantiate.
	 * 
	 * @param clazz  The class to instantiate (not null).
	 * @param action The action to perform on the new instance (not null).
	 * @return	     A new instance of the class or null if an error occured.
	 */
	public static <T> T fromClassAndApply(Class<T> clazz, Consumer<T> action) {
		T obj = fromClass(clazz);
		action.accept(obj);
		return obj;
	}
	
	/**
	 * Creates a new instance of the provided class using reflection. The class must define
	 * a public or protected empty constructor in order to work correctly.
	 * <p>
	 * The class can also be an {@link Enum} implementation, in this case the given arguments must either
	 * be the enum value, name or ordinal, in order for the method {@link #fromClassAsEnum(Class, Object)} 
	 * to return an enum constant.
	 * <p>
	 * After the instance has been created the given mapping {@link Function} is used on it
	 * and the result is returned.
	 * 
	 * @param <T> The type of object to instantiate.
	 * @param <C> The type of object that the mapping function is returning.
	 * 
	 * @param clazz The class to instantiate (not null).
	 * @param map	The mapping action to perform on the new instance (not null).
	 * @return	    A new instance of the class or null if an error occured.
	 */
	public static <T, C> C fromClass(Class<T> clazz, Function<? super T, ? extends C> map) {
		Validator.nonNull(map, "The mapping action can't be null!");
		
		T obj = fromClass(clazz);
		return map.apply(obj);
	}
	
	/**
	 * Creates a new instance of the provided class using reflection. The class must define
	 * a public or protected empty constructor in order to work correctly.
	 * <p>
	 * The class can also be an {@link Enum} implementation, in this case the given arguments must either
	 * be the enum value, name or ordinal, in order for the method {@link #fromClassAsEnum(Class, Object)} 
	 * to return an enum constant.
	 * 
	 * @param <T> The type of object to instantiate.
	 * 
	 * @param clazz The class to instantiate (not null).
	 * @return	    A new instance of the class or null if an error occured.
	 * 
	 * @see #fromClassAsEnum(Class, Object)
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromClass(Class<T> clazz, Object... args) {
		Validator.nonNull(clazz, "The class to instantiate can't be null!");
		
		T obj = null;
		try {
			
			if(Enum.class.isAssignableFrom(clazz) && args.length == 1) {
				return (T) fromClassAsEnum((Class<Enum<?>>) clazz, args[0]);
			}
			
			Constructor<T> constructor = null;
			if(args != null && args.length > 0) {
				List<Class<?>> argTypes = Arrays.asList(args).stream().map(Object::getClass).collect(Collectors.toList());
				constructor = clazz.getDeclaredConstructor(argTypes.toArray(new Class[argTypes.size()]));
			} else {
				constructor = clazz.getDeclaredConstructor();
			}
			
			if(Modifier.isProtected(constructor.getModifiers())) {
				/*
				 * Only access protected constructor not private ones for security. 
				 */
				constructor.setAccessible(true);
			}
			
			if(args != null && args.length > 0) {
				obj = (T) constructor.newInstance(args);
			} else {
				obj = (T) constructor.newInstance();
			}
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | 
				InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			System.err.println("Instantiation failed for the component '" + clazz.getName() + 
					"'! Make sure the constructor is empty.");
		}
		
		return obj;
	}
	
	/**
	 * Returns an {@link Enum} constant of the provided type using the provided argument. The argument
	 * must be either defining the enum constant directly, its name or its ordinal.
	 * 
	 * @param <T> The type of enum constant to instantiate.
	 * 
	 * @param enumType The class of enum to instantiate (not null).
	 * @param arg	   The argument to know what enum constant to return, either its value, 
	 * 				   name or ordinal (not null).
	 * @return	       An enum constant matching the provided argument.
	 * 
	 * @throws IllegalArgumentException Thrown if the argument isn't representing a valid enum 
	 * 									constant, name or ordinal.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum> T fromClassAsEnum(Class<T> enumType, Object arg) {
		final Enum[] enums = enumType.getEnumConstants();
		for(Enum e : enums) {
			// First case, the argument represent the enum constant.
			if(arg instanceof Enum) {
				Enum value = (Enum) arg;
				if(e.equals(value)) {
					return (T) e;
				}
			}
			// Second case, the argument represent the name of the enum constant.
			else if(arg instanceof String) {
				String name = (String) arg;
				if(e.name().equals(name)) {
					return (T) e;
				}
			} 
			// Third case, the argument represent the ordinal of the enum constant.
			else if(arg instanceof Integer) {
				Integer ordinal = (Integer) arg;
				if(e.ordinal() == ordinal) {
					return (T) e;
				}
			}
		}
		
		throw new IllegalArgumentException("The argument " + arg + " of type " + arg.getClass().getName() + 
				" must either represent the enum constant, the enum name or the enum ordinal!");
	}

	/**
	 * Creates a new instance with the provided {@link ClassLoader} and {@link JarEntry} and execute the given
	 * {@link Predicate} on the new instance before returning it.
	 * 
	 * @param <T> The type of object to instantiate.
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
	 * Invokes the {@link Method} with the specified name contained in the given class instance using the provided
	 * objects as arguments. The function also return whether the invokation has been successfully made. 
	 * 
	 * @param methodName	The name of the method to invoke which should be accessible (not null, not empty).
	 * @param classInstance The instance of the class containing the method (not null).
	 * @param args			The argument to use for the method or null for none.
	 * @return				Whether the invokation has been successfully made.
	 */
	public static boolean invokeMethod(String methodName, Object classInstance, Object... args) {
		Validator.nonEmpty(methodName, "The name of the method to invoke can't be null or empty!");
		Validator.nonNull(classInstance, "The class instance can't be null!");
		try {
			Method method = classInstance.getClass().getMethod(methodName, 
					Arrays.asList(args).stream().map(Object::getClass).toArray(i -> new Class[i]));
			return invokeMethod(method, classInstance, args);
		} catch (IllegalArgumentException | NoSuchMethodException | SecurityException ex) {
			System.err.println("Invokation of the method: " + methodName + " failed, for the specified class instance " 
					+ classInstance.getClass().getSimpleName() + " and for the argument " + Arrays.toString(args) + "!");
		}
		return false;
	}
	
	/**
	 * Invokes the specified {@link Method} contained in the given class instance using the provided
	 * objects as arguments. The function also return whether the invokation has been successfully made. 
	 * 
	 * @param method		The method to invoke which should be accessible (not null).
	 * @param classInstance The instance of the class containing the method (not null).
	 * @param args			The argument to use for the method or null for none.
	 * @return				Whether the invokation has been successfully made.
	 */
	public static boolean invokeMethod(Method method, Object classInstance, Object... args) {
		Validator.nonNull(method, "The method to invoke can't be null!");
		Validator.nonNull(classInstance, "The class instance can't be null!");
		try {
			method.invoke(classInstance, args);
			return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			System.err.println("Invokation of the method: " + method.getName() + " failed, for the specified class instance " 
					+ classInstance.getClass().getSimpleName() + " and for the argument " + Arrays.toString(args) + "!");
		}
		return false;
	}
}
