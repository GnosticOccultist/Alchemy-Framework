package fr.alchemy.utilities.collections.dictionnary;

import java.util.Objects;
import java.util.function.Consumer;

import fr.alchemy.utilities.collections.array.Array;
import fr.alchemy.utilities.collections.array.ArrayUtil;
import fr.alchemy.utilities.collections.dictionnary.entry.Entry;
import fr.alchemy.utilities.collections.pool.FastReusablePool;

public abstract class AbstractDictionary<K, V, E extends Entry<E, V>> implements Dictionary<K, V> {
	
	/**
	 * The default initial capacity of the dictionary.
	 */
	protected static final int DEFAULT_INITIAL_CAPACITY = 16;
	/**
	 * The maximum capacity before reaching maximum threshold value.
	 */
	protected static final int DEFAULT_MAXIMUM_CAPACITY = 1 << 30;
	/**
	 * The load factor before resizing the dictionary.
	 */
	protected static final float DEFAULT_LOAD_FACTOR = 0.75f;
	
    /**
     * Calculate a hash value for the provided hashcode.
     *
     * @param hashcode The hashcode.
     * @return 		   The calculated hash value.
     */
    protected static int hash(int hashcode) {
        hashcode ^= hashcode >>> 20 ^ hashcode >>> 12;
        return hashcode ^ hashcode >>> 7 ^ hashcode >>> 4;
    }

    /**
     * Calculate a hash value for the provided long key.
     *
     * @param key The long key.
     * @return    The calculated hash value.
     */
    protected static int hash(long key) {
        int hash = (int) (key ^ key >>> 32);
        hash ^= hash >>> 20 ^ hash >>> 12;
        return hash ^ hash >>> 7 ^ hash >>> 4;
    }

    /**
     * Return an index array in the {@link Dictionary}.
     *
     * @param hash   The hash value of a key.
     * @param length The length of an array.
     * @return 		 The index in the array.
     */
    protected static int indexFor(int hash, int length) {
        return hash & length - 1;
    }
	
	/**
	 * The reusable pool of entries.
	 */
	protected final FastReusablePool<E> entryPool;
	/**
	 * The load factor.
	 */
	protected final float loadFactor;
	
	@SuppressWarnings("unchecked")
	protected AbstractDictionary(float loadFactor, int initialCapacity) {
		this.loadFactor = loadFactor;
		this.entryPool = new FastReusablePool<>((Class<E>) getEntryType());
		setEntries((E[]) ArrayUtil.create(getEntryType(), initialCapacity));
		setThreshold((int) (initialCapacity * loadFactor)); 
		setSize(0);
	}
	
    @SuppressWarnings("unchecked")
	protected final void resize(int newLength) {
        E[] prevEntries = entries();

        int oldLength = prevEntries.length;

        if (oldLength >= DEFAULT_MAXIMUM_CAPACITY) {
            setThreshold(Integer.MAX_VALUE);
            return;
        }

        E[] newEntries = (E[]) ArrayUtil.create(getEntryType(), newLength);

        transfer(newEntries);
        setEntries(newEntries);
        setThreshold((int) (newLength * loadFactor));
    }
    
    private void transfer(E[] newEntries) {
        E[] entries = entries();

        int newCapacity = newEntries.length;
        for (E entry : entries) {

            if (entry == null) {
                continue;
            }
            
            do {
                E next = entry.getNext();
                int i = indexFor(entry.getHash(), newCapacity);

                entry.setNext(newEntries[i]);
                newEntries[i] = entry;
                entry = next;

            } while (entry != null);
        }
    }
    
    @Override
    public boolean containsValue(V value) {
    	for (E entry : entries()) {
            for (E nextEntry = entry; nextEntry != null; nextEntry = nextEntry.getNext()) {
                if (Objects.equals(value, nextEntry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public void forEach(Consumer<? super V> consumer) {
        for (Entry<E, V> entry : entries()) {
            while (entry != null) {
                consumer.accept(entry.getValue());
                entry = entry.getNext();
            }
        }
    }
    
    @Override
    public void clear() {
    	E[] entries = entries();
        E next;
        for(E entry : entries) {
            while (entry != null) {
                next = entry.getNext();
                entryPool.inject(entry);
                entry = next;
            }
        }
        
        ArrayUtil.clear(entries);
        setSize(0);
    }
    
    @Override
    public final Array<V> values(Array<V> container) {
        for (E entry : entries()) {
            while (entry != null) {
                container.add(entry.getValue());
                entry = entry.getNext();
            }
        }
        return container;
    }
	
	/**
	 * Return the {@link Entry} type of the <code>AbstractDictionary</code>.
	 * 
	 * @return The entries type.
	 */
	protected abstract Class<? super E> getEntryType();
	
	/**
	 * Return an array of all entries in this <code>AbstractDictionary</code>.
	 * 
	 * @return The array of entries.
	 */
	protected abstract E[] entries();
	
	/**
	 * Sets the new array of entries for this <code>AbstractDictionary</code>.
	 * 
	 * @param entries The new array of entries.
	 */
	protected abstract void setEntries(E[] entries);
	
    /**
     * Return the threshold of the <code>AbstractDictionary</code>.
     *
     * @return The next size of resizing.
     */
    protected abstract int getThreshold();
    
	/**
	 * Sets the next size value at which to resize the <code>AbstractDictionary</code>.
	 * 
	 * @param threshold The next size of the resizing.
	 */
	protected abstract void setThreshold(int threshold);
	
	/**
	 * Sets the new size of this <code>AbstractDictionary</code>.
	 * 
	 * @param size The new size of the dictionary.
	 */
	protected abstract void setSize(int size);
	
    /**
     * Decrement and get the size of this <code>Dictionary</code>.
     *
     * @return The new size of this dictionary.
     */
    protected abstract int decrementSizeAndGet();

    /**
     * Increment and get the size of this <code>Dictionary</code>.
     *
     * @return The new size of this dictionary.
     */
    protected abstract int incrementSizeAndGet();
}
