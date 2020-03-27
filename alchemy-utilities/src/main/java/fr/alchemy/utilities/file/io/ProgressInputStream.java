package fr.alchemy.utilities.file.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Path;

import fr.alchemy.utilities.Validator;

/**
 * <code>ProgressInputStream</code> is an implementation of {@link InputStream} which allows the monitoring of reading 
 * progress when the number of bytes to be read can be easily determined, for example when using an {@link URLConnection}.
 * 
 * @author GnosticOccultist
 */
public class ProgressInputStream extends InputStream {

	/**
	 * The internal input stream to read bytes with.
	 */
	private final InputStream internal;
	/**
	 * The path of the file currently read.
	 */
	private final Path file;
	/**
	 * The total size of the content to read.
	 */
	private final int size;
	/**
	 * The count of bytes that has already been read.
	 */
	private int bytesRead;
	/**
	 * The listener of the loading progress.
	 */
	private ProgressListener listener;
	
	/**
	 * Instantiates a new <code>ProgressInputStream</code> with no {@link ProgressListener}.
	 * 
	 * @param internal The internal input stream to read bytes with (not null).
	 * @param file	   The path of the file currently read (not null).
	 * @param size	   The total size of the content to read (not null).
	 */
	public ProgressInputStream(InputStream internal, Path file, int size) {
		this(internal, file, size, null);
	}
	
	/**
	 * Instantiates a new <code>ProgressInputStream</code> with the provided {@link ProgressListener} to
	 * follow the progress of the given loaded file.
	 * 
	 * @param internal The internal input stream to read bytes with (not null).
	 * @param file	   The path of the file currently read (not null).
	 * @param size	   The total size of the content to read (not null).
	 * @param listener The listener to assign to the input stream, or null for none.
	 */
	public ProgressInputStream(InputStream internal, Path file, int size, ProgressListener listener) {
		Validator.nonNull(internal, "The internal input stream can't be null!");
		Validator.nonNull(file, "The path of the file can't be null!");
		Validator.nonNegative(size, "The size of an input stream can't be negative!");
		this.internal = internal;
		this.file = file;
		this.size = size;
		this.bytesRead = 0;
		this.listener = listener;
	}
	
	/**
	 * Assign a {@link ProgressListener} to this <code>ProgressInputStream</code>, or null
	 * to remove any previously set listener.
	 * 
	 * @param listener The listener to assign to the input stream, or null for none.
	 */
	public void listen(ProgressListener listener) {
		this.listener = listener;
	}
	
	@Override
	public int read() throws IOException {
		int b = internal.read();
		
		if(b != -1) {
			bytesRead++;
		}
		
		notifyListener();
		return b;
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		int read = internal.read(b);
        bytesRead += read;
        
        notifyListener();
        return read;
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int read = internal.read(b, off, len);
        bytesRead += read;
        
        notifyListener();
        return read;
	}
	
	@Override
	public int available() throws IOException {
		return size - bytesRead;
	}
	
	/**
	 * Compute and return the percentage of loaded content from this <code>ProgressInputStream</code>.
	 * 
	 * @return The progress percentage (&ge;0, &le;100)
	 */
	public double loadedPercentage() {
		return loadedRatio() * 100;
	}
	
	/**
	 * Compute and return the ratio of loaded content from this <code>ProgressInputStream</code>.
	 * 
	 * @return The progress ratio (&ge;0, &le;1)
	 */
	public double loadedRatio() {
		return bytesRead / size;
	}
	
	/**
	 * Notify the {@link ProgressListener} of the current loading progress.
	 */
	private void notifyListener() {
		listener.load(file, bytesRead, size);
	}
	
	/**
	 * <code>ProgressListener</code> is a functional interface to keep track of a {@link ProgressInputStream}'s progress.
	 * <p>
	 * The defined functional method {@link #load(Path, int, int)} will be called each time one or multiple bytes are being read
	 * from the {@link InputStream}.
	 * 
	 * @author GnosticOccultist
	 */
	@FunctionalInterface
	public interface ProgressListener {
		
		/**
		 * Invoked when the {@link ProgressInputStream} linked to this <code>ProgressListener</code> 
		 * has read one or multiple bytes.
		 * 
		 * @param file		The path of the file being read (not null).
		 * @param bytesRead The count of bytes that has already been read (&gt;0).
		 * @param size		The total size of the content to read (&gt;0).
		 */
		void load(Path file, int bytesRead, int size);
	}
}
