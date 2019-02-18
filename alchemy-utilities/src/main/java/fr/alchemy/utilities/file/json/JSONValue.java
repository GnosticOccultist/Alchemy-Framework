package fr.alchemy.utilities.file.json;

import java.io.Serializable;

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
	 * @return Whether the value is an instance of JSON literal <code>null</code>.
	 */
	public boolean isNull() {
		return false;
	}
	
	public JSONObject asObject() {
		throw new UnsupportedOperationException("The JSON value " + this + " can't be cast as an object!");
	}
	
	public JSONArray asArray() {
		throw new UnsupportedOperationException("The JSON value " + this + " can't be cast as an array!");
	}
	
	public String asString() {
		throw new UnsupportedOperationException("The JSON value " + this + " can't be cast as a string!");
	}
	
	public int asInt() {
		throw new UnsupportedOperationException("The JSON value " + this + " can't be cast as an number!");
	}
}
