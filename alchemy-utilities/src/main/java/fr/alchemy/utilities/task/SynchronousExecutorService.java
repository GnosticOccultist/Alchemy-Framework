package fr.alchemy.utilities.task;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <code>SynchronousExecutorService</code> is an implementation of a synchronous {@link AbstractExecutorService} which always
 * invoke the {@link Runnable} in the caller {@link Thread}, with no thread pool involved.
 * <p>
 * There is no task queue and no thread pool. The task will thus always be executed by the caller thread in a fully synchronous
 * method invocation.
 * <p>
 * This implementation is very simple and doesn't support waiting for tasks to complete during shutdown.
 * 
 * @version 0.1.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public class SynchronousExecutorService extends AbstractExecutorService {

	/**
	 * Whether the service is shutdown.
	 */
	private volatile boolean shutdown;
	
	@Override
	public void shutdown() {
		this.shutdown = true;
	}

	@Override
	public List<Runnable> shutdownNow() {
		shutdown();
		return Collections.emptyList();
	}

	@Override
	public boolean isShutdown() {
		return shutdown;
	}

	@Override
	public boolean isTerminated() {
		return shutdown;
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return true;
	}

	@Override
	public void execute(Runnable runnable) {
		if(!shutdown) {
			runnable.run();
		}
	}
}
