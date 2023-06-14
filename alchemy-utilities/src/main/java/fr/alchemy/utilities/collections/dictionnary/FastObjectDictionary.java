package fr.alchemy.utilities.collections.dictionnary;

import fr.alchemy.utilities.collections.dictionnary.entry.ObjectEntry;

public class FastObjectDictionary<K, V> extends AbstractObjectDictionary<K, V> {

	/**
	 * The array of entries.
	 */
	private ObjectEntry<K, V>[] entries;
	/**
	 * The next size value at which to resize (capacity * load factor).
	 */
	private int threshold;
	/**
	 * The count of values in this dictionary.
	 */
	private int size;
	
	public FastObjectDictionary() {
		this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
	}
	
	public FastObjectDictionary(float loadFactor, int initialCapacity) {
		super(loadFactor, initialCapacity);
	}
	
	@Override
	public int size() {
		return size;
	}
	
	@Override
	protected void setSize(int size) {
		this.size = size;
	}
	
	@Override
	protected ObjectEntry<K, V>[] entries() {
		return entries;
	}
	
	@Override
	protected void setEntries(ObjectEntry<K, V>[] entries) {
		this.entries = entries;
	}
	
	@Override
	protected int getThreshold() {
		return threshold;
	}
	
	@Override
	protected void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	
	@Override
	protected int decrementSizeAndGet() {
		return --size;
	}
	
	@Override
	protected int incrementSizeAndGet() {
		return ++size;
	}
}
