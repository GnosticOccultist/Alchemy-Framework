package fr.alchemy.utilities.file.json;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;

import fr.alchemy.utilities.file.io.FastBufferedWriter;
import fr.alchemy.utilities.file.json.JSONWriter.PrettyJSONWriter;

/**
 * <code>JSONValue</code> represents a JSON value. This can be a JSON <strong>object</strong>, an <strong>array</strong>,
 * a <strong>number</strong>, a <strong>string</strong>, or one of the literals <strong>true</strong>, <strong>false</strong>, 
 * and <strong>null</strong>.
 * <p>
 * The literals <strong>true</strong>, <strong>false</strong>, and <strong>null</strong> are represented by the constants 
 * {@link AlchemyJSON#TRUE}, {@link AlchemyJSON#FALSE}, and {@link AlchemyJSON#NULL}.
 * </p>
 * <p>
 * JSON <strong>objects</strong> and <strong>arrays</strong> are represented by the subtypes {@link JSONObject} and {@link JSONArray}.
 * Instances of these types can be created using the public constructors of these classes.
 * </p>
 * <p>
 * If the type of a JSON value is known by the user, the methods {@link #asObject()}, {@link #asArray()}, {@link #asBoolean()}, {@link #asString()},
 * {@link #asInt()}, ect. can be used to cast the value to the appropriate type.
 * </p>
 * <p>
 * This class is <strong>not supposed to be extended</strong> by clients.
 * </p>
 * 
 * @author GnosticOccultist
 */
public abstract class JSONValue implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Package constructor to prevent subclasses outside of the package.
	 */
	JSONValue() {}
	
	/**
	 * Detects whether this <code>JSONValue</code> represents a JSON object. If this is the case, 
	 * this value is an instance of {@link JSONObject}.
	 * 
	 * @return Whether the value is an instance of JSON object.
	 */
	public boolean isObject() {
		return false;
	}
	
	/**
	 * Detects whether this <code>JSONValue</code> represents a JSON array. If this is the case, 
	 * this value is an instance of {@link JSONArray}.
	 * 
	 * @return Whether the value is an instance of JSON array.
	 */
	public boolean isArray() {
		return false;
	}
	
	/**
	 * Detects whether this <code>JSONValue</code> represents a JSON number. If this is the case, 
	 * this value is an instance of {@link JSONNumber}.
	 * 
	 * @return Whether the value is an instance of JSON number.
	 */
	public boolean isNumber() {
		return false;
	}
	
	/**
	 * Detects whether this <code>JSONValue</code> represents a JSON string. If this is the case, 
	 * this value is an instance of {@link JSONString}.
	 * 
	 * @return Whether the value is an instance of JSON string.
	 */
	public boolean isString() {
		return false;
	}
	
	/**
	 * Detects whether this <code>JSONValue</code> represents a JSON literal <code>null</code>. 
	 * If this is the case, this value is an instance of {@link JSONLiteral}.
	 * 
	 * @return Whether the value is an instance of JSON literal null.
	 */
	public boolean isNull() {
		return false;
	}
	
	/**
	 * Detects whether this <code>JSONValue</code> represents a JSON literal <code>true</code>. 
	 * If this is the case, this value is an instance of {@link JSONLiteral}.
	 * 
	 * @return Whether the value is an instance of JSON literal true.
	 */
	public boolean isTrue() {
		return false;
	}
	
	/**
	 * Detects whether this <code>JSONValue</code> represents a JSON literal <code>false</code>. 
	 * If this is the case, this value is an instance of {@link JSONLiteral}.
	 * 
	 * @return Whether the value is an instance of JSON literal false.
	 */
	public boolean isFalse() {
		return false;
	}
	
	/**
	 * Return the <code>JSONValue</code> as a {@link JSONObject}, assuming that the value is representing
	 * an object otherwise an exception is thrown.
	 * 
	 * @return The value casted as a JSON object (not null).
	 * 
	 * @throws UnsupportedOperationException Thrown if the value isn't representing a JSON object.
	 */
	public JSONObject asObject() {
		throw new UnsupportedOperationException("The JSON value " + this + " can't be cast as an object!");
	}
	
	/**
	 * Return the <code>JSONValue</code> as a {@link JSONArray}, assuming that the value is representing
	 * an array otherwise an exception is thrown.
	 * 
	 * @return The value casted as a JSON array (not null).
	 * 
	 * @throws UnsupportedOperationException Thrown if the value isn't representing a JSON array.
	 */
	public JSONArray asArray() {
		throw new UnsupportedOperationException("The JSON value " + this + " can't be cast as an array!");
	}
	
	/**
	 * Return the <code>JSONValue</code> as a {@link JSONString}, assuming that the value is representing
	 * a string otherwise an exception is thrown.
	 * 
	 * @return The value casted as a JSON string (not null).
	 * 
	 * @throws UnsupportedOperationException Thrown if the value isn't representing a JSON string.
	 */
	public String asString() {
		throw new UnsupportedOperationException("The JSON value " + this + " can't be cast as a string!");
	}
	
	/**
	 * Return the <code>JSONValue</code> as an integer value, assuming that the value is representing
	 * a {@link JSONNumber} that can be intepreted as an integer, otherwise an exception is thrown.
	 * <p>
	 * The number must be in range of the Java's {@link Integer} and don't contain a fraction or an 
	 * exponent part.
	 * 
	 * @return The value casted as an int.
	 * 
	 * @throws UnsupportedOperationException Thrown if the value isn't representing a JSON number.
	 */
	public int asInt() {
		throw new UnsupportedOperationException("The JSON value " + this + " can't be cast as an number!");
	}
	
	/**
	 * Return the <code>JSONValue</code> as a boolean value, assuming that the value is representing
	 * a <code>true</code> or <code>false</code> literal, otherwise an exception is thrown.
	 * 
	 * @return The value casted as a boolean.
	 * 
	 * @throws UnsupportedOperationException Thrown if the value isn't representing a JSON boolean.
	 */
	boolean asBoolean() {
		throw new UnsupportedOperationException("The JSON value " + this + " can't be cast as a boolean!");
	}
	
	public void write(Writer writer) throws IOException {
		FastBufferedWriter buffer = new FastBufferedWriter(writer, 128);
		write(new JSONWriter(buffer));
		buffer.flush();
	}
	
	@Override
	public String toString() {
		StringWriter writer = new StringWriter();
		try {
			FastBufferedWriter buffer = new FastBufferedWriter(writer, 128);
			write(new PrettyJSONWriter(buffer));
			buffer.flush();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		
		return writer.toString();
	}
	
	abstract void write(JSONWriter writer) throws IOException;
}
