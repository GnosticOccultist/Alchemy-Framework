package fr.alchemy.core.asset.binary;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.javafx.fxml.expression.Expression;

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
	 * The binary importer.
	 */
	private final BinaryImporter importer;
	
	private BinaryClassObject cObj;

    protected Exportable exportable;

    protected Map<Byte, Object> fieldData;

    protected int index = 0;
	
    public BinaryReader(BinaryImporter importer, Exportable exportable, BinaryClassObject bco) {
        this.importer = importer;
        this.cObj = bco;
        this.exportable = exportable;
    }
	
    public void setContent(byte[] content, int start, int limit) {
        fieldData = new HashMap<Byte, Object>();
        for (index = start; index < limit;) {
            byte alias = content[index];

            index++;

            try {
            	cObj.nameFields.keySet().forEach(System.out::println);
                byte type = cObj.aliasFields.get(alias).type;
                Object value = null;

                switch (type) {
                    case BinaryClassField.BITSET: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.BOOLEAN: {
                        value = readBoolean(content);
                        break;
                    }
                    case BinaryClassField.BOOLEAN_1D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.BOOLEAN_2D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.BYTE: {
                        value = readByte(content);
                        break;
                    }
                    case BinaryClassField.BYTE_1D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.BYTE_2D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.BYTEBUFFER: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.DOUBLE: {
                        value = readDouble(content);
                        break;
                    }
                    case BinaryClassField.DOUBLE_1D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.DOUBLE_2D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.FLOAT: {
                        value = readFloat(content);
                        break;
                    }
                    case BinaryClassField.FLOAT_1D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.FLOAT_2D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.FLOATBUFFER: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.FLOATBUFFER_ARRAYLIST: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.BYTEBUFFER_ARRAYLIST: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.INT: {
                        value = readInteger(content);
                        break;
                    }
                    case BinaryClassField.INT_1D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.INT_2D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.INTBUFFER: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.LONG: {
                        value = readLong(content);
                        break;
                    }
                    case BinaryClassField.LONG_1D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.LONG_2D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.SAVABLE: {
                        value = readExportable(content);
                        break;
                    }
                    case BinaryClassField.SAVABLE_1D: {
                        value = readExportableArray(content);
                        break;
                    }
                    case BinaryClassField.SAVABLE_2D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.SAVABLE_ARRAYLIST: {
                    	value = readExportableArray(content);
                        break;
                    }
                    case BinaryClassField.SAVABLE_ARRAYLIST_1D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.SAVABLE_ARRAYLIST_2D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.SAVABLE_MAP: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.STRING_SAVABLE_MAP: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.INT_SAVABLE_MAP: {
                        throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.SHORT: {
                        value = readShort(content);
                        break;
                    }
                    case BinaryClassField.SHORT_1D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.SHORT_2D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.SHORTBUFFER: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.STRING: {
                        value = readString(content);
                        break;
                    }
                    case BinaryClassField.STRING_1D: {
                    	throw new UnsupportedOperationException();
                    }
                    case BinaryClassField.STRING_2D: {
                    	throw new UnsupportedOperationException();
                    }

                    default:
                        // skip put statement
                        continue;
                }

                fieldData.put(alias, value);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	
	/**
	 * Reads the byte specified by the given name and turn it into a boolean.
	 * 
	 * @param name			The name of the boolean value.
	 * @param defaultValue  The default boolean value to return if reading fails.
	 * @return				The readed boolean value or the provided one.
	 * @throws IOException
	 */
    public boolean readBoolean(String name, boolean defVal) throws IOException {
        BinaryClassField field = cObj.nameFields.get(name);
        if (field == null || !fieldData.containsKey(field.alias))
            return defVal;
        return ((Boolean) fieldData.get(field.alias)).booleanValue();
    }
	
    public byte readByte(String name, byte defVal) throws IOException {
        BinaryClassField field = cObj.nameFields.get(name);
        if (field == null || !fieldData.containsKey(field.alias))
            return defVal;
        return ((Byte) fieldData.get(field.alias)).byteValue();
    }
    
    public double readDouble(String name, double defVal) throws IOException {
        BinaryClassField field = cObj.nameFields.get(name);
        if (field == null || !fieldData.containsKey(field.alias))
            return defVal;
        return ((Double) fieldData.get(field.alias)).doubleValue();
    }
    
    public float readFloat(String name, float defVal) throws IOException {
        BinaryClassField field = cObj.nameFields.get(name);
        if (field == null || !fieldData.containsKey(field.alias))
            return defVal;
        return ((Float) fieldData.get(field.alias)).floatValue();
    }
    
    public int readInteger(String name, int defVal) throws IOException {
        BinaryClassField field = cObj.nameFields.get(name);
        if (field == null || !fieldData.containsKey(field.alias))
            return defVal;
        return ((Integer) fieldData.get(field.alias)).intValue();
    }
    
    public long readLong(String name, long defVal) throws IOException {
        BinaryClassField field = cObj.nameFields.get(name);
        if (field == null || !fieldData.containsKey(field.alias))
            return defVal;
        return ((Long) fieldData.get(field.alias)).longValue();
    }
    
    public Exportable readExportable(String name, Exportable defVal) throws IOException {
        BinaryClassField field = cObj.nameFields.get(name);
        if (field == null || !fieldData.containsKey(field.alias))
            return defVal;
        Object value = fieldData.get(field.alias);
        if (value == null)
            return null;
        else if (value instanceof ID) {
            value = importer.readObject(((ID) value).id);
            fieldData.put(field.alias, value);
            return (Exportable) value;
        } else
            return defVal;
    }
    
    private Exportable[] resolveIDs(Object[] values) {
        if (values != null) {
        	Exportable[] savables = new Exportable[values.length];
            for (int i = 0; i < values.length; i++) {
                final ID id = (ID) values[i];
                savables[i] = id != null ? importer.readObject(id.id) : null;
            }
            return savables;
        } else {
            return null;
        }
    }
    
    public short readShort(String name, short defVal) throws IOException {
        BinaryClassField field = cObj.nameFields.get(name);
        if (field == null || !fieldData.containsKey(field.alias))
            return defVal;
        return ((Short) fieldData.get(field.alias)).shortValue();
    }
	
    public String readString(String name, String defVal) throws IOException {
        BinaryClassField field = cObj.nameFields.get(name);
        if (field == null || !fieldData.containsKey(field.alias))
            return defVal;
        return (String) fieldData.get(field.alias);
    }
    
    public Exportable[] readExportableArray(String name, Exportable[] defVal)
            throws IOException {
        BinaryClassField field = cObj.nameFields.get(name);
        if (field == null || !fieldData.containsKey(field.alias))
            return defVal;
        Object[] values = (Object[]) fieldData.get(field.alias);
        if (values instanceof ID[]) {
            values = resolveIDs(values);
            fieldData.put(field.alias, values);
            return (Exportable[]) values;
        } else
            return defVal;
    }
    
    protected static byte[] inflateFrom(byte[] contents, int index) {
        byte firstByte = contents[index];
        if (firstByte == BinaryWriter.NULL_OBJECT)
            return ByteUtils.toBytes(BinaryWriter.NULL_OBJECT);
        else if (firstByte == BinaryWriter.DEFAULT_OBJECT)
            return ByteUtils.toBytes(BinaryWriter.DEFAULT_OBJECT);
        else if (firstByte == 0)
            return new byte[0];
        else {
            byte[] rVal = new byte[firstByte];
            for (int x = 0; x < rVal.length; x++)
                rVal[x] = contents[x + 1 + index];
            return rVal;
        }
    }
    
    protected byte readByte(byte[] content) throws IOException {
        byte value = content[index];
        index++;
        return value;
    }
    
    protected int readInteger(byte[] content) throws IOException {
        byte[] bytes = inflateFrom(content, index);
        index += 1 + bytes.length;
        bytes = ByteUtils.rightAlignBytes(bytes, 4);
        int value = ByteUtils.readInteger(bytes, 0);
        if (value == BinaryWriter.NULL_OBJECT
                || value == BinaryWriter.DEFAULT_OBJECT)
            index -= 4;
        return value;
    }

    
    protected float readFloat(byte[] content) throws IOException {
        float value = ByteUtils.readFloat(content, index);
        index += 4;
        return value;
    }
    
    protected double readDouble(byte[] content) throws IOException {
        double value = ByteUtils.readDouble(content, index);
        index += 8;
        return value;
    }
    
    protected long readLong(byte[] content) throws IOException {
        byte[] bytes = inflateFrom(content, index);
        index += 1 + bytes.length;
        bytes = ByteUtils.rightAlignBytes(bytes, 8);
        long value = ByteUtils.readLong(bytes, 0);
        return value;
    }

    
    protected short readShort(byte[] content) throws IOException {
        short value = ByteUtils.readShort(content, index);
        index += 2;
        return value;
    }
    
    protected boolean readBoolean(byte[] content) throws IOException {
        boolean value = ByteUtils.readBoolean(content, index);
        index += 1;
        return value;
    }
    
    /*
     * UTF-8 crash course:
     *
     * UTF-8 codepoints map to UTF-16 codepoints and vv, which is what Java uses for its Strings.
     * (so a UTF-8 codepoint can contain all possible values for a Java char)
     *
     * A UTF-8 codepoint can be 1, 2 or 3 bytes long. How long a codepint is can be told by reading the first byte:
     * b < 0x80, 1 byte
     * (b & 0xC0) == 0xC0, 2 bytes
     * (b & 0xE0) == 0xE0, 3 bytes
     *
     * However there is an additional restriction to UTF-8, to enable you to find the start of a UTF-8 codepoint,
     * if you start reading at a random point in a UTF-8 byte stream. That's why UTF-8 requires for the second and third byte of
     * a multibyte codepoint:
     * (b & 0x80) == 0x80  (in other words, first bit must be 1)
     */
    private final static int UTF8_START = 0; // next byte should be the start of a new
    private final static int UTF8_2BYTE = 2; // next byte should be the second byte of a 2 byte codepoint
    private final static int UTF8_3BYTE_1 = 3; // next byte should be the second byte of a 3 byte codepoint
    private final static int UTF8_3BYTE_2 = 4; // next byte should be the third byte of a 3 byte codepoint
    private final static int UTF8_ILLEGAL = 10; // not an UTF8 string

    // String
    protected String readString(byte[] content) throws IOException {
        int length = readInteger(content);
        if (length == BinaryWriter.NULL_OBJECT)
            return null;

        /*
         * @see ISSUE 276
         *
         * We'll transfer the bytes into a separate byte array.
         * While we do that we'll take the opportunity to check if the byte data is valid UTF-8.
         *
         * If it is not UTF-8 it is most likely saved with the BinaryOutputCapsule bug, that saves Strings using their native
         * encoding. Unfortunatly there is no way to know what encoding was used, so we'll parse using the most common one in
         * that case; latin-1 aka ISO8859_1
         *
         * Encoding of "low" ASCII codepoint (in plain speak: when no special characters are used) will usually look the same
         * for UTF-8 and the other 1 byte codepoint encodings (espc true for numbers and regular letters of the alphabet). So these
         * are valid UTF-8 and will give the same result (at most a few charakters will appear different, such as the euro sign).
         *
         * However, when "high" codepoints are used (any codepoint that over 0x7F, in other words where the first bit is a 1) it's
         * a different matter and UTF-8 and the 1 byte encoding greatly will differ, as well as most 1 byte encodings relative to each
         * other.
         *
         * It is impossible to detect which one-byte encoding is used. Since UTF8 and practically all 1-byte encodings share the most
         * used characters (the "none-high" ones) parsing them will give the same result. However, not all byte sequences are legal in
         * UTF-8 (see explantion above). If not UTF-8 encoded content is detected we therefore fall back on latin1. We also log a warning.
         *
         * By this method we detect all use of 1 byte encoding if they:
         * - use a "high" codepoint after a "low" codepoint or a sequence of codepoints that is valid as UTF-8 bytes, that starts with 1000
         * - use a "low" codepoint after a "high" codepoint
         * - use a "low" codepoint after "high" codepoint, after a "high" codepoint that starts with 1110
         *
         *  In practise this means that unless 2 or 3 "high" codepoints are used after each other in proper order, we'll detect the string
         *  was not originally UTF-8 encoded.
         *
         */
        byte[] bytes = new byte[length];
        int utf8State = UTF8_START;
        int b;
        for (int x = 0; x < length; x++) {
            bytes[x] =  content[index++];
            b = (int) bytes[x] & 0xFF; // unsign our byte

            switch (utf8State) {
            case UTF8_START:
                if (b < 0x80) {
                    // good
                }
                else if ((b & 0xC0) == 0xC0) {
                    utf8State = UTF8_2BYTE;
                }
                else if ((b & 0xE0) == 0xE0) {
                    utf8State = UTF8_3BYTE_1;
                }
                else {
                    utf8State = UTF8_ILLEGAL;
                }
                break;
            case UTF8_3BYTE_1:
            case UTF8_3BYTE_2:
            case UTF8_2BYTE:
                 if ((b & 0x80) == 0x80)
                    utf8State = utf8State == UTF8_3BYTE_1 ? UTF8_3BYTE_2 : UTF8_START;
                 else
                    utf8State = UTF8_ILLEGAL;
                break;
            }
        }

        try {
            // even though so far the parsing might have been a legal UTF-8 sequence, only if a codepoint is fully given is it correct UTF-8
            if (utf8State == UTF8_START) {
                // Java misspells UTF-8 as UTF8 for official use in java.lang
                return new String(bytes, "UTF8");
            }
            else {
            	System.err.println(
                        "Your export has been saved with an incorrect encoding for it's String fields which means it might not load correctly " +
                        "due to encoding issues. You should probably re-export your work. See ISSUE 276 in the jME issue tracker."
                );
                // We use ISO8859_1 to be consistent across platforms. We could default to native encoding, but this would lead to inconsistent
                // behaviour across platforms!
                // Developers that have previously saved their exports using the old exporter (which uses native encoding), can temporarly
                // remove the ""ISO8859_1" parameter, and change the above if statement to "if (false)".
                // They should then import and re-export their models using the same environment they were originally created in.
                return new String(bytes, "ISO8859_1");
            }
        } catch (UnsupportedEncodingException uee) {
            // as a last resort fall back to platform native.
            // JavaDoc is vague about what happens when a decoding a String that contains un undecodable sequence
            // it also doesn't specify which encodings have to be supported (though UTF-8 and ISO8859 have been in the SUN JRE since at least 1.1)
        	System.err.println(
                    "Your export has been saved with an incorrect encoding or your version of Java is unable to decode the stored string. " +
                    "While your export may load correctly by falling back, using it on different platforms or java versions might lead to "+
                    "very strange inconsitenties. You should probably re-export your work. See ISSUE 276 in the jME issue tracker."
            );
            return new String(bytes);
        }
    }
    
    protected ID readExportable(byte[] content) throws IOException {
        int id = readInteger(content);
        if (id == BinaryWriter.NULL_OBJECT) {
            return null;
        }

        return new ID(id);
    }
   
    protected ID[] readExportableArray(byte[] content) throws IOException {
        int elements = readInteger(content);
        if (elements == BinaryWriter.NULL_OBJECT)
            return null;
        ID[] rVal = new ID[elements];
        for (int x = 0; x < elements; x++) {
            rVal[x] = readExportable(content);
        }
        return rVal;
    }
    
    public ArrayList<?> readSavableArrayList(String name, ArrayList<?> defVal) throws IOException {
        BinaryClassField field = cObj.nameFields.get(name);
        if (field == null || !fieldData.containsKey(field.alias))
            return defVal;
        Object value = fieldData.get(field.alias);
        if (value instanceof ID[]) {
            // read Savable array and convert to ArrayList
            Exportable[] savables = readExportableArray(name, null);
            value = exportableArrayListFromArray(savables);
            fieldData.put(field.alias, value);
        }
        return (ArrayList<?>) value;

    }
    
    private ArrayList<Exportable> exportableArrayListFromArray(Exportable[] savables) {
        if(savables == null) {
            return null;
        }
        ArrayList<Exportable> arrayList = new ArrayList<Exportable>(savables.length);
        for (int x = 0; x < savables.length; x++) {
            arrayList.add(savables[x]);
        }
        return arrayList;
    }
    
    static private class ID {
        public int id;

        public ID(int id) {
            this.id = id;
        }
    }

    static private class StringIDMap {
        public String[] keys;
        public ID[] values;
    }

    static private class IntIDMap {
        public int[] keys;
        public ID[] values;
    }

    public <T extends Enum<T>> T readEnum(String name, Class<T> enumType, T defVal) throws IOException {
        String eVal = readString(name, defVal != null ? defVal.name() : null);
        if (eVal != null) {
            return Enum.valueOf(enumType, eVal);
        } else {
            return null;
        }
    }
}
