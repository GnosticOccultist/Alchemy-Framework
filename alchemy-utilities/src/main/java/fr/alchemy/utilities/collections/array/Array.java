package fr.alchemy.utilities.collections.array;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.RandomAccess;

import fr.alchemy.utilities.Validator;
import fr.alchemy.utilities.collections.pool.Reusable;

/**
 * <code>Array</code> is an interface to implement dynamic arrays using the Java's {@link Collection} interface.
 * <p>
 * The interface and its implementation are supposed to provided safe access, for example with {@link ConcurrentArray} or
 * {@link ReadOnlyArray}, as well as more fleshed out methods using functional interfaces and lambdas.
 * <p>
 * The most basic implementation of this interface is the {@link FastArray}.
 * 
 * @param <E> The type of elements contained in the array.
 * 
 * @version 0.2.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 * 
 * @see ConcurrentArray
 * @see ReadOnlyArray
 * @see FastArray
 * 
 * @see SortedArray
 */
public interface Array<E> extends Collection<E>, Serializable, Reusable, Cloneable, RandomAccess {
	
	/**
	 * The default initial capacity of a new array.
	 */
	int DEFAULT_CAPACITY = 10;
	
	/**
	 * An empty read-only array.
	 */
	static ReadOnlyArray<?> EMPTY = new ReadOnlyFastArray<>(new Object[0]);
	
	/**
	 * Return an empty {@link ReadOnlyArray}.
	 * 
	 * @param <T> The type of element contained in the array.
	 * 
	 * @return An empty read-only array (not null).
	 */
	@SuppressWarnings("unchecked")
	static <T> ReadOnlyArray<T> empty() {
		return (ReadOnlyArray<T>) EMPTY;
	}
	
	/**
	 * Instantiates a new {@link Array} which will contain the provided element's type
	 * and with an initial capacity of 10 elements.
	 * 
	 * @param <T> The type of element contained in the array.
	 * 
	 * @param type The type of elements to contain (not null).
	 * @return	   A new array instance (not null).
	 */
	static <T> Array<T> ofType(Class<? super T> type) {
		Validator.nonNull(type, "The type can't be null!");
		return new FastArray<T>(type);
	}
	
	/**
	 * Instantiates a new {@link Array} which will contain the provided element's type
	 * and of the given initial capacity.
	 * 
	 * @param <T> The type of element contained in the array.
	 * 
	 * @param type 	   The type of elements to contain (not null).
	 * @param capacity The initial capacity of the array (&ge;0).
	 * @return	  	   A new array instance (not null).
	 */
	static <T> Array<T> ofType(Class<? super T> type, int capacity) {
		Validator.nonNull(type, "The type can't be null!");
		Validator.nonNegative(capacity, "The initial capacity can't be negative!");
		return new FastArray<T>(type, capacity);
	}
	
	/**
	 * Instantiates a new {@link ReadOnlyArray} which will contain the provided element.
	 * The created array will be read-only.
	 * 
	 * @param <T> The type of elements contained in the array.
	 * 
	 * @param element The element to add to the array (not null).
	 * @return		  A new read-only array containing the given element (not null).
	 */
	@SuppressWarnings("unchecked")
	static <T> ReadOnlyArray<T> of(T element) {
		Validator.nonNull(element, "The element can't be null!");
		
    	T[] newArray = (T[]) ArrayUtil.create(element.getClass(), 1);
    	newArray[0] = element;

    	return new ReadOnlyFastArray(newArray);
    }
	
	/**
	 * Instantiates a new {@link ReadOnlyArray} which will contain the provided array of elements.
	 * The created array will be read-only.
	 * 
	 * @param <T> The type of element contained in the array.
	 * 
	 * @param elements The elements to add to the array (not null, not empty).
	 * @return		   A new read-only array containing the given elements (not null).
	 */
	@SuppressWarnings("unchecked")
	static <T> ReadOnlyArray<T> ofArray(T[] elements) {
		Validator.nonEmpty(elements, "The elements can't be null or empty!");
		return (ReadOnlyArray<T>) new ReadOnlyFastArray(ArrayUtil.copyOf(elements, 0));
	}
	
	@SuppressWarnings("unchecked")
	static <T> ReadOnlyArray<T> of(T... elements) {
		Validator.nonEmpty(elements, "The elements can't be null or empty!");
		return (ReadOnlyArray<T>) new ReadOnlyFastArray(ArrayUtil.copyOf(elements, 0));
	}
	
	/**
	 * Instantiates a new {@link ReadOnlyArray} which will contain the provided {@link Array} of elements.
	 * The created array will be read-only.
	 * 
	 * @param <T> The type of element contained in the array.
	 * 
	 * @param elements The elements to add to the array (not null, not empty).
	 * @return		   A new read-only array containing the given elements (not null).
	 */
	@SuppressWarnings("unchecked")
	static <T> ReadOnlyArray<T> of(Array<T> elements) {
    	Validator.nonEmpty(elements, "The elements can't be null or empty!");
        return new ReadOnlyFastArray(Arrays.copyOfRange(elements.array(), 0, elements.size()));
    }
    
