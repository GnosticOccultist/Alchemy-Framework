package fr.alchemy.core.asset.binary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
	
    public static final int NULL_OBJECT = -1;
    public static final int DEFAULT_OBJECT = -2;
    public static byte[] NULL_BYTES = new byte[] { (byte) -1 };
    public static byte[] DEFAULT_BYTES = new byte[] { (byte) -2 };
	
	/**
	 * The byte array output stream.
	 */
	protected ByteArrayOutputStream baos;
	/**
	 * The bytes array written.
	 */
	protected byte[] bytes;
	/**
	 * The binary exporter.
	 */
	private final BinaryExporter exporter;
	/**
	 * The class object in binary form.
	 */
	private final BinaryClassObject binaryObject;
	
	public BinaryWriter(final BinaryExporter exporter, final BinaryClassObject bco) {
		this.baos = new ByteArrayOutputStream();
		this.exporter = exporter;
		this.binaryObject = bco;
	}
	
	/**
	 * Writes the provided boolean value and its name into a byte array.
	 * 
	 * @param name 	The name of the boolean value.
	 * @param value The boolean value to write.
	 * @throws IOException
	 */
    public void write(boolean value, String name, boolean defVal) throws IOException {
        if (value == defVal)
            return;
        writeAlias(name, BinaryClassField.BOOLEAN);
        write(value);
    }
    
    public void write(int value, String name, int defVal) throws IOException {
        if (value == defVal) {
        	return;
        }
            
        writeAlias(name, BinaryClassField.INT);
        write(value);
    }

    protected void write(boolean value) throws IOException {
        baos.write(ByteUtils.toBytes(value));
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
	
    protected void write(int value) throws IOException {
        baos.write(deflate(ByteUtils.toBytes(value)));
    }
	
    protected void write(String value) throws IOException {
        if (value == null) {
            write(NULL_OBJECT);
            return;
        }
        
        // write our output as UTF-8. Java misspells UTF-8 as UTF8 for official use in java.lang
        byte[] bytes = value.getBytes("UTF8");
        write(bytes.length);
        baos.write(bytes);
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
    public void write(double value, String name, double defVal) throws IOException {
        if (value == defVal) {
        	return;
        }
           
        writeAlias(name, BinaryClassField.DOUBLE);
        write(value);
    }
    
    public void write(float value, String name, float defVal) throws IOException {
    	if (value == defVal) {
    		return;
    	}
    	
        writeAlias(name, BinaryClassField.FLOAT);
        write(value);
    }
    
    protected void write(double value) throws IOException {
        baos.write(ByteUtils.toBytes(value));
    }
    
    protected void write(float value) throws IOException {
        baos.write(ByteUtils.toBytes(value));
    }
	
	/**
	 * Writes the provided string value and its name into a byte array.
	 * 
	 * @param name 	The name of the string value.
	 * @param value The string value to write.
	 * @throws IOException
	 */
    public void write(String value, String name, String defVal) throws IOException {
        if (value == null ? defVal == null : value.equals(defVal))
            return;
        writeAlias(name, BinaryClassField.STRING);
        write(value);
    }
	
	/**
	 * Writes the provided exportable value and its class name into a byte array.
	 * 
	 * @param name 	The class name of the exportable value.
	 * @param value The exportable value to write.
	 * @throws IOException
	 */
    protected void write(Exportable object) throws IOException {
        if (object == null) {
            write(NULL_OBJECT);
            return;
        }
        
        int id = exporter.writeClassObject(object);
        write(id);
    }
	
    public void writeSavableArrayList(ArrayList<?> array, String name, ArrayList<?> defVal) throws IOException {
        if (array == defVal)
            return;
        writeAlias(name, BinaryClassField.SAVABLE_ARRAYLIST);
        writeSavableArrayList(array);
    }
    
    public void write(Exportable object, String name, Exportable defVal) throws IOException {
        if (object == defVal)
            return;
        
        writeAlias(name, BinaryClassField.SAVABLE);
        write(object);
    }
	
    protected void writeAlias(String name, byte fieldType) throws IOException {
        if (binaryObject.nameFields.get(name) == null)
            generateAlias(name, fieldType);

        byte alias = binaryObject.nameFields.get(name).alias;
        write(alias);
    }
    
    protected void generateAlias(String name, byte type) {
        byte alias = (byte) binaryObject.nameFields.size();
        binaryObject.nameFields.put(name, new BinaryClassField(name, alias, type));
    }
    
	/**
	 * Writes the provided {@link Exportable} array.
	 *
	 * @param values The texture array to write.
	 * @throws IOException
	 */
    public void writeSavableArrayList(ArrayList<?> array) throws IOException {
        if (array == null) {
            write(NULL_OBJECT);
            return;
        }
        write(array.size());
        for (Object bs : array) {
            write((Exportable) bs);
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
	protected void writeValue(final byte value) throws IOException {
		baos.write(value);
	}
	
	/**
	 * Writes the name and the name's length to the {@link OutputStream}.
	 * 
	 * @param name The name to write.
	 * @throws IOException
	 */
	protected void writeValue(final byte[] bytes) throws IOException {
		write(bytes.length);
		baos.write(bytes);
	}
	
    protected static byte[] deflate(byte[] bytes) {
        int size = bytes.length;
        if (size == 4) {
            int possibleMagic = ByteUtils.readInteger(bytes, 0);
            if (possibleMagic == NULL_OBJECT)
                return NULL_BYTES;
            else if (possibleMagic == DEFAULT_OBJECT)
                return DEFAULT_BYTES;
        }
        for (int x = 0; x < bytes.length; x++) {
            if (bytes[x] != 0)
                break;
            size--;
        }
        if (size == 0)
            return new byte[1];

        byte[] rVal = new byte[1 + size];
        rVal[0] = (byte) size;
        for (int x = 1; x < rVal.length; x++)
            rVal[x] = bytes[bytes.length - size - 1 + x];

        return rVal;
    }

	public void finish() {
		bytes = baos.toByteArray();
		baos = null;
	}
}
