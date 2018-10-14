package fr.alchemy.core.asset.binary;

import java.io.IOException;
import java.io.InputStream;

import fr.alchemy.core.asset.AssetManager;
import fr.alchemy.core.asset.Texture;
import fr.alchemy.core.asset.cache.Asset;
import fr.alchemy.core.scene.component.Component;
import fr.alchemy.core.scene.component.Transform;
import fr.alchemy.core.scene.component.VisualComponent;
import fr.alchemy.core.scene.entity.Entity;
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
	public Object read(final String name, final Object defaultValue) throws IOException {
		if(defaultValue instanceof Boolean) {
			return readBoolean(name, (Boolean) defaultValue);
		} else if(defaultValue instanceof Integer) {
			return readInteger(name, (Integer) defaultValue);
		} else if(defaultValue instanceof Float) {
			return readFloat(name, (Float) defaultValue);
		} else if(defaultValue instanceof Long) {
			return readLong(name, (Long) defaultValue);
		} else if(defaultValue instanceof Double) {
			return readDouble(name, (Double) defaultValue);
		} else if(defaultValue instanceof String) {
			return readString(name, (String) defaultValue);
		} else if(defaultValue instanceof Exportable) {
			return read(name, (Exportable) defaultValue);
		} else if(defaultValue instanceof Asset) {
			return readAsset(name, (Asset) defaultValue);
		}
		
		System.err.println("Unable to write the specified value type: " + defaultValue.getClass().getName());
		return null;
	}
	
	/**
	 * Reads the byte specified by the given name and turn it into a boolean.
	 * 
	 * @param name			The name of the boolean value.
	 * @param defaultValue  The default boolean value to return if reading fails.
	 * @return				The readed boolean value or the provided one.
	 * @throws IOException
	 */
	public boolean readBoolean(final String name, final boolean defaultValue) throws IOException {
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
	public int readInteger(final String name, final int defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return defaultValue;
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
	public float readFloat(final String name, final float defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return defaultValue;
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
	public long readLong(final String name, final long defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return defaultValue;
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
	public double readDouble(final String name, final double defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return defaultValue;
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
	public String readString(final String name, final String defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return defaultValue;
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
	public <T extends Exportable> T readExportable(final Class<T> type, final T defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String className = ByteUtils.readString(bytes, classLength, numBytes);
		numBytes += classLength;
		final T value = manager.insertObject(className, this);
		return value;
	}
	
	/**
	 * Reads the bytes specified by the given name and turn it into an array of {@link Exportable}.
	 * 
	 * @param name			The name of the array of exportable.
	 * @param defaultValue  The default array to return if reading fails.
	 * @return				The readed array of textures or the provided one.
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public <T extends Exportable> T[] readExportableArray(final Class<T> type, final T[] defaultValue) throws IOException {
		final int size = readInteger("size", 0);
		final T[] exportables = (T[]) new Exportable[size];
		for(int i = 0; i < size; i++) {
			if(i + 1 < defaultValue.length) {
				exportables[i] = readExportable(type, defaultValue[i]); 
			} else {
				exportables[i] = readExportable(type, null); 
			}
		}
		return exportables;
	}
	
	/**
	 * Reads and attaches the {@link Component components} to the provided {@link Entity}.
	 * 
	 * @param entity The entity to attach the components to.
	 * @throws IOException
	 */
	public void attachComponents(final Entity entity) throws IOException {
		final Exportable[] components = readExportableArray(Exportable.class, new Exportable[] {new Transform(), new VisualComponent()});
		entity.getComponent(Transform.class).set((Transform) components[0]);
		entity.getComponent(VisualComponent.class).set((VisualComponent) components[1]);
		
		for(int i = 2; i < components.length; i++) {	
			entity.attach((Component) components[i]);	
		}
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
	public Asset readAsset(final String name, final Asset defaultValue) throws IOException {
		final int classLength = ByteUtils.readInteger(bytes, numBytes);
		numBytes += 4;
		final String fieldName = ByteUtils.readString(bytes, classLength, numBytes);
		if(!name.equals(fieldName)) {
			System.err.println("Unknown field named: " + fieldName);
			return defaultValue;
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
	public Asset[] readAssetArray(final String name, final Asset defaultValue) throws IOException {
		final int size = readInteger(name + "_size", 0);
		final Asset[] textures = new Asset[size];
		for(int i = 0; i < size; i++) {
			textures[i] = readAsset(name + "_" + i, defaultValue); 
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
