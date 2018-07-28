package fr.alchemy.core.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The interface for implementing your own tasks executor device.
 * 
 * @author GnosticOccultist
 */
public interface AlchemyTaskExecutor extends Executor {
	
	static AtomicInteger nextTaskExecutor = new AtomicInteger(0);
	
	/**
	 * Executes the specified task.
	 * 
	 * @param task The task to execute.
	 */
	@Override
	void execute(final Runnable task);
	
	/**
	 * Shutdown cleanly the task executor.
	 */
	void shutdown();
	
	/**
	 * Acquire the next task executor index.
	 *
	 * @return The next available index.
	 */
	static int acquireNextIndex() {
		return nextTaskExecutor.incrementAndGet();
	}
	
	/**
	 * Resets the index manager.
	 */
	static void resetIndex() {
		nextTaskExecutor.set(0);
	}
}
