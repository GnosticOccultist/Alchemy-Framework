package fr.alchemy.utilities.dictionnary;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.array.Array;

public abstract class AbstractObjectDictionary<K, V> extends AbstractDictionary<K, V, ObjectEntry<K, V>> implements ObjectDictionary<K, V> {

    protected AbstractObjectDictionary() {
        this(DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractObjectDictionary(float loadFactor) {
        this(loadFactor, DEFAULT_INITIAL_CAPACITY);
    }

    protected AbstractObjectDictionary(int initCapacity) {
        this(DEFAULT_LOAD_FACTOR, initCapacity);
    }

    protected AbstractObjectDictionary(float loadFactor, int initCapacity) {
        super(loadFactor, initCapacity);
    }

    @Override
    protected Class<? super ObjectEntry<K, V>> getEntryType() {
        return ObjectEntry.class;
    }

    /**
     * Add new entry to this dictionary.
     *
     * @param hash  the hash of the key.
     * @param key   the key.
     * @param value the value of the key.
     * @param index the index of bucket.
     */
    private void addEntry(int hash, K key, V value, int index) {

        ObjectEntry<K, V>[] entries = entries();
        ObjectEntry<K, V> entry = entries[index];

        ObjectEntry<K, V> newEntry = entryPool.take(ObjectEntry::new);
        newEntry.set(hash, key, value, entry);

        entries[index] = newEntry;

        if (incrementSizeAndGet() >= getThreshold()) {
            resize(2 * entries.length);
        }
    }

    @Override
    public final boolean containsKey(K key) {
        return getEntry(key) != null;
    }

    @Override
    public V get(K key) {
        ObjectEntry<K, V> entry = getEntry(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public V getOrCompute(K key, Supplier<V> factory) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.get());
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("The factory " + factory + " returned a null value.");
        }

        return entry.getValue();
    }

    @Override
    public V getOrCompute(K key, Function<K, V> factory) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(key));
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("The factory " + factory + " returned a null value.");
        }

        return entry.getValue();
    }

    @Override
    public <T> V getOrCompute(
    		K key,
            T argument,
            Function<T, V> factory
    ) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, factory.apply(argument));
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("The factory " + factory + " returned a null value.");
        }

        return entry.getValue();
    }

    @Override
    public <T>  V getOrCompute(
    		K key,
            T argument,
            BiFunction<K, T, V> factory
    ) {

        ObjectEntry<K, V> entry = getEntry(key);

        if (entry == null) {
            put(key, Validator.nonNull(factory.apply(key, argument)));
            entry = getEntry(key);
        }

        if (entry == null) {
            throw new IllegalStateException("The factory " + factory + " returned a null value.");
        }

        return entry.getValue();
    }

    /**
     * Get an entry for the key.
     *
     * @param key the key.
     * @return the entry or null.
     */
    private ObjectEntry<K, V> getEntry(K key) {

        ObjectEntry<K, V>[] entries = entries();
        int hash = hash(key.hashCode());
        int index = indexFor(hash, entries.length);

        for (ObjectEntry<K, V> entry = entries[index]; entry != null; entry = entry.getNext()) {
            if (entry.getHash() == hash && key.equals(entry.getKey())) {
                return entry;
            }
        }

        return null;
    }
    
    @Override
    public V remove(K key) {
    	
        ObjectEntry<K, V> old = removeEntryForKey(key);
        V value = old == null ? null : old.getValue();

        if(old != null) {
            entryPool.put(old);
        }

        return value;
    }
    
    public ObjectEntry<K, V> removeEntryForKey(K key) {

        ObjectEntry<K, V>[] entries = entries();

        int hash = hash(key.hashCode());
        int i = indexFor(hash, entries.length);

        ObjectEntry<K, V> prev = entries[i];
        ObjectEntry<K, V> entry = prev;

        while (entry != null) {
            ObjectEntry<K, V> next = entry.getNext();

            if (entry.getHash() == hash && key.equals(entry.getKey())) {
                decrementSizeAndGet();

                if (prev == entry) {
                    entries[i] = next;
                } else {
                    prev.setNext(next);
                }

                return entry;
            }

            prev = entry;
            entry = next;
        }

        return null;
    }

    @Override
    public final Iterator<V> iterator() {
    	throw new UnsupportedOperationException();
    }

    @Override
    public final Array<K> keyArray(Array<K> container) {
        for (ObjectEntry<K, V> entry : entries()) {
            while (entry != null) {
                container.add(entry.getKey());
                entry = entry.getNext();
            }
        }
        return container;
    }

    @SuppressWarnings("unchecked")
	@Override
    public void copyTo(Dictionary<? super K, ? super V> dictionary) {

        if (isEmpty() || !(dictionary instanceof ObjectDictionary)) {
            return;
        }

        ObjectDictionary<K, V> target = (ObjectDictionary<K, V>) dictionary;

        for (ObjectEntry<K, V> entry : entries()) {
            while (entry != null) {
                target.put(entry.getKey(), entry.getValue());
                entry = entry.getNext();
            }
        }
    }

    @SuppressWarnings("resource")
	@Override
    public V put(K key, V value) {

    	ObjectEntry<K, V>[] entries = entries();

        int hash = hash(key.hashCode());
        int i = indexFor(hash, entries.length);
        
        for (ObjectEntry<K, V> entry = entries[i]; entry != null; entry = entry.getNext()) {
        	if (entry.getHash() == hash && key.equals(entry.getKey())) {
        		return entry.setValue(value);
        	}
        }
        
        addEntry(hash, key, value, i);
        return null;
    }

    @Override
    public final String toString() {

        int size = size();

        StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder.append(" size = ")
                .append(size)
                .append(" : ");

        ObjectEntry<K, V>[] table = entries();

        for (ObjectEntry<K, V> entry : table) {
            while (entry != null) {

                builder.append("[")
                        .append(entry.getKey())
                        .append(" - ")
                        .append(entry.getValue())
                        .append("]\n");

                entry = entry.getNext();
            }
        }

        if (size > 0) {
            builder.replace(builder.length() - 1, builder.length(), ".");
        }

        return builder.toString();
    }
}
