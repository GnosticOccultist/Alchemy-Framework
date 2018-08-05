package fr.alchemy.core.asset;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/**
 * <code>Music</code> is a wrapper class around a {@link MediaPlayer} which
 * can be used to play long sounds like background music in 'mp3' format.
 * 
 * @author GnosticOccultist
 */
public final class Music {
	/**
	 * The media player accessing the sound.
	 */
	private final MediaPlayer player;
	
	Music(final Media media) {
		this.player = new MediaPlayer(media);
	}
	
	/**
	 * Plays or resume the <code>Music</code> playback.
	 */
	public void play() {
		player.play();
	}
	
	/**
	 * Sets the number of times the <code>Music</code> needs to be played
	 * in a row. 
	 * 
	 * @param count The count of cycling music.
	 * @return      The updated music.
	 */
	public void setCycleCount(final int cycle) {
		player.setCycleCount(cycle);
	}
	
	/**
	 * Pauses the <code>Music</code> playback.
	 */
	public void pause() {
		player.pause();
	}
	
	/**
	 * Stops the <code>Music</code> playback.
	 */
	public void stop() {
		player.stop();
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
	public void cleanup() {
		if(getStatus() == Status.DISPOSED) {
			// Already disposed !
			return;
		}
		player.dispose();
	}
}
