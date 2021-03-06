package fr.alchemy.utilities.collections.array;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.RandomAccess;

import fr.alchemy.utilities.collections.pool.Reusable;

public interface Array<E> extends Collection<E>, Serializable, Reusable, Cloneable, RandomAccess {
	
	static <T> Array<T> ofType(Class<? super T> type) {
		return new FastArray<T>(type);
	}
	
	static <T> Array<T> ofType(Class<? super T> type, int size) {
		return new FastArray<T>(type, size);
	}
    
	@SuppressWarnings("unchecked")
	static <T> ReadOnlyArray<T> of(T[] elements) {
		return (ReadOnlyArray<T>) new ReadOnlyFastArray(ArrayUtil.copyOf(elements, 0));
	}
    
	@SuppressWarnings("unchecked")
	static <T> ReadOnlyArray<T> of(T element) {
    	T[] newArray = (T[]) ArrayUtil.create(element.getClass(), 1);
    	newArray[0] = element;

    	return new ReadOnlyFastArray(newArray);
    }
    
    static ReadOnlyArray<Object> ofAll(Collection<Object> elements) {
    	Object[] newArray = ArrayUtil.create(Object.class, elements.size());
       	
    	return Array.of(elements.toArray(newArray));
    }
    
    @SuppressWarnings("unchecked")
	static <T> ReadOnlyArray<T> of(Collection<T> elements) {
    	Class<T> type = null;
    	Iterator<?> it = elements.iterator();
    	while (it.hasNext()) {
    		Object next = it.next();
    		if(next != null && type == null) {
    			type = (Class<T>) next.getClass();
    		}
    	}
    	T[] newArray = ArrayUtil.create(type, elements.size());
    	
        return Array.of(elements.toArray(newArray));
    }
    
    /**
     * Return the internal array of the <code>Array</code>.
     * 
     * @return The internal array.
     */
    E[] array();
    
    /**
     * Removes the element at the given index with reordering.
     * 
     * @param index The index for removing the element.
     * @return 		The removed element.
     */
    E fastRemove(int index);
    
    /**
     * Removes the specified element with reordering.
     * 
     * @param object The element to remove.
     * @return		 Whether the element was removed.
     */
    default boolean fastRemove(Object object) {

        int index = indexOf(object);

        if (index == -1) {
            return false;
        }

        fastRemove(index);
        return true;
    }
    
    /**
     * Remove the element at the given index without reordering.
     * 
     * @param index The index of the element.
     * @return		The removed element.
     */
    E slowRemove(int index);
    
    /**
     * Remove the provided element without reordering.
     * 
     * @param object The element to remove.
     * @return		 Whether the element was removed.
     */
    default boolean remove(Object object) {
    	return slowRemove(object);
    }
    
    /**
     * Remove the provided element without reordering.
     * 
     * @param object The element to remove.
     * @return		 Whether the element was removed.
     */
    boolean slowRemove(Object object);
    
    /**
     * Return the element at the specified index from this <code>Array</code>.
     * 
     * @param index The index of the element to retrieve.
     * @return		The element at the index, or null.
     */
    E get(int index);
    
    /**
     * Find the index of the provided object in this <code>Array</code>.
     * 
     * @param object The object to find.
     * @return		 The index of the object or -1 if not found.
     */
    default int indexOf(Object object) {
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
    
    @Override
    default boolean contains(Object object) {
        for (E element : array()) {
            if (element == null) {
                break;
            } else if (element.equals(object)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    default boolean containsAll(Collection<?> array) {
        if (array.isEmpty()) {
            return false;
        }

        for (Object element : array) {
            if (element == null) {
                break;
            } else if (!contains(element)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Retrieves the first element of this <code>Array</code>.
     * 
     * @return The first element or null.
     */
    default E poll() {
    	return isEmpty() ? null : slowRemove(0);
    }
    
    /**
     * Retrieves the last element of this <code>Array</code>.
     * 
     * @return The last element or null.
     */
    default E pop() {
    	return isEmpty() ? null : fastRemove(size() - 1);
    }
    
    E set(int index, E element);
    
    /**
     * Return whether the <code>Array</code> is empty.
     * 
     * @return Whether the array is empty.
     */
    default boolean isEmpty() {
        return size() < 1;
    }
    
    default boolean retainAll(Array<?> target) {
        E[] array = array();
        for(int i = 0, length = size(); i < length; i++) {
            if(!target.contains(array[i])) {
                fastRemove(i--);
                length--;
            }
        }
        return true;
    }
    
    default boolean retainAll(Collection<?> target) {
        E[] array = array();
        for(int i = 0, length = size(); i < length; i++) {
            if(!target.contains(array[i])) {
                fastRemove(i--);
                length--;
            }
        }
        return true;
    }
    
    default ReadOnlyArray<E> readOnly() {
    	return Array.of(array());
    }
    
    @SuppressWarnings("unchecked")
	@Override
    default E[] toArray() {
    	E[] array = array();
        return Arrays.copyOf(array, size(), (Class<E[]>) array.getClass());
    }
    
    @SuppressWarnings("unchecked")
	@Override
    default <T> T[] toArray(T[] newArray) {
    	E[] array = array();
        if (newArray.length >= size()) {
            for (int i = 0, j = 0, length = array.length, newLength = newArray.length;
            	i < length && j < newLength; i++) {
                if (array[i] == null) continue;
                newArray[j++] = (T) array[i];
            }
            return newArray;
        }

        Class<T[]> arrayClass = (Class<T[]>) newArray.getClass();
        Class<T> componentType = (Class<T>) arrayClass.getComponentType();

        return toArray(componentType);
    }
    
    default <T> T[] toArray(Class<T> componentType) {
        T[] newArray = ArrayUtil.create(componentType, size());
        
        E[] array = array();
        System.arraycopy(array, 0, newArray, 0, size());

        return newArray;
    }
    
    @Override
    default void free() {
    	clear();
    }
}
