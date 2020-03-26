package fr.alchemy.utilities.collections.dictionnary;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import fr.alchemy.utilities.collections.array.Array;
import fr.alchemy.utilities.collections.array.FastArray;

public interface ObjectDictionary<K, V> extends Dictionary<K, V> {

	@SuppressWarnings("unchecked")
	static <T> ObjectDictionary<T, T> ofType(Class<? super T> keyValueType) {
        return (ObjectDictionary<T, T>) new FastObjectDictionary();
    }
	
	@SuppressWarnings("unchecked")
	static <K, V> ObjectDictionary<K, V> ofType(Class<? super K> keyType, Class<? super V> valueType) {
        return (ObjectDictionary<K, V>) new FastObjectDictionary();
    }
	
	static <K, V> PoolDictionary emptyPool() {
        return new PoolDictionary();
    }
	
	static <K, V> PoolDictionary poolOf(Class<? super K> poolType) {
        return new PoolDictionary();
    }
	
    /**
     * Return true if this dictionary contains a mapping for the specified key.
     *
     * @param key key whose presence in this dictionary is to be tested.
     * @return true if this dictionary contains a mapping for the specified key.
     */
    default boolean containsKey(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the value to which the specified key is mapped, or {@code null} if this dictionary
     * contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the value to which the specified key is mapped, or {@code null} if this dictionary contains no mapping for the key.
     */
    default V get(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the optional value to which the specified key is mapped, or {@code null} if this dictionary
     * contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned.
     * @return the optional value to which the specified key is mapped.
     */
    default Optional<V> getOptional(K key) {
        return Optional.ofNullable(get(key));
    }
    
    default V getOrDefault(K key, V def) {
    	if(containsKey(key)) {
    		return get(key);
    	}
    	return def;
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param key     the key.
     * @param factory the factory.
     * @return the stored value by the key or the new value.
     */
    default V getOrCompute(K key, Supplier<V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param key     the key.
     * @param factory the factory.
     * @return the stored value by the key or the new value.
     */
    default V getOrCompute(K key, Function<K, V> factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param <T>      the argument's type.
     * @param key      the key.
     * @param argument the additional argument.
     * @param factory  the factory.
     * @return the stored value by the key or the new value.
     */
    default <T> V getOrCompute(
    		K key,
    		T argument,
    		Function<T, V> factory
    ) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param <T>      the argument's type.
     * @param key      the key.
     * @param argument the additional argument.
     * @param factory  the factory.
     * @return the stored value by the key or the new value.
     * @see #getOrCompute(Object, Object, BiFunction)
     */
    default <T> V get(K key, T argument, BiFunction<K, T, V> factory) {
        return getOrCompute(key, argument, factory);
    }

    /**
     * Get the value for the key. If the value doesn't exists, the factory will create new value,
     * puts this value to this dictionary and return this value.
     *
     * @param <T>      the argument's type.
     * @param key      the key.
     * @param argument the additional argument.
     * @param factory  the factory.
     * @return the stored value by the key or the new value.
     */
    default <T>  V getOrCompute(
    		K key,
            T argument,
            BiFunction<K, T, V> factory
    ) {

        throw new UnsupportedOperationException();
    }

    /**
     * Put to the array all keys of this dictionary.
     *
     * @param container the container.
     * @return the array with all keys.
     */
    default Array<K> keyArray(Array<K> container) {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an array with all keys of this dictionary.
     *
     * @param type the key's type.
     * @return the array with all keys of this dictionary.
     */
    default Array<K> keyArray(Class<K> type) {
        return keyArray(new FastArray<>(type, size()));
    }

    /**
     * Put the value by the key.
     *
     * @param key   the value's key.
     * @param value the value.
     * @return the previous value for the key or null.
     */
    default V put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Put the value by the key.
     *
     * @param key   the value's key.
     * @param value the value.
     * @return the optional value of the previous value for the key.
     */
    default Optional<V> putOptional(K key, V value) {
        return Optional.ofNullable(put(key, value));
    }

    /**
     * Remove a mapping of the key.
     *
     * @param key the key.
     * @return the previous value for the key or null.
     */
    default V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a mapping of the key.
     *
     * @param key the key.
     * @return the optional value of the previous value for the key.
     */
    default Optional<V> removeOptional(K key) {
        return Optional.ofNullable(remove(key));
    }

    /**
     * Performs the given action for each key-value pair of this dictionary.
     *
     * @param consumer the consumer.
     */
    default void forEach(BiConsumer<? super K, ? super V> consumer) {
        throw new UnsupportedOperationException();
    }
}
