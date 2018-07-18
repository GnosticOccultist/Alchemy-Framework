package fr.alchemy.core.util;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class NanoTimer {
	/**
	 * A second in nanoseconds.
	 */
	public static final long SECOND = 1_000_000_000;
	/**
	 * A minute in nanoseconds.
	 */
	public static final long MINUTE = 60 * SECOND;
	/**
	 * The timer per single frame in nanoseconds.
	 */
	public static final long TIME_PER_FRAME = SECOND / 60;
	/**
	 * Holds the current tick (frame).
	 */
	private long tick = 0;
	/**
	 * Time for the tick in nanoseconds.
	 */
	private long now = 0;
	/**
	 * FPS counter to approximate FPS values.
	 */
	private FPSCounter fpsCounter = new FPSCounter();
	/**
	 * FPS counter to approximate FPS values.
	 */
	private FPSCounter fpsPerformanceCounter = new FPSCounter();
	/**
	 * Average render FPS.
	 */
	protected IntegerProperty fps = new SimpleIntegerProperty();
	/**
	 * Average performance FPS.
	 */
	protected IntegerProperty fpsPerformance = new SimpleIntegerProperty();
	/**
	 * Used as delta from internal JavaFX time-stamp to calculate renderFPS.
	 */
	protected long fpsTime = 0; 
	/**
	 * The starting nano value.
	 */
	private long startNanos = -1;
	/**
	 * The real FPS.
	 */
    private long realFPS = -1;
    
    /**
     * Called at the start of an update loop cycle.
     * The tick value is incremented at this moment.
     */
    public void tickStart(final long internalTime) {
    	tick++;
    	now = (getTick() - 1) * TIME_PER_FRAME;
    	startNanos = System.nanoTime();
    	realFPS = internalTime - fpsTime;
    	fpsTime = internalTime;
    }
    
    /**
     * Called at the end of an update loop cycle.
     */
    public void tickEnd() {
    	fpsPerformance.set(Math.round(fpsPerformanceCounter.count(SECOND / (System.nanoTime() - startNanos))));
		fps.set(Math.round(fpsCounter.count(SECOND / realFPS)));
    }
	
    /**
     * Resets the tick value to 0.
     */
	public void reset() {
		tick = 0;
	}
	
	/**
	 * Returns the current tick (frame). When the application has just started,
	 * the first cycle in the loop will have a tick value of 1, second of 2 etc.
	 * <p>
	 * The tick updates when a new cycle starts.
	 */
	public long getTick() {
		return tick;
	}
	
	/**
	 * Returns the current time for the tick in nanoseconds. It represents also
	 * the time elapsed from the start of the application.
	 * This time doesn't change while the application is paused or while within the
	 * same tick.
	 */
	public long getNow() {
		return now;
	}
	
	/**
	 * @return The performance FPS property.
	 */
	public IntegerProperty performanceFPSProperty() {
		return fpsPerformance;
	}
	
	/**
	 * @return The FPS property.
	 */
	public IntegerProperty fpsProperty() {
		return fps;
	}
}
