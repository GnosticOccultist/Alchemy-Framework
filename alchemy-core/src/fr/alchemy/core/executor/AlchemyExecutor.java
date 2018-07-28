package fr.alchemy.core.executor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.alchemy.core.AlchemyApplication;

public final class AlchemyExecutor {

	/**
	 * The executor manager logger.
	 */
	private Logger logger = LoggerFactory.getLogger("alchemy.executor");
	/**
	 * The table of all the task executors stored by type.
	 */
	private final Map<Class<? extends AlchemyTaskExecutor>, AlchemyTaskExecutor[]> executorsMap;
	/**
	 * The service to execute scheduled tasks.
	 */
	private final ScheduledExecutorService scheduledExecutorService;
	/**
	 * The number of available processors to the JVM.
	 */
	public static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();
	
	private static AlchemyExecutor instance;
	/**
	 * @return The single-instance of the <code>AlchemyExecutor</code>.
	 */
	public static AlchemyExecutor executor() {
		if(instance == null) {
			instance = new AlchemyExecutor();
		}
		return instance;
	}
	
	private AlchemyExecutor() {
		this.executorsMap = new HashMap<>();
		this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	}
	
	/**
	 * Perform the provided task with the specified type of <code>AlchemyTaskExecutor</code>.
	 * If multiple executors are registered under the same type, it will choose an unused executor
	 * in the array bounds.
	 * 
	 * @param type The type of task executor.
	 * @param task The task to execute.
	 */
	public void performTask(final Class<? extends AlchemyTaskExecutor> type, final Runnable task) {
		AlchemyTaskExecutor[] executors = executorsMap.get(type);
		if(executors.length > 1) {
			final int index = AlchemyTaskExecutor.acquireNextIndex();
			if (index < executors.length) {
	            executors[index].execute(task);
	        } else {
	            AlchemyTaskExecutor.resetIndex();
	            executors[0].execute(task);
	        }
		} else {
			executors[0].execute(task);
		}
	}
	
	/**
	 * Registers a new array of the provided type <code>AlchemyTaskExecutor</code> which is used to execute tasks.
	 * If you want to register a single executor use {@link #registerExecutor(AlchemyTaskExecutor)}.
	 * <p>
	 * If multiple executors are registered inside an array, when a task is executed with {@link #performTask(Class, Runnable)}
	 * it will choose an available executor.
	 * 
	 * @param type   The type of executor to register.
	 * @param number The number of executors to register.
	 */
	public void registerExecutors(final Class<? extends AlchemyTaskExecutor> type, final int number) {
		AlchemyTaskExecutor[] executors = new AlchemyTaskExecutor[number];
		for (int i = 0, length = executors.length; i < length; i++) {
			try {
				executors[i] = type.getConstructor(int.class).newInstance(i + 1);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(executors.length != 0) {
			this.executorsMap.put(type, executors);
			logger().info("Registered an executor array of type: " + type.getSimpleName() + " with a size of " + number + ".");
		}
	}
	
	/**
	 * Registers a new type of <code>AlchemyTaskExecutor</code> which is used to execute tasks.
	 * If you want to register an array of executor use {@link #registerExecutors(Class, int)}.
	 * 
	 * @param executor The executor to register.
	 */
	public void registerExecutor(final Class<? extends AlchemyTaskExecutor> type) {
		try {
			this.executorsMap.put(type, new AlchemyTaskExecutor[] {type.newInstance()});
			logger().info("Registered a new executor: " + type.getSimpleName() + ".");
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates and executes a one-shot action after the given delay (in milliseconds).
	 * 
	 * @param runnable The task to execute.
	 * @param timeout  The delay before the execution.
	 */
	public void schedule(final Runnable runnable, final long timeout) {
		scheduledExecutorService.schedule(runnable, timeout, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Executes periodically the given task, starting after the provided delay (in milliseconds).
	 * Each execution will be separated from this delay but it may be delayed if the executor encounter
	 * a problem.
	 * <p>
	 * If a fatal exception was raised, all the next tasks will be suppressed.
	 * 
	 * @param runnable The task to execute periodically.
	 * @param delay	   The delay of execution.
	 */
	public void scheduleAtFixedRate(final Runnable runnable, final long delay) {
		scheduledExecutorService.scheduleAtFixedRate(runnable, delay, delay, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Shutdown the <code>AlchemyExecutor</code> by stopping all task executor.
	 * This function is called in {@link AlchemyApplication#exit()} when closing the application.
	 */
	public void shutdown() {
		logger().info("Shutting down tasks executor device");
		scheduledExecutorService.shutdown();
		executorsMap.values().forEach(t -> Arrays.asList(t).forEach(AlchemyTaskExecutor::shutdown));
		executorsMap.clear();
	}
	
	/**
	 * @return The logger of the <code>AlchemyExecutor</code>.
	 */
	private Logger logger() {
		return logger;
	}
}	
