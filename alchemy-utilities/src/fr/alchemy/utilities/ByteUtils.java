package fr.alchemy.utilities;

import java.io.IOException;

/**
 * <code>ByteUtils</code> provides utilities functions concerning byte format, mainly
 * for binary writing and reading.
 * 
 * @author GnosticOccultist
 */
public class ByteUtils {
	
	public static byte[] toBytes(final Object obj) throws IOException {
		if(obj instanceof Integer) {
			return toBytes((Integer) obj);
		} else if(obj instanceof Boolean) {
			return toBytes((Boolean) obj);
		} else if(obj instanceof String) {
			return toBytes((String) obj);
		}
		
		throw new IOException("Cannot convert " + obj.getClass().getSimpleName() + " to bytes!");
	}
	
	/**
	 * Converts the specified integer as an array of bytes.
	 * 
	 * @param integer The integer value.
	 * @return		  The array of bytes corresponding to the int value.
	 */
	public static byte[] toBytes(final int integer) {
		 byte[] bytes = new byte[4];

		 bytes[0] = (byte) (integer >> 24);
		 bytes[1] = (byte) (integer >> 16);
		 bytes[2] = (byte) (integer >> 8);
		 bytes[3] = (byte) integer;
		 return bytes;
	}
	
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
	 * Converts the specified string as an array of bytes in UTF-8 charset.
	 * 
	 * @param string The string value.
	 * @return		 The array of bytes in UTF-8.
	 * @throws IOException If the UTF-8 charset is not supported.
	 */
	public static byte[] toBytes(final String string) throws IOException {
		return string.getBytes("UTF8");
	}
}
