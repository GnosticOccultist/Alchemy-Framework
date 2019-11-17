package fr.alchemy.utilities.concurrent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

public class TaskExecutor implements Executor {
	
	private final Queue<Runnable> tasks = new ConcurrentLinkedQueue<Runnable>();
	
	public void execute() {
		Runnable task = null;
		while ((task = tasks.poll()) != null) {
			task.run();
		}
	}
	
	@Override
	public void execute(Runnable command) {
		tasks.add(command);
	}
}
