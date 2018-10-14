package fr.alchemy.core.asset.binary;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import fr.alchemy.core.asset.Texture;
import fr.alchemy.core.asset.cache.Asset;
import fr.alchemy.utilities.ByteUtils;

/**
 * <code>BinaryWriter</code> is a wrapper class around an {@link OutputStream}, which
 * allow for easy and quick writing of byte values into the stream.
 * 
 * @author GnosticOccultist
 */
public final class BinaryWriter {
	
	/**
	 * The binary manager.
	 */
	private final BinaryManager manager;
	/**
	 * The output stream.
	 */
	private final OutputStream os;
	
	public BinaryWriter(final BinaryManager ex, final OutputStream os) {
		this.manager = ex;
		this.os = os;
	}
	
	/**
	 * Writes the provided object value and its name if the <code>BinaryWriter</code> can
	 * into a byte array.
	 * 
	 * @param name	The name of the value.
	 * @param value The object value to write.
	 * @throws IOException
	 */
	public void write(final String name, final Object value) throws IOException {
		if(value instanceof Boolean) {
			write(name, (Boolean) value);
		} else if(value instanceof Integer) {
			write(name, (Integer) value);
		} else if(value instanceof Float) {
			write(name, (Float) value);
		} else if(value instanceof Long) {
			write(name, (Long) value);
		} else if(value instanceof Double) {
			write(name, (Double) value);
		} else if(value instanceof String) {
			write(name, (String) value);
		} else if(value instanceof Exportable) {
			write(name, (Exportable) value);
		} else if(value instanceof Asset) {
			write(name, (Asset) value);
		} else {
			System.err.println("Unable to write the specified value type: " + value.getClass().getName());
		}
	}
	
	/**
	 * Writes the provided boolean value and its name into a byte array.
	 * 
	 * @param name 	The name of the boolean value.
	 * @param value The boolean value to write.
	 * @throws IOException
	 */
	public void write(final String name, final boolean value) throws IOException {
		if(name == null || name.isEmpty()) {
			throw new IOException("Name for '" + value + "' cannot be null or empty!");
		}
		
		writeName(name);
		writeValue(ByteUtils.toBytes(value));
	}
	
	/**
	 * Writes the provided integer value and its name into a byte array.
	 * 
	 * @param name 	The name of the integer value.
	 * @param value The integer value to write.
	 * @throws IOException
	 */
	public void write(final String name, final int value) throws IOException {
		if(name == null || name.isEmpty()) {
			throw new IOException("Name for '" + value + "' cannot be null or empty!");
		}
		
		writeName(name);
		writeValue(ByteUtils.toBytes(value));
	}
	
	/**
	 * Writes the provided float value and its name into a byte array.
	 * 
	 * @param name 	The name of the float value.
	 * @param value The float value to write.
	 * @throws IOException
	 */
	public void write(final String name, final float value) throws IOException {
		if(name == null || name.isEmpty()) {
			throw new IOException("Name for '" + value + "' cannot be null or empty!");
		}
		
		writeName(name);
		writeValue(ByteUtils.toBytes(value));
	}
	
	/**
	 * Writes the provided long value and its name into a byte array.
	 * 
	 * @param name 	The name of the long value.
	 * @param value The long value to write.
	 * @throws IOException
	 */
	public void write(final String name, final long value) throws IOException {
		if(name == null || name.isEmpty()) {
			throw new IOException("Name for '" + value + "' cannot be null or empty!");
		}
		
		writeName(name);
		writeValue(ByteUtils.toBytes(value));
	}
	
	/**
	 * Writes the provided double value and its name into a byte array.
	 * 
	 * @param name 	The name of the double value.
	 * @param value The double value to write.
	 * @throws IOException
	 */
	public void write(final String name, final double value) throws IOException {
		if(name == null || name.isEmpty()) {
			throw new IOException("Name for '" + value + "' cannot be null or empty!");
		}
		
		writeName(name);
		writeValue(ByteUtils.toBytes(value));
	}
	
	/**
	 * Writes the provided string value and its name into a byte array.
	 * 
	 * @param name 	The name of the string value.
	 * @param value The string value to write.
	 * @throws IOException
	 */
	public void write(final String name, final String value) throws IOException {
		if(name == null || name.isEmpty()) {
			throw new IOException("Name for '" + value + "' cannot be null or empty!");
		}
		
		writeName(name);
		
		if(value == null || value.isEmpty()) {
			throw new IOException("Value '" + value + "' cannot be null or empty!");
		}
		
		writeValue(ByteUtils.toBytes(value.length()));
		writeValue(ByteUtils.toBytes(value));
	}
	
	/**
	 * Writes the provided exportable value and its class name into a byte array.
	 * 
	 * @param name 	The class name of the exportable value.
	 * @param value The exportable value to write.
	 * @throws IOException
	 */
	public void write(final Exportable value) throws IOException {
		if(value == null) {
			throw new IOException("Name for '" + value + "' cannot be null or empty!");
		}
		
		writeName(value.getClass().getName());
		value.export(this);
	}
	
	/**
	 * Writes the provided {@link Exportable} array.
	 *
	 * @param values The texture array to write.
	 * @throws IOException
	 */
	public void write(final List<? extends Exportable> values) throws IOException {
		write("size", values.size());
		
		for(int i = 0; i < values.size(); i++) {
			write(values.get(i));
		}
	}
	
	/**
	 * Writes the provided {@link Texture} path and its class name into a byte array.
	 * 
	 * @param name 	The name of the texture.
	 * @param value The texture to write the path from.
	 * @throws IOException
	 */
	public void write(final String name, final Asset value) throws IOException {
		if(value == null) {
			throw new IOException("Name for '" + value + "' cannot be null or empty!");
		}
		
		writeName(name);
		
		final String path = value.getFile();
		writeValue(ByteUtils.toBytes(path.length()));
		writeValue(ByteUtils.toBytes(path));
	}
	
	/**
	 * Writes the provided {@link Texture} array and its name into a byte array.
	 * 
	 * @param name 	 The name of the texture array.
	 * @param values The texture array to write.
	 * @throws IOException
	 */
	public void write(final String name, final List<Texture> values) throws IOException {
		write(name + "_size", values.size());
		
		for(int i = 0; i < values.size(); i++) {
			write(name + "_" + i, values.get(i));
		}
	}

	/**
	 * Writes the name and the name's length to the {@link OutputStream}.
	 * 
	 * @param name The name to write.
	 * @throws IOException
	 */
	private void writeName(final String name) throws IOException {
		writeValue(ByteUtils.toBytes(name.length()));
		writeValue(ByteUtils.toBytes(name));
	}
	
	/**
	 * Writes the name and the name's length to the {@link OutputStream}.
	 * 
	 * @param name The name to write.
	 * @throws IOException
	 */
	private void writeValue(final byte[] bytes) throws IOException {
		os.write(bytes);
	}
	
	/**
	 * @return The binary manager.
	 */
	public BinaryManager manager() {
		return manager;
	}
}