	/**
	 * Instantiates a new {@link ReadOnlyArray} which will contain the provided collection of elements.
	 * The created array will be read-only.
	 * 
	 * @param <T> The type of element contained in the array.
	 * 
	 * @param elements The elements to add to the array (not null, not empty).
	 * @return		   A new read-only array containing the given elements (not null).
	 */
    @SuppressWarnings("unchecked")
	static <T> ReadOnlyArray<T> of(Collection<T> elements) {
    	Validator.nonEmpty(elements, "The elements can't be null or empty!");
    	
    	Class<T> type = null;
    	Iterator<?> it = elements.iterator();
    	while (it.hasNext()) {
    		Object next = it.next();
    		if(next != null && type == null) {
    			type = (Class<T>) next.getClass();
    			break;
    		}
    	}
    	
    	T[] newArray = ArrayUtil.create(type, elements.size());
        return new ReadOnlyFastArray(elements.toArray(newArray));
    }
    
    /**
     * Adds all the elements contained in the provided array at the end of the <code>Array</code>, 
     * resizing the internal array if need be.
     * 
     * @param elements The array of elements to add to the array (not null).
     * @return 		   Whether the array was changed.
     */
    boolean addAll(E[] elements);
    
    /**
     * Return the internal unsafe array of the <code>Array</code>.
     * 
     * @return The internal array (not null).
     */
    E[] array();
    
    /**
     * Removes the element at the given index in the <code>Array</code>.
     * The last element replaces the removed element in the array.
     * 
     * @param index The index of the element to remove (&ge;0, &lt;size).
     * @return 		The removed element or null if none.
     * 
     * @throws NoSuchElementException Thrown if the provided index is out of range.
     */
    E fastRemove(int index);
    
    /**
     * Removes the specified element from the <code>Array</code>.
     * The last element replaces the removed element in the array.
     * 
     * @param element The element to remove (not null).
     * @return		  Whether the element was removed.
     * 
     * @throws NoSuchElementException Thrown if the provided index is out of range.
     */
    default boolean fastRemove(Object element) {
    	Validator.nonNull(element, "The element to remove can't be null!");

        int index = indexOf(element);

        if (index >= 0) {
        	fastRemove(index);
        }

        return index >= 0;
    }
    
    /**
     * Remove the element at the given index in the <code>Array</code>.
     * 
     * @param index The index of the element to remove (&ge;0, &lt;size).
     * @return		The removed element or null if none.
     */
    E remove(int index);
    
    /**
     * Remove the provided element from the <code>Array</code>.
     * 
     * @param element The element to remove (not null).
     * @return		  Whether the element was removed.
     */
    @Override
    default boolean remove(Object element) {
    	Validator.nonNull(element, "The element to remove can't be null!");
    	
    	int index = indexOf(element);

        if (index >= 0) {
            remove(index);
        }

        return index >= 0;
    }
    
    /**
     * Removes all the elements in the provided collection that are also contained in the <code>Array</code>.
     * The resulting array will therefore contain no elements in common with the specified collection.
     * 
     * @param elements The collection containing the elements to remove (not null).
     * @return		   Whether all elements from the collection have been removed.
     */
    @Override
    default boolean removeAll(Collection<?> elements) {
    	if(elements.isEmpty()) {
    		return false;
    	}
    	
    	int count = 0;

        for (Object element : elements) {
            if (remove(element)) {
                count++;
            }
        }

        return count == elements.size();
    }
    
    /**
     * Return the element at the specified index from the <code>Array</code>.
     * 
     * @param index The index of the element to retrieve (&ge;0, &lt;size).
     * @return		The element at the index, or null.
     */
    E get(int index);
    
    /**
     * Find the index of the provided element in the <code>Array</code>.
     * 
     * @param object The element to find (not null).
     * @return		 The index of the element or -1 if not found.
     */
    default int indexOf(Object object) {
    	Validator.nonNull(object, "The element can't be null!");
    	
    	int index = 0;
        for (E element : array()) {
            if (element == null) {
                break;
            } else if (Objects.equals(object, element)) {
                return index;
            }

            index++;
        }
        
        return -1;
    }
    
