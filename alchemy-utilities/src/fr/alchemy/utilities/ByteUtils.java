package fr.alchemy.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * <code>ByteUtils</code> provides utilities functions concerning byte format, mainly
 * for binary writing and reading.
 * 
 * @author GnosticOccultist
 */
public class ByteUtils {
	
	/**
	 * Converts the specified boolean as an array of bytes,
	 * either 1 or 0 for true or false.
	 * 
	 * @param b The boolean value.
	 * @return  Either 1 or 0 byte value contained in an array.
	 */
	public static byte[] toBytes(final boolean b) {
		return new byte[] {b ? (byte) 1 : (byte) 0};
	}
	
	/**
	 * Converts the specified string as an array of bytes in the 
	 * specified charset.
	 * 
	 * @param string  The string value.
	 * @param charset The charset to use for encoding.
	 * @return		  The array of bytes in the specified charset.
	 * @throws IOException If the charset to use is not supported.
	 */
	public static byte[] toBytes(final String string, final String charset) throws IOException {
		return string.getBytes(charset);
	}
	
	/**
	 * Converts the specified string as an array of bytes in UTF-8 charset.
	 * 
	 * @param string The string value.
	 * @return		 The array of bytes in UTF-8.
	 * @throws IOException If the UTF-8 charset is not supported.
	 */
	public static byte[] toBytes(final String string) throws IOException {
		return toBytes(string, "UTF8");
	}
	
	/**
	 * Converts the specified integer as an array of bytes.
	 * 
	 * @param integer The integer value.
	 * @return		  The array of bytes corresponding to the int value.
	 */
	public static byte[] toBytes(final int integer) {
		 final byte[] bytes = new byte[4];

		 bytes[0] = (byte) (integer >> 24);
		 bytes[1] = (byte) (integer >> 16);
		 bytes[2] = (byte) (integer >> 8);
		 bytes[3] = (byte) integer;
		 return bytes;
	}
	
	/**
	 * Converts the specified float as an array of bytes.
	 * 
	 * @param f The float value.
	 * @return  The array of bytes corresponding to the float value.
	 */
    public static byte[] toBytes(final float f) {
        final int temp = Float.floatToIntBits(f);
        return toBytes(temp);
    }
	
	/**
	 * Converts the specified double as an array of bytes.
	 * 
	 * @param d The double value.
	 * @return	The array of bytes corresponding to the double value.
	 */
	public static byte[] toBytes(final double d) {
        final long bits = Double.doubleToLongBits(d);
        return toBytes(bits);
    }
	
	/**
	 * Converts the specified long as an array of bytes.
	 * 
	 * @param l The long value.
	 * @return	The array of bytes corresponding to the double value.
	 */
    public static byte[] toBytes(long l) {
    	final byte[] bytes = new byte[8];

        bytes[7] = (byte) (l); l >>>= 8;
        bytes[6] = (byte) (l); l >>>= 8;
        bytes[5] = (byte) (l); l >>>= 8;
        bytes[4] = (byte) (l); l >>>= 8;
        bytes[3] = (byte) (l); l >>>= 8;
        bytes[2] = (byte) (l); l >>>= 8;
        bytes[1] = (byte) (l); l >>>= 8;
        bytes[0] = (byte) (l);

        return bytes;
    }
    
    /**
     * Reads the specified amount of bytes in the provided {@link InputStream}.
     * 
     * @param inputStream The input stream to read bytes from.
     * @param length      The amount of bytes to read.
     * @return			  The readed byte array.
     */
    public static byte[] readBytes(final InputStream inputStream, final int length) throws IOException {
    	final byte[] byteArray = new byte[length];

        // Read the bytes in the provided length.
        inputStream.read(byteArray);
        
        return byteArray;
    }
    
    /**
     * Reads the specified interval of bytes (1 byte for a boolean value)
     * and converts it into a boolean value.
     * 
     * @param byteArray The total array of bytes.
     * @param offset    The index to where the reading needs to start.
     * @return			The boolean value of the corresponding byte.
     */
    public static boolean readBoolean(final byte[] byteArray, final int offset) {
        return byteArray[offset] != 0;
    }
    
    public static short readShort(byte[] byteArray, int offset) {
        // Convert it to a short
        short number = (short) ((byteArray[offset+1] & 0xFF) + ((byteArray[offset+0] & 0xFF) << 8));
        return number;
    }
    
    /**
     * Reads the specified interval of bytes (x bytes defined by length)
     * and converts it into a string value.
     * 
     * @param bytes	 The total array of bytes.
     * @param length The length of the bytes to read.
     * @param offset The index to where the reading needs to start.
     * @return		 The string value of the corresponding bytes array.
     */
    public static String readString(final byte[] bytes, final int length, final int offset) {
        final byte[] data = new byte[length];
        for(int j = 0; j < length; j++) {
            data[j] = bytes[j + offset];
        }

        return new String(data);
    }
    
