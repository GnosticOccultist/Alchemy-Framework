package fr.alchemy.utilities.dictionnary;

import fr.alchemy.utilities.array.Array;
import fr.alchemy.utilities.array.FastArray;
import fr.alchemy.utilities.pool.Reusable;

public interface Dictionary<K, V> extends Iterable<V>, Reusable {
	
	/**
	 * Put all the data from this <code>Dictionary</code> to
	 * the provided dictionary.
	 * 
	 * @param dictionary The dictionary to copy to.
	 */
	void copyTo(Dictionary<? super K, ? super V> dictionary);
	
	/**
	 * Put all the data from the provided dictionary to this 
	 * <code>Dictionary</code>.
	 * 
	 * @param dictionary The dictionary to copy from.
	 */
	default void put(Dictionary<K, V> dictionary) {
		dictionary.copyTo(this);
	}
	
	default int size() {
		throw new UnsupportedOperationException();
	}
	
    /**
     * Return whether the <code>Dictionary</code> contains the provided value.
     *
     * @param value The value to check presence.
     * @return		Whether the dictionary contains the value.
     */
    default boolean containsValue(V value) {
        throw new UnsupportedOperationException();
    }
	
	/**
	 * Clear the <code>Dictionary</code>.
	 */
	default void clear() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	default void free() {
		clear();
	}
	
	/**
	 * Return whether this <code>Dictionary</code> contains no 
	 * key-value mappings.
	 * 
	 * @return Whether the dictionary is empty.
	 */
	default boolean isEmpty() {
		return size() == 0;
	}
	
	default Array<V> values(Array<V> container) {
		throw new UnsupportedOperationException();
	}
	
	default Array<V> values(Class<V> type) {
		return values(new FastArray<V>(type, size()));
	}
}
