package fr.alchemy.utilities.collections.dictionnary.entry;

public class ObjectEntry<K, V> implements Entry<ObjectEntry<K, V>, V> {

	/**
	 * The next entry.
	 */
	private ObjectEntry<K, V> next;
	/**
	 * The key of this entry.
	 */
	private K key;
	/**
	 * The value of this entry.
	 */
	private V value;
	/**
	 * The hash of the key.
	 */
	private int hash;
	
	/**
	 * Sets all the fields for this <code>ObjectEntry</code>.
	 */
	public void set(int hash, K key, V value, ObjectEntry<K, V> next) {
		this.value = value;
        this.next = next;
        this.key = key;
        this.hash = hash;
	}
	
	@Override
	public ObjectEntry<K, V> getNext() {
		return next;
	}

	@Override
	public void setNext(ObjectEntry<K, V> next) {
		this.next = next;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public V setValue(V value) {
		V old = getValue();
		this.value = value;
		return old;
	}
	
	public K getKey() {
		return key;
	}
	
	@Override
	public void free() {
		this.key = null;
		this.value = null;
		this.next = null;
		this.hash = 0;
	}
	
	@Override
	public void reuse() {
		this.hash = 0;
	}

	@Override
	public int getHash() {
		return hash;
	}
	
	@Override
	public int hashCode() {
		return key.hashCode() ^ value.hashCode();
	}
	
	@Override
	public String toString() {
		return "Entry : " + key + " = " + value;
	}
}