    /**
     * Reads the specified interval of bytes (x bytes defined by length)
     * and converts it into a string value.
     * 
     * @param inputStream The input stream to read the bytes from.
     * @param length 	  The length of the bytes to read.
     * @return		 	  The string value of the corresponding readed bytes array.
     */
    public static String readString(final InputStream inputStream, final int length) throws IOException {
        final byte[] byteArray = new byte[length];

        // Read the string in the provided length bytes.
        inputStream.read(byteArray);

        return new String(byteArray);
    }
    
    /**
     * Reads the specified interval of bytes (4 bytes for a integer value)
     * and converts it into a integer value.
     * 
     * @param byteArray The total array of bytes.
     * @param offset	The index to where the reading needs to start.
     * @return			The integer value of the corresponding bytes array.
     */
    public static int readInteger(final byte[] byteArray, final int offset) {
        final int number =  
        		((byteArray[offset] & 0xFF) << 24)
        	+ ((byteArray[offset + 1] & 0xFF) << 16) 
        	+ ((byteArray[offset + 2] & 0xFF) << 8)
        	+ (byteArray[offset + 3] & 0xFF);
        return number;
    }
    
    /**
     * Reads the specified interval of bytes (4 bytes for a integer value)
     * from the provided {@link InputStream}.
     * 
     * @param inputStream The input stream to read the bytes from.
     * @return The integer value of the corresponding readed bytes array.
     */
    public static int readInteger(final InputStream inputStream) throws IOException {
        final byte[] byteArray = new byte[4];

        // Read the integer in the next 4 bytes.
        inputStream.read(byteArray);

        int number = readInteger(byteArray, 0);

        return number;
    }
    
    /**
     * Reads the specified interval of bytes (4 bytes for a float value)
     * and converts it into a float value.
     * 
     * @param byteArray The total array of bytes.
     * @param offset	The index to where the reading needs to start.
     * @return			The float value of the corresponding bytes array.
     */
    public static float readFloat(final byte[] byteArray, final int offset) {
        final int number = readInteger(byteArray, offset);
        return Float.intBitsToFloat(number);
    }
    
    /**
     * Reads the specified interval of bytes (8 bytes for a double value)
     * and converts it into a double value.
     * 
     * @param bytes  The total array of bytes.
     * @param offset The index to where the reading needs to start.
     * @return		 The double value of the corresponding bytes array.
     */
    public static double readDouble(final byte[] bytes, final int offset) {
        final long bits = readLong(bytes, offset);
        return Double.longBitsToDouble(bits);
    }
    
    /**
     * Reads the specified interval of bytes (8 bytes for a long value)
     * and converts it into a long value.
     * 
     * @param bytes  The total array of bytes.
     * @param offset The index to where the reading needs to start.
     * @return		 The long value of the corresponding bytes array.
     */
    public static long readLong(final byte[] bytes, final int offset) {
        final long bits = 
        	  ((((long) bytes[offset + 7]) & 0xFF) 
        	+ ((((long) bytes[offset + 6]) & 0xFF) << 8)
        	+ ((((long) bytes[offset + 5]) & 0xFF) << 16)
        	+ ((((long) bytes[offset + 4]) & 0xFF) << 24)
        	+ ((((long) bytes[offset + 3]) & 0xFF) << 32)
        	+ ((((long) bytes[offset + 2]) & 0xFF) << 40)
        	+ ((((long) bytes[offset + 1]) & 0xFF) << 48) 
        	+ ((((long) bytes[offset + 0]) & 0xFF) << 56));
        return bits;
    }
   
    /**
     * Converts a serializable object into a byte array.
     * 
     * @param object The object to serialize.
     * @return		 A byte array corresponding to the serialized object.
     */
    public static byte[] serialize(Serializable object) {

    	ByteArrayOutputStream bout = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bout)) {
            out.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bout.toByteArray();
    }

    /**
     * Converts the byte array to an object.
     * 
     * @param bytes The byte array to deserialize.
     * @return 		The result object from the byte array.
     */
    @SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] bytes) {

    	ByteArrayInputStream bin = new ByteArrayInputStream(bytes);

        try (ObjectInputStream in = new ObjectInputStream(bin)) {
            return (T) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static byte[] rightAlignBytes(byte[] bytes, int width) {
        if (bytes.length != width) {
            byte[] rVal = new byte[width];
            for (int x = width - bytes.length; x < width; x++) {
                rVal[x] = bytes[x - (width - bytes.length)];
            }
            return rVal;
        }
            
        return bytes;
    }
}
