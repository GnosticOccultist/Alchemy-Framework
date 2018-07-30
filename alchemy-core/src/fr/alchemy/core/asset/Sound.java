package fr.alchemy.core.asset;

import javafx.scene.media.AudioClip;

/**
 * <code>Sound</code> is a wrapper class around an {@link AudioClip} which can be used
 * to play small sound effects in 'wav' format.
 * 
 * @author GnosticOccultist
 */
public final class Sound {
	/**
	 * The audio accessing the sound.
	 */
	private final AudioClip audio;
	
	Sound(final AudioClip audio) {
		this.audio = audio;
	}
	
	/**
	 * Sets the speed at which the <code>Sound</code> is played.
	 * 
	 * @param speed The sound rate.
	 * @return      The updated sound.
	 */
	public Sound setSpeed(final double speed) {
		this.audio.setRate(speed);
		return this;
	}
	
	/**
	 * Sets the volume of the <code>Sound</code>.
	 * 
	 * @param volume The sound volume.
	 * @return       The updated sound.
	 */
	public Sound setVolume(final double volume) {
		this.audio.setVolume(volume);
		return this;
	}
	
	/**
	 * Sets the number of times the <code>Sounds</code> needs to be played
	 * in a row. 
	 * 
	 * @param count The count of cycling sound.
	 * @return      The updated sound.
	 */
	public Sound setCycleCount(final int count) {
		this.audio.setCycleCount(count);
		return this;
	}
	
	/**
	 * Plays the sound effect, call {@link #stop()} to immediately stop all 
	 * playback of the <code>Sound</code>.
	 */
	public void play() {
		this.audio.play();
	}
	
	/**
	 * Stops immediately all playback of the <code>Sound</code>.
	 */
	public void stop() {
		this.audio.stop();
	}
}
