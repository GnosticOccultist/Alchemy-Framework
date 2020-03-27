package fr.alchemy.utilities.logging;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import fr.alchemy.utilities.Instantiator;
import fr.alchemy.utilities.Validator;

/**
 * <code>FactoryLogger</code> is a utility class to manage and store each instantiated {@link Logger}.
 * <p>
 * By default the factory uses the {@link AlchemyLogger} as its base implementation but you can change it 
 * by calling {@link #changeToType(Class)} to your own logger implementation if you have designed one.
 * <p>
 * A logger can be registered or retrieved at any time using {@link #getLogger(Class)} or {@link #getLogger(String)},
 * and then be used to write something in the console using the printing function, by default this is <code>System.err::println</code> 
 * but this can be changed with {@link #changePrinter(Consumer)}.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public final class FactoryLogger {
	
	/**	
	 * The table with all created loggers organized with their names.
	 */
	private static final Map<String, Logger> LOGGERS = 
			Collections.synchronizedMap(new HashMap<String, Logger>());
	/**
	 * The current logger type (default&rarr;AlchemyLogger).
	 */
	private static Class<? extends Logger> loggerType = 
			AlchemyLogger.class;
	/**
	 * The function to print the message (default&rarr;System.err::println).
	 */
	private static Consumer<String> printer = 
			System.err::println;
	
	/**
	 * The date format of the logging device (default&rarr;"HH:mm:ss:SSS").
	 */
	private static final DateTimeFormatter TIME_FORMATTER = 
			DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
	
	/**
	 * Changes the type of {@link Logger} used by the {@link FactoryLogger}.
	 * <p>
	 * Invoking this method will result in destroying all previously created logger if the implementation 
	 * to use differs from the old one.
	 * 
	 * @param loggerType The logger implementation to use (not null).
	 */
	public static void changeToType(Class<? extends Logger> loggerType) {
		Validator.nonNull(loggerType, "The logger type can't be null!");
		if(loggerType != FactoryLogger.loggerType) {
			LOGGERS.clear();
			FactoryLogger.loggerType = loggerType;
		}
	}
	
	/**
	 * Changes the print function for each {@link Logger}.
	 * 
	 * @param printer The printer function (not null).
	 */
	public static void changePrinter(Consumer<String> printer) {
		Validator.nonNull(printer, "The printing function can't be null!");
		FactoryLogger.printer = printer;
	}
	
	/**
	 * Writes the provided message at the specified {@link LoggerLevel} and with the given name 
	 * using the registered printer (by default: <code>System.err.println</code>).
	 * 
	 * @param level   The level to print the message at (not null).
	 * @param name	  The name to print with.
	 * @param message The message to print to the console.
	 */
	public static void print(LoggerLevel level, String name, String message) {
		Validator.nonNull(level, "The logger level can't be null!");

		String date = TIME_FORMATTER.format(LocalTime.now());
		String result = level.name() + ' ' + date + " [" + name + "]: " + message;

		print(result);
	}
	
	/**
	 * Writes the provided message at the specified {@link LoggerLevel} using 
	 * the registered printer (by default: <code>System.err.println</code>).
	 * 
	 * @param entireMessage The message to print as is.
	 */
    private static void print(String entireMessage) {
    	printer.accept(entireMessage);
    }
	
	/**
	 * Returns the {@link Logger} for the provided class or instantiate a new one if it doesn't
	 * exist yet. 
	 * 
	 * @param clazz The class to get the logger for (not null).
	 * @return		The logger corresponding to the class or a new one (not null).
	 */
	public static Logger getLogger(Class<?> clazz) {
		Validator.nonNull(clazz, "The class to get the logger can't be null!");
		return Validator.nonNull(LOGGERS.computeIfAbsent(clazz.getSimpleName(), FactoryLogger::createLogger));
	}
	
	/**
	 * Returns the {@link Logger} with the provided name or instantiate a new one if it doesn't
	 * exist yet. 
	 * 
	 * @param name The name of the logger to get (not null, not empty).
	 * @return	   The logger corresponding to the name or a new one (not null).
	 */
    public static Logger getLogger(String name) {
    	Validator.nonEmpty(name, "The name of the logger can't be null!");
        return Validator.nonNull(LOGGERS.computeIfAbsent(name, FactoryLogger::createLogger));
    }
	
	/**
	 * Creates a new {@link Logger} instance with the provided name.
	 * 
	 * @param name The name of the logger to create (not null).
	 * @return	   The newly created logger, or null if instantiation failed.
	 */
    private static Logger createLogger(String name) {

        Logger logger = Instantiator.fromClass(loggerType);
        logger.setName(name);
        
        return logger;
    }
}
