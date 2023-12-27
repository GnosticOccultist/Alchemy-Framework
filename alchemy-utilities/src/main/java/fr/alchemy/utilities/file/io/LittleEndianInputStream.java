package fr.alchemy.utilities.file.io;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;

/**
 * <code>LittleEndianInputStream</code> is an implementation of
 * {@link InputStream} which allows reading little-endian stored data.
 * 
 * @version 0.2.1
 * @since 0.2.1
 * 
 * @author GnosticOccultist
 */
public class LittleEndianInputStream extends InputStream implements DataInput {

	/**
	 * The internal input stream.
	 */
	private BufferedInputStream in;

	/**
	 * Instantiates a new <code>LittleEndianInputStream</code> for the given
	 * {@link InputStream}.
	 * 
	 * @param in The input stream to read from.
	 */
	public LittleEndianInputStream(InputStream in) {
		this.in = new BufferedInputStream(in);
	}

	@Override
	public void readFully(byte[] b) throws IOException {
		in.read(b, 0, b.length);
	}

	@Override
	public void readFully(byte[] b, int off, int len) throws IOException {
		in.read(b, off, len);
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return (int) in.skip(n);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return (in.read() != 0);
	}

	@Override
	public byte readByte() throws IOException {
		return (byte) in.read();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return in.read();
	}

	@Override
	public short readShort() throws IOException {
		return (short) readUnsignedShort();
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return (in.read() & 0xff) | ((in.read() & 0xff) << 8);
	}

	@Override
	public char readChar() throws IOException {
		return (char) readUnsignedShort();
	}

	@Override
	public int readInt() throws IOException {
		return ((in.read() & 0xff) | ((in.read() & 0xff) << 8) | ((in.read() & 0xff) << 16)
				| ((in.read() & 0xff) << 24));
	}

	@Override
	public long readLong() throws IOException {
		return ((in.read() & 0xff) | ((long) (in.read() & 0xff) << 8) | ((long) (in.read() & 0xff) << 16)
				| ((long) (in.read() & 0xff) << 24) | ((long) (in.read() & 0xff) << 32)
				| ((long) (in.read() & 0xff) << 40) | ((long) (in.read() & 0xff) << 48)
				| ((long) (in.read() & 0xff) << 56));
	}

	@Override
	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	@Override
	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	@Override
	public String readLine() throws IOException {
		throw new IOException("Not supported!");
	}

	@Override
	public String readUTF() throws IOException {
		throw new IOException("Not supported!");
	}

	@Override
	public int read() throws IOException {
		return in.read();
	}

	@Override
	public int available() throws IOException {
		return in.available();
	}

	@Override
	public void close() throws IOException {
		in.close();
		in = null;
	}
}
