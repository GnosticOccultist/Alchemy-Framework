package fr.alchemy.core.asset.binary;

import java.io.IOException;
import java.io.InputStream;

import fr.alchemy.core.asset.AssetManager;
import fr.alchemy.core.asset.Texture;
import fr.alchemy.utilities.ByteUtils;

/**
 * <code>BinaryReader</code> is a wrapper class around an array of bytes from an {@link InputStream}, which
 * allow for easy and quick reading of byte values from the stream and turn them into
 * usable values.
 * 
 * @author GnosticOccultist
 */
public final class BinaryReader {

	/**
	 * The binary manager.
	 */
	private final BinaryManager manager;
	/**
	 * The total array of bytes from the input stream.
	 */
	private final byte[] bytes;
	/**
	 * The current readed byte index.
	 */
	private int numBytes;
	
	public BinaryReader(final BinaryManager ex, final byte[] bytes, int numBytes) {
		this.manager = ex;
		this.bytes = bytes;
		this.numBytes = numBytes;
	}
	
	/**
	 * Reads the byte specified by the given name and turn it into a boolean.
	 * 
	 * @param name			The name of the boolean value.
	 * @param defaultValue  The default boolean value to return if reading fails.
	 * @return				The readed boolean value or the provided one.
	 * @throws IOException
	 */
	public boolean read(final String name, final boolean defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return defaultValue;
		}
		numBytes += classLength;
		final boolean value = ByteUtils.readBoolean(bytes, numBytes);
		numBytes += 1;
		return value;
	}
	
	/**
	 * Reads the bytes specified by the given name and turn it into an integer.
	 * 
	 * @param name			The name of the integer value.
	 * @param defaultValue  The default integer value to return if reading fails.
	 * @return				The readed integer value or the provided one.
	 * @throws IOException
	 */
	public int read(final String name, final int defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return 0;
		}
		
		numBytes += classLength;
		final int value = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		return value;
	}
	
	/**
	 * Reads the bytes specified by the given name and turn it into a float.
	 * 
	 * @param name			The name of the float value.
	 * @param defaultValue  The default float value to return if reading fails.
	 * @return				The readed float value or the provided one.
	 * @throws IOException
	 */
	public float read(final String name, final float defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return 0;
		}
		
		numBytes += classLength;
		final float value = ByteUtils.readFloat(bytes, numBytes);
		numBytes += 4;
		return value;
	}
	
	/**
	 * Reads the bytes specified by the given name and turn it into a long.
	 * 
	 * @param name			The name of the long value.
	 * @param defaultValue  The default long value to return if reading fails.
	 * @return				The readed long value or the provided one.
	 * @throws IOException
	 */
	public long read(final String name, final long defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return 0;
		}
		
		numBytes += classLength;
		final long value = ByteUtils.readLong(bytes, numBytes);
		numBytes += 8;
		return value;
	}
	
	/**
	 * Reads the bytes specified by the given name and turn it into a double.
	 * 
	 * @param name			The name of the double value.
	 * @param defaultValue  The default double value to return if reading fails.
	 * @return				The readed double value or the provided one.
	 * @throws IOException
	 */
	public double read(final String name, final double defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return 0;
		}
		
		numBytes += classLength;
		final double value = ByteUtils.readDouble(bytes, numBytes);
		numBytes += 8;
		return value;
	}
	
	/**
	 * Reads the bytes specified by the given name and turn it into a String.
	 * 
	 * @param name			The name of the string value.
	 * @param defaultValue  The default string value to return if reading fails.
	 * @return				The readed string value or the provided one.
	 * @throws IOException
	 */
	public String read(final String name, final String defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return "";
		}
		
		numBytes += classLength;
		final int stringLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String string = ByteUtils.readString(bytes, stringLength, numBytes);
		numBytes += stringLength;
		return string;
	}
	
	/**
	 * Reads the bytes specified by the given class and turn it into an {@link Exportable} value.
	 * 
	 * @param name			The name of the object value.
	 * @param defaultValue  The default object value to return if reading fails.
	 * @return				The readed object value or the provided one.
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends Exportable> T read(final Class<T> type, final T defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String className = ByteUtils.readString(bytes, classLength, numBytes);
		if(!type.getName().equals(className)) {
			System.err.println(type.getName() + " doesn't match " + className + " . Please check if the class exists!");
			return null;
		}
		numBytes += classLength;
		final Exportable value = manager.insertObject(className, this);
		return (T) value;
	}
	
	/**
	 * Reads the bytes specified by the given name and turn it into a {@link Texture} loaded with
	 * the {@link AssetManager}.
	 * 
	 * @param name			The name of the texture value.
	 * @param defaultValue  The default texture value to return if reading fails.
	 * @return				The readed texture value or the provided one.
	 * @throws IOException
	 */
	public Texture read(final String name, final Texture defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return null;
		}
		
		numBytes += classLength;
		final int stringLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String string = ByteUtils.readString(bytes, stringLength, numBytes);
		numBytes += stringLength;
		return manager.assetManager.loadTexture(string);
	}	
	
	/**
	 * Reads the bytes specified by the given name and turn it into an array of {@link Texture} loaded with
	 * the {@link AssetManager}.
	 * 
	 * @param name			The name of the array of textures.
	 * @param defaultValue  The default array to return if reading fails.
	 * @return				The readed array of textures or the provided one.
	 * @throws IOException
	 */
	public Texture[] readArray(final String name, final Texture defaultValue) throws IOException {
		final int size = read(name + "_size", 0);
		Texture[] textures = new Texture[size];
		for(int i = 0; i < size; i++) {
			textures[i] = read(name + "_" + i, defaultValue); 
		}
		return textures;
	}	
	
	/**
	 * @return The binary manager.
	 */
	public BinaryManager manager() {
		return manager;
	}
}
