package fr.alchemy.core;

import java.util.Arrays;

/**
 * Convenience class that buffers FPS values and calculates 
 * the arithmetic mean to approximate FPS value as it 
 * varies from frame to frame.
 * 
 * @author GnosticOccultist
 */
final class FPSCounter {
	
	private static final int MAX_SAMPLES = 100;
	
	private final float[] values = new float[MAX_SAMPLES];
	private float sum = 0.0f;
	private int index = 0;
	
	public FPSCounter() {
		Arrays.fill(values, 0.0f);
	}
	
	/**
	 * Calculates the average FPS and buffers given value
	 * for future corrections to the FPS value.
	 * 
	 * @param tpf The time that took the last frame.
	 */
	public float count(float tpf) {
		sum -= values[index];
		sum += tpf;
		values[index] = tpf;
		if(++index == values.length) {
			index = 0;
		}
		
		return sum / values.length;
	}
}
