package fr.alchemy.core.asset;

import fr.alchemy.core.asset.cache.Asset;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

/**
 * <code>Music</code> is a wrapper class around a {@link MediaPlayer} which
 * can be used to play long sounds like background music in 'mp3' format.
 * 
 * @author GnosticOccultist
 */
public final class Music implements Asset {
	/**
	 * The media player accessing the sound.
	 */
	private final MediaPlayer player;
	
	Music(final Media media) {
		this(media, false);
	}
	
	Music(final Media media, final boolean autoPlay) {
		this.player = new MediaPlayer(media);
		this.player.setAutoPlay(autoPlay);
	}
	
	/**
	 * Plays or resume the <code>Music</code> playback.
	 */
	public void play() {
		this.player.play();
	}
	
	/**
	 * Pauses the <code>Music</code> playback.
	 */
	public void pause() {
		this.player.pause();
	}
	
	/**
	 * Stops the <code>Music</code> playback.
	 */
	public void stop() {
		this.player.stop();
	}
	
	/**
	 * Sets the starting and stopping time of the <code>Music</code>.
	 * 
	 * @param start The starting time.
	 * @param end   The stopping time.
	 * @return      The updated music.
	 */
	public Music setInterval(final Duration start, final Duration end) {
		this.player.setStartTime(start);
		this.player.setStopTime(end);
		return this;
	}
	
	/**
	 * Sets the number of times the <code>Music</code> needs to be played
	 * in a row. 
	 * 
	 * @param count The count of cycling music.
	 * @return      The updated music.
	 */
	public Music setCycleCount(final int cycle) {
		this.player.setCycleCount(cycle);
		return this;
	}
	
	/**
	 * Sets the speed at which the <code>Music</code> is played.
	 * 
	 * @param speed The music rate.
	 * @return      The updated music.
	 */
	public Music setSpeed(final double speed) {
		this.player.setRate(speed);
		return this;
	}
	
	/**
	 * Sets the volume of the <code>Music</code>.
	 * 
	 * @param volume The music volume.
	 * @return       The updated music.
	 */
	public Music setVolume(final double volume) {
		this.player.setVolume(volume);
		return this;
	}
	
	/**
	 * Sets whether the <code>Music</code> needs to be muted.
	 * Note that it as no effect on the volume and it will only stop the audio
	 * to be produced.
	 * 
	 * @param mute Whether to mute the music.
	 * @return	   The updated music.
	 */
	public Music mute(final boolean mute) {
		this.player.setMute(mute);
		return this;
	}
	
	/**
	 * @return The {@link Status} of the {@link MediaPlayer}.
	 */
	public Status getStatus() {
		return player.getStatus();
	}
	
	/**
	 * Cleanup the <code>Music</code> by disposing the {@link MediaPlayer}
	 */
	@Override
	public void cleanup() {
		if(getStatus() == Status.DISPOSED) {
			// Already disposed !
			return;
		}
		stop();
		player.dispose();
	}
}
