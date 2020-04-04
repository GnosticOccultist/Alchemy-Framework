package fr.alchemy.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * <code>ByteUtils</code> provides utility functions concerning byte format, mainly for binary writing and reading.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public final class ByteUtils {
	
	/**
	 * Private constructor to inhibit instantiation of <code>ByteUtils</code>.
	 */
	private ByteUtils() {}
   
    /**
     * Converts the given {@link Serializable} object into an array of bytes.
     * 
     * @param object The object to serialize (not null).
     * @return		 A byte array corresponding to the serialized object (not null).
     */
    public static byte[] serialize(Serializable object) {
    	Validator.nonNull(object, "The serializable object can't be null!");
    	ByteArrayOutputStream bout = new ByteArrayOutputStream();

        try (ObjectOutputStream out = new ObjectOutputStream(bout)) {
            out.writeObject(object);
        } catch (IOException ex) {
        	throw new RuntimeException(ex);
        }

        return bout.toByteArray();
    }

    /**
     * Converts the byte array to a {@link Serializable} object.
     * 
     * @param bytes The byte array to deserialize (not null, not empty).
     * @return 		The result object from the byte array.
     */
    @SuppressWarnings("unchecked")
	public static <T extends Serializable> T deserialize(byte[] bytes) {
    	Validator.nonEmpty(bytes, "The byte array to deserialize can't be null!");
    	ByteArrayInputStream bin = new ByteArrayInputStream(bytes);

        try (ObjectInputStream in = new ObjectInputStream(bin)) {
            return (T) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
