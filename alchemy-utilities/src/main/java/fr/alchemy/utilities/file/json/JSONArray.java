package fr.alchemy.utilities.file.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import fr.alchemy.utilities.Validator;

public class JSONArray extends JSONValue implements Iterable<JSONValue> {

	private static final long serialVersionUID = 1L;
	
	private final List<JSONValue> values;
	
	/**
	 * Creates a new empty JsonArray.
	 */
	public JSONArray() {
		values = new ArrayList<JSONValue>();
	}
	
	/**
	 * Appends the specified JSON value to the end of this <code>JSONArray</code>.
	 * 
	 * @param value The JSON value to add to the array.
	 * @return 		The array itself, to enable method chaining.
	 */
	public JSONArray add(JSONValue value) {
		Validator.nonNull(value);
		values.add(value);
		return this;
	}
	
	/**
	 * Replaces the element at the specified position in this <code>JSONArray</code>.
	 * with the specified JSON value.
	 * 
	 * @param index	The index of the array element to replace.
	 * @param value	The value to be stored at the specified position.
	 * @return		The array itself, to enable method chaining.
	 * 
	 * @throws IndexOutOfBoundsException Thrown if the index is out of range (&lt;0 or &gt;size).
	 */
	public JSONArray set(int index, JSONValue value) {
		Validator.nonNull(value);
		values.set(index, value);
		return this;
	}
	
	/**
	 * Removes the element at the specified index from this <code>JSONArray</code>. 
	 * 
	 * @param index The index of the element to remove.
	 * @return		The array itself, to enable method chaining.
	 * 
	 * @throws IndexOutOfBoundsException Thrown if the index is out of range (&lt;0 or &gt;size).
	 */
	public JSONArray remove(int index) {
		values.remove(index);
		return this;
	}
	
	/**
	 * Returns the number of elements in this <code>JSONArray</code>.  
	 * 
	 * @return The number of elements in this array.
	 */
	public int size() {
		return values.size();
	}
	
	/**
	 * Returns whether this <code>JSONArray</code> contains no elements.
	 * 
	 * @return Whether this array contains no elements.
	 */
	public boolean isEmpty() {
		return values.isEmpty();
	}
	
	/**
	 * Returns the value of the element at the specified position in this <code>JSONArray</code>.
	 *
	 * @param index The index of the array element to return.
	 * @return The value of the element at the specified position.
	 * 
	 * @throws IndexOutOfBoundsException Thrown if the index is out of range (&lt;0 or &gt;size).
	 */
	public JSONValue get(int index) {
		return values.get(index);	
	}
	
	/**
	 * Returns a list of the values in this <code>JSONArray</code> in document order. The returned 
	 * list is backed by this array and will reflect subsequent changes. It cannot be used to modify 
	 * this array. Attempts to modify the returned list will result in an exception.
	 * 
	 * @return A read-only list of the values in this array.
	 */
	public List<JSONValue> values() {
		return Collections.unmodifiableList(values);
	}
	
	@Override
	public boolean isArray() {
		return true;
	}
	
	@Override
	public JSONArray asArray() {
		return this;
	}

	@Override
	public Iterator<JSONValue> iterator() {
		return null;
	}
	
	@Override
	public int hashCode() {
		return values.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (getClass() != o.getClass()) {
			return false;
		}
		JSONArray other = (JSONArray) o;
		return values.equals(other.values);
	}
}
