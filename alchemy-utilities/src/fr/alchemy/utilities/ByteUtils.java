package fr.alchemy.utilities;

import java.io.IOException;
import java.io.InputStream;

/**
 * <code>ByteUtils</code> provides utilities functions concerning byte format, mainly
 * for binary writing and reading.
 * 
 * @author GnosticOccultist
 */
public class ByteUtils {
	
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
	
	public static byte[] toBytes(double n) {
        long bits = Double.doubleToLongBits(n);
        return toBytes(bits);
    }
	
    public static byte[] toBytes(long n) {
        byte[] bytes = new byte[8];

        bytes[7] = (byte) (n);
        n >>>= 8;
        bytes[6] = (byte) (n);
        n >>>= 8;
        bytes[5] = (byte) (n);
        n >>>= 8;
        bytes[4] = (byte) (n);
        n >>>= 8;
        bytes[3] = (byte) (n);
        n >>>= 8;
        bytes[2] = (byte) (n);
        n >>>= 8;
        bytes[1] = (byte) (n);
        n >>>= 8;
        bytes[0] = (byte) (n);

        return bytes;
    }
	
    public static String readString(InputStream f, int length) throws IOException {
        byte[] data = new byte[length];
        for(int j = 0; j < length; j++) {
            data[j] = (byte) f.read();
        }

        return new String(data);
    }
    
    public static String readString(byte[] bytes, int length, int offset) throws IOException {
        byte[] data = new byte[length];
        for(int j = 0; j < length; j++) {
            data[j] = bytes[j + offset];
        }

        return new String(data);
    }
    
    public static int readInt(final InputStream inputStream) throws IOException {
        byte[] byteArray = new byte[4];
        // Read in the next 4 bytes
        inputStream.read(byteArray);

        int number = convertIntFromBytes(byteArray);

        return number;
    }
    
    public static int convertIntFromBytes(final byte[] byteArray) {
        // Convert it to an int
        int number = ((byteArray[0] & 0xFF) << 24)
                + ((byteArray[0 + 1] & 0xFF) << 16) + ((byteArray[0 + 2] & 0xFF) << 8)
                + (byteArray[0 + 3] & 0xFF);
        return number;
    }
    
    public static int convertIntFromBytes(byte[] byteArray, int offset) {
        // Convert it to an int
        int number = ((byteArray[offset] & 0xFF) << 24)
                + ((byteArray[offset+1] & 0xFF) << 16) + ((byteArray[offset+2] & 0xFF) << 8)
                + (byteArray[offset+3] & 0xFF);
        return number;

    }
    
    public static boolean readBoolean(InputStream inputStream) throws IOException {
        byte[] byteArray = new byte[1];
        // Read in the next byte
        inputStream.read(byteArray);

        return byteArray[0] != 0;
    }
    
    public static boolean convertBooleanFromBytes(byte[] byteArray, int offset) {
        return byteArray[offset] != 0;
    }
    

    public static double convertDoubleFromBytes(byte[] bytes, int offset) {
        // Convert it to a double
        long bits = convertLongFromBytes(bytes, offset);
        return Double.longBitsToDouble(bits);
    }
    
    public static long convertLongFromBytes(byte[] bytes, int offset) {
        // Convert it to a long
        return    ((((long) bytes[offset+7]) & 0xFF) 
                + ((((long) bytes[offset+6]) & 0xFF) << 8)
                + ((((long) bytes[offset+5]) & 0xFF) << 16)
                + ((((long) bytes[offset+4]) & 0xFF) << 24)
                + ((((long) bytes[offset+3]) & 0xFF) << 32)
                + ((((long) bytes[offset+2]) & 0xFF) << 40)
                + ((((long) bytes[offset+1]) & 0xFF) << 48) 
                + ((((long) bytes[offset+0]) & 0xFF) << 56));
    }
}
