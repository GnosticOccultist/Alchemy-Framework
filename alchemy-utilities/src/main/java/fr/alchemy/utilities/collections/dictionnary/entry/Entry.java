package fr.alchemy.utilities.collections.dictionnary.entry;

import fr.alchemy.utilities.collections.pool.Reusable;

public interface Entry<T, V> extends Reusable {
	
	/**
	 * Get the next entry.
	 * 
	 * @return The next entry.
	 */
	T getNext();
	
	/**
	 * Sets the next entry.
	 * 
	 * @param next The next entry.
	 */
	void setNext(T next);
	
	/**
	 * Get the value.
	 * 
	 * @return The value.
	 */
	V getValue();
	
	/**
	 * Sets the value.
	 * 
	 * @param next The new value of this entry.
	 * @return	   The old value or null.
	 */
	V setValue(V next);
	
	/**
	 * Get the hash.
	 * 
	 * @return The hash.
	 */
	int getHash();
}
