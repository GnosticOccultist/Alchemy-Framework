package fr.alchemy.utilities.file.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import fr.alchemy.utilities.Validator;

/**
 * <code>JSONObject</code> represents a JSON object, a set of name/value pairs, where the names are strings and the values
 * are {@link JSONValue}.
 * <p>
 * Members can be added using the {@link #add(String, JSONValue)} methods which accept instances of {@link JSONValue}. To modify
 * certain values of an object, use the 
 * 
 * @author GnosticOccultist
 */
public class JSONObject extends JSONValue {

	private static final long serialVersionUID = 1L;
	
	/**
	 * The list of names for the values.
	 */
	private final List<String> names;
	/**
	 * The list of the JSON values.
	 */
	private final List<JSONValue> values;
	/**
	 * The index table to keep track of the indexes.
	 */
	private transient HashIndexTable table;
	
	/**
	 * Instantiates a new empty <code>JSONObject</code>.
	 */
	public JSONObject() {
		names = new ArrayList<String>();
		values = new ArrayList<JSONValue>();
		table = new HashIndexTable();
	}
	
	/**
	 * Appends a new member to the end of this <code>JSONObject</code>, with the specified name and 
	 * the specified {@link JSONValue}.
	 * <p>
	 * This method <strong>does not prevent duplicate names</strong>. Calling this method with a name
	 * that already exists in the object will append another member with the same name. In order to
	 * replace existing members, use the method {@link #set(String, JSONValue)} instead. However,
	 * <strong><em>add</em> is much faster than <em>set</em></strong> (because it does not need to search
	 * for existing members). Therefore <em>add</em> should be preferred when constructing new objects.
	 * </p>
	 * <p>
	 * Finally, please note that the provided name and JSON value cannot be null.
	 * </p>
	 * 
	 * @param name	The name of the member to add.
	 * @param value The value of the member to add.
	 * @return		The object itself, to enable method chaining.
	 */
	public JSONObject add(String name, JSONValue value) {
		Validator.nonNull(name);
		Validator.nonNull(value);
		
		table.add(name, names.size());
		names.add(name);
		values.add(value);
		return this;
	}
	
	/**
	 * Sets the value of the member with the specified name to the specified {@link JSONValue}. If this
	 * object does not contain a member with this name, a new member is added at the end of the object.
	 * If this object contains multiple members with the name, only the last one is changed.
	 * <p>
	 * This method should <strong>only be used to modify existing objects</strong>. To fill a new object
	 * with members, the method {@link #add(String, JSONValue)} should be preferred which is much faster
	 * (as it does not need to search for existing members).
	 * </p>
	 * 
	 * @param name	The name of the member to add.
	 * @param value The value of the member to add.
	 * @return		The object itself, to enable method chaining.
	 */
	public JSONObject set(String name, JSONValue value) {
		Validator.nonNull(name);
		Validator.nonNull(value);
		
		int index = indexOf(name);
		if(index != -1) {
			values.set(index, value);
		} else {
			table.add(name, names.size());
			names.add(name);
			values.add(value);
		}
		return this;
	}
	
	/**
	 * Returns the value of the member with the specified name in this <code>JSONObject</code>. 
	 * If this object contains multiple members with the given name, this method will return the last one.
	 *
	 * @param name The name of the member whose value is to be returned.
	 * @return 	 The value of the last member with the specified name, or <code>null</code> if this
	 *         	 object does not contain a member with that name.
	 */
	public JSONValue get(String name) {
		Validator.nonNull(name);
		int index = indexOf(name);
	    return index != -1 ? values.get(index) : null;
	}
	
	/**
	 * Returns the value of the member as an {@link Optional} with the specified name in this <code>JSONObject</code>. 
	 * If this object contains multiple members with the given name, this method will return the last one.
	 *
	 * @param name The name of the member whose value is to be returned.
	 * @return 	 The value of the last member with the specified name, or <code>null</code> if this
	 *         	 object does not contain a member with that name.
	 */
	public Optional<JSONValue> getOptional(String name) {
		Validator.nonNull(name);
		int index = indexOf(name);
	    return index != -1 ? Optional.ofNullable(values.get(index)) : Optional.empty();
	}
	
	/**
	 * Checks if a specified member is present as a child of this <code>JSONObject</code>. 
	 * This will not test if this object contains the literal <code>null</code>, {@link JSONValue#isNull()}
	 * should be used for that purpose.
	 * 
	 * @param name The name of the member to check for.
	 * @return	   Whether the specified member is present.
	 */
	public boolean contains(String name) {
		return names.contains(name);
	}
	
	/**
	 * Returns the number of members (name/value pairs) in the <code>JSONObject</code>.
	 * 
	 * @return The number of members in the JSON object.
	 */
	public int size() {
		return names.size();
	}
	
	/**
	 * Returns whether the <code>JSONObject</code> contains no members.
	 * 
	 * @return Whether the JSON object contains no members.
	 */
	public boolean isEmpty() {
		return names.isEmpty();
	}
	
	public List<String> names() {
		return Collections.unmodifiableList(names);
	}
	
	public List<JSONValue> values() {
		return Collections.unmodifiableList(values);
	}
	
	int indexOf(String name) {
		int index = table.get(name);
		if(index != -1 && name.equals(names.get(index))) {
			return index;
		}
		return names.lastIndexOf(name);
	}
	
	@Override
	public boolean isObject() {
		return true;
	}
	
	@Override
	public JSONObject asObject() {
		return this;
	}
	
	@Override
	public int hashCode() {
		int result = 1;
	    result = 31 * result + names.hashCode();
	    result = 31 * result + values.hashCode();
	    return result;
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
	    JSONObject other = (JSONObject) o;
	    return names.equals(other.names) && values.equals(other.values);
	}
	
	static class HashIndexTable {

		private final byte[] hashTable = new byte[32]; 

		HashIndexTable() {}

		HashIndexTable(HashIndexTable original) {
			System.arraycopy(original.hashTable, 0, hashTable, 0, hashTable.length);
		}

		void add(String name, int index) {
			int slot = hashSlotFor(name);
			if (index < 0xff) {
				// increment by 1, 0 stands for empty
				hashTable[slot] = (byte)(index + 1);
			} else {
				hashTable[slot] = 0;
			}
		}
		
		void remove(int index) {
			for (int i = 0; i < hashTable.length; i++) {
				if ((hashTable[i] & 0xff) == index + 1) {
					hashTable[i] = 0;
				} else if ((hashTable[i] & 0xff) > index + 1) {
					hashTable[i]--;
				}
			}
		}

		int get(Object name) {
			int slot = hashSlotFor(name);
			// subtract 1, 0 stands for empty
			return (hashTable[slot] & 0xff) - 1;
		}

		private int hashSlotFor(Object element) {
			return element.hashCode() & hashTable.length - 1;
		}	
	}
}