    /**
     * Returns whether the <code>Array</code> contains at least one instance of the provided element.
     * 
     * @param object The element to check presence of (not null).
     * @return 		 Whether the element is contained in the array.
     */
    @Override
    default boolean contains(Object object) {
    	Validator.nonNull(object, "The element can't be null!");
    	
        for (E element : array()) {
            if (element == null) {
                break;
            } else if (element.equals(object)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns whether the <code>Array</code> contains all the elements stored in the provided collection.
     * 
     * @param object The element to check presence of (not null).
     * @return 		 Whether all elements are contained in the array.
     */
    @Override
    default boolean containsAll(Collection<?> collection) {
        if (collection.isEmpty()) {
            return false;
        }

        for (Object element : collection) {
            if (element == null) {
                break;
            } else if (!contains(element)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Return an {@link Optional} value of the first retrieved element of the <code>Array</code>.
     * 
     * @return An optional value containing the first retrieved element or empty if none.
     */
    default Optional<E> pollSafe() {
    	return Optional.ofNullable(poll());
    }
    
    /**
     * Retrieves the first element of the <code>Array</code>.
     * 
     * @return The first element or null.
     */
    default E poll() {
    	return isEmpty() ? null : remove(0);
    }
    
    /**
     * Return an {@link Optional} value of the last retrieved element of the <code>Array</code>.
     * 
     * @return An optional value containing the last retrieved element or empty if none.
     */
    default Optional<E> popSafe() {
    	return Optional.ofNullable(pop());
    }
    
    /**
     * Retrieves the last element of the <code>Array</code>.
     * 
     * @return The last element or null.
     */
    default E pop() {
    	return isEmpty() ? null : fastRemove(size() - 1);
    }
    
    /**
     * Return an {@link Optional} value of the last element of the <code>Array</code>.
     * 
     * @return An optional value containing the last element or empty if none.
     */
    default Optional<E> lastSafe() {
    	return Optional.ofNullable(last());
    }
    
    /**
     * Return the last element of the <code>Array</code>.
     * 
     * @return The last element or null if none.
     */
    default E last() {
    	
    	int size = size();

        if (size < 1) {
            return null;
        }

        return get(size - 1);
    }
    
    /**
     * Return an {@link Optional} value of the first element of the <code>Array</code>.
     * 
     * @return An optional value containing the first element or empty if none.
     */
    default Optional<E> firstSafe() {
    	return Optional.ofNullable(first());
    }
    
    /**
     * Return the first element of the <code>Array</code>.
     * 
     * @return The first element or null if none.
     */
    default E first() {
    	
    	int size = size();

        if (size < 1) {
            return null;
        }

        return get(0);
    }
    
    /**
     * Sets the element at the given index in the <code>Array</code> to the provided one.
     * 
     * @param index   The index to set the element to.
     * @param element The element to set, or null to remove any previous element.
     */
    void set(int index, E element);
    
    /**
     * Return whether the <code>Array</code> is empty.
     * 
     * @return Whether the array is empty.
     */
    default boolean isEmpty() {
        return size() < 1;
    }
    
    /**
     * Retains only the elements contained in the specified collection that are also contained in 
     * the <code>Array</code>, the other ones are {@link #fastRemove(int)}.
     * 
     * @param target The collection to retain elements from (not null, not empty).
     * @return		 Whether the array was changed.
     */
    default boolean retainAll(Collection<?> target) {
    	Validator.nonEmpty(target, "The collection can't be null or empty!");
    	
        E[] array = array();
        for(int i = 0, length = size(); i < length; i++) {
            if(!target.contains(array[i])) {
                fastRemove(i--);
                length--;
            }
        }
        return true;
    }
    
    /**
     * Creates and return a new {@link ReadOnlyArray} version of the <code>Array</code>.
     * 
     * @return A new read-only array containing its elements (not null).
     */
    default ReadOnlyArray<E> readOnly() {
    	return Array.ofArray(array());
    }
    
    /**
     * Creates and return a copy of the internal array of the <code>Array</code>.
     * 
     * @return An array containing all elements (not null).
     */
	@Override
	@SuppressWarnings("unchecked")
    default E[] toArray() {
    	E[] array = array();
        return Arrays.copyOf(array, size(), (Class<E[]>) array.getClass());
    }
    
	/**
     * Copy the elements of the internal array of the <code>Array</code> into the provided
     * one if it's big enough, otherwise a new array is being allocated.
     * 
     * @param <T> The type of elements contained in the array.
     * 
     * @param newArray The new array to store the elements (only used if big enough).
     * @return 		   An array containing all elements (not null).
     */
	@Override
	@SuppressWarnings("unchecked")
    default <T> T[] toArray(T[] newArray) {
    	E[] array = array();
        if (newArray.length >= size()) {
            for (int i = 0, j = 0, length = array.length, newLength = newArray.length;
            	i < length && j < newLength; i++) {
                if (array[i] == null) {
                	continue;
                }
                newArray[j++] = (T) array[i];
            }
            return newArray;
        }

        Class<T[]> arrayClass = (Class<T[]>) newArray.getClass();
        Class<T> componentType = (Class<T>) arrayClass.getComponentType();

        return toArray(componentType);
    }
    
	/**
	 * Creates and return a copy of the internal array of the <code>Array</code> using the
	 * provided type of elements.
	 * 
	 * @param <T> The type of elements contained in the array.
	 * 
	 * @param componentType The type of components in the new array (not null).
	 * @return				An array containing all elements (not null).
	 */
    default <T> T[] toArray(Class<T> componentType) {
    	Validator.nonNull(componentType, "The type of components can't be null!");
    	
        T[] newArray = ArrayUtil.create(componentType, size());
        
        E[] array = array();
        System.arraycopy(array, 0, newArray, 0, size());

        return newArray;
    }
    
    /**
     * Cleanup the <code>Array</code> by clearing all of its elements, before for example 
     * injecting it back to a pool. 
     */
    @Override
    default void free() {
    	clear();
    }
}
