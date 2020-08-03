package fr.alchemy.utilities.file.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import fr.alchemy.utilities.Validator;

/**
 * <code>FastBufferedWriter</code> is an unsynchronized and fast implementation of {@link Writer} which is designed
 * as a replacement to {@link BufferedWriter} for higher performance.
 * 
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @author GnosticOccultist
 */
public final class FastBufferedWriter extends Writer {

	/**
	 * The default size of the character buffer.
	 */
	private static final int DEFAULT_CHAR_BUFFER_SIZE = 8192;
	
	/**
	 * The wrapped writer instance.
	 */
	private Writer wrapped;
	/**
	 * The character buffer.
	 */
	private char[] buffer;
	/**
	 * The current position in the buffer.
	 */
	private int pos;
	
	public FastBufferedWriter(Writer wrapped) {
		this(wrapped, DEFAULT_CHAR_BUFFER_SIZE);
	}
	
	public FastBufferedWriter(Writer wrapped, int size) {
		Validator.nonNull(wrapped, "The wrapped writer can't be null!");
		Validator.positive(size, "The size of the buffer must be strictly positive!");
		
		this.wrapped = wrapped;
		this.buffer = new char[size];
	}
	
	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		if(pos + len >= buffer.length) {
			flushBuffer();
		}
		
		if(len >= buffer.length) {
			/*
			 * If the requested length exceeds the size of the output buffer, write 
			 * the data directly. This way way buffered streams will cascade harmlessly.
			 */
			wrapped.write(cbuf, off, len);
		} else {
			System.arraycopy(cbuf, off, buffer, pos, len);
			this.pos += len;
		}
	}
	
	@Override
	public void write(String str, int off, int len) throws IOException {
		if(pos + len >= buffer.length) {
			flushBuffer();
		}
		
		if(len >= buffer.length) {
			/*
			 * If the requested length exceeds the size of the output buffer, write 
			 * the data directly. This way way buffered streams will cascade harmlessly.
			 */
			wrapped.write(str, off, len);
		} else {
			str.getChars(off, off + len, buffer, pos);
			this.pos += len;
		}
	}

	@Override
	public void write(int c) throws IOException {
		if(pos == buffer.length) {
			flushBuffer();
		}
		
		this.buffer[pos++] = (char) c;
	}

	@Override
	public void flush() throws IOException {
		flushBuffer();
		wrapped.close();
	}

	@Override
	public void close() throws IOException {
		flushBuffer();
		wrapped.flush();
	}
	
	private void flushBuffer() throws IOException {
		wrapped.write(buffer, 0, pos);
		pos = 0;
	}
}
