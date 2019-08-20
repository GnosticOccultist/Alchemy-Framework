package fr.alchemy.utilities.concurrent;

import fr.alchemy.utilities.Validator;

/**
 * <code>AtomicTime</code> is a wrapper class containing time information which can be accessed
 * on different {@link Thread}.
 * The time used is in nanoseconds, but the computed time per frame is in seconds.
 * 
 * @author GnosticOccultist
 */
public class AtomicTime {
	
	/**
	 * The conversion factor to go from nanoseconds to seconds.
	 */
	public static final double NANOS_TO_SECONDS = 1.0 / 1_000_000_000.0;

	/**
	 * The current count of frames.
	 */
	private long frame;
	/**
	 * The current time.
	 */
	private long time;
	/**
	 * The reference starting time.
	 */
	private long referenceTime;
	/**
	 * The time per frame.
	 */
	private double tpf;
	
	/**
	 * Instantiates a new <code>AtomicTime</code>.
	 */
	public AtomicTime() {}
	
	/**
	 * Update the <code>AtomicTime</code> using the provided time value in nanoseconds, generally provided 
	 * with {@link System#nanoTime()}. If the method is called for the first time it will set the given 
	 * time as the starting reference.
	 * <p>
	 * The method updates the time per frame in seconds as well as the total count of frame since the start.
	 * 
	 * @param realTime The real time in nanoseconds (&ge;0).
	 */
	public void update(long realTime) {
		Validator.nonNegative(realTime, "The real time can't be negative!");
		
		// Set our time reference so we know when we started.
		if(frame == 0) {
			this.referenceTime = realTime;
		}
		// Get the delta time that has passed since the start.
		realTime -= referenceTime;
		// Increase the current frame count.
		frame++;
		// Calculate the elapsed time per frame in seconds.
		tpf = (realTime - time) * NANOS_TO_SECONDS;
		// Update the time value for the next cycle.
		time = realTime;
	}
	
	/**
	 * Return the time in nanoseconds of the <code>AtomicTime</code>.
	 * 
	 * @return The time in nanoseconds (&ge;0).
	 */
	public long getTime() {
		return time;
	}
	
	/**
	 * Return the time in seconds of the <code>AtomicTime</code>.
	 * 
	 * @return The time in seconds (&ge;0).
	 */
	public double timeInSeconds() {
		return time * NANOS_TO_SECONDS;
	}
	
	/**
	 * Return the time per frame of the <code>AtomicTime</code>.
	 * 
	 * @return The time per frame in seconds (&ge;0).
	 */
	public double timePerFrame() {
		return tpf;
	}
	
	/**
	 * Return the count of frames since the start of the <code>AtomicTime</code>, or
	 * zero if it hasn't been {@link #update(long) updated} yet.
	 * 
	 * @return The count of frames (&ge;0).
	 */
	public long frameCount() {
		return frame;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "tpf= " + tpf + ", seconds= " + timeInSeconds();
	}
}
