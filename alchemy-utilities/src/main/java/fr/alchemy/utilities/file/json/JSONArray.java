package fr.alchemy.utilities.file.json;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.collections.array.Array;
import fr.alchemy.utilities.collections.array.ReadOnlyArray;

/**
 * <code>JSONArray</code> is an implementation of {@link JSONValue} to represent a JSON array containing
 * an ordered collection of JSON values.
 * 
 * @author GnosticOccultist
 */
public class JSONArray extends JSONValue implements Iterable<JSONValue> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The values contained in the array.
	 */
	private final Array<JSONValue> values;
	
	/**
	 * Creates a new empty JsonArray.
	 */
	public JSONArray() {
		values = Array.ofType(JSONValue.class);
	}
	
	/**
	 * Appends the specified JSON value to the end of this <code>JSONArray</code>.
	 * 
	 * @param value The JSON value to add to the array (not null).
	 * @return 		The array itself, to enable method chaining.
	 */
	public JSONArray add(JSONValue value) {
		Validator.nonNull(value, "The JSON value to add can't be null!");
		values.add(value);
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
	 * Returns a {@link ReadOnlyArray} of the values in this <code>JSONArray</code> in document order.
	 * <p>
	 * It cannot be used to modify this array. Attempts to modify the returned array will result in an exception
	 * as it is read-only.
	 * 
	 * @return A read-only array of the values in this array (not null).
	 */
	public ReadOnlyArray<JSONValue> values() {
		return Array.of(values);
	}
	
	/**
	 * Returns a {@link Collection} of the values in this <code>JSONArray</code> in document order. 
	 * The returned array is backed by this array and will reflect subsequent changes. 
	 * <p>
	 * It cannot be used to modify this array. Attempts to modify the returned collection will result in an 
	 * exception as it is read-only.
	 * 
	 * @return An unmodifiable collection of the values in this array (not null).
	 */
	public Collection<JSONValue> collection() {
		return Collections.unmodifiableCollection(values);
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
	void write(JSONWriter writer) throws IOException {
		writer.writeArrayOpen();
	    Iterator<JSONValue> iterator = iterator();
	    if(iterator.hasNext()) {
	    	iterator.next().write(writer);
	    	while (iterator.hasNext()) {
	    		writer.writeArraySeparator();
	    		iterator.next().write(writer);
	    	}
	    }
	    writer.writeArrayClose();
	}

	@Override
	public Iterator<JSONValue> iterator() {
		final Iterator<JSONValue> it = values.iterator();
		return new Iterator<JSONValue>() {

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public JSONValue next() {
				return it.next();
			}
			
			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	
	@Override
	public int hashCode() {
		return values.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		
		if(o == null || getClass() != o.getClass()) {
			return false;
		}
		
		JSONArray other = (JSONArray) o;
		return values.equals(other.values);
	}
}
