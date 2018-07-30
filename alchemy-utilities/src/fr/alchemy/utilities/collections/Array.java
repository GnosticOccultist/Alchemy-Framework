package fr.alchemy.utilities.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

import fr.alchemy.utilities.functions.ModifierAction;
import fr.alchemy.utilities.functions.VoidAction;

/**
 * <code>Array</code> represents a dynamic implementation of an array, meaning
 * it can be resized and its elements added or removed during its life-time.
 * 
 * @author GnosticOccultist
 */
public class Array<E> implements Collection<E> {

	/**
	 * The simple array containing the values.
	 */
	protected E[] array;
	/**
	 * The size of the array.
	 */
	protected int size;
	
	/**
	 * Creates a new empty <code>Array</code> with a size of 10.
	 *    
	 * @param type The type of object that the array will contain.
	 * @return	   The new empty array.
	 */
	public static <T> Array<T> newEmptyArray(final Class<T> type) {
		return new Array<>(type);
	}
	
	/**
	 * Creates a new empty <code>Array</code> with the provided 
	 * initial size.
	 *    
	 * @param type The type of object that the array will contain.
	 * @param size The initial size of the array.
	 * @return	   The new empty array.
	 */
	public static <T> Array<T> newEmptyArray(final Class<T> type, final int size) {
		return new Array<>(type, size);
	}
	
	private Array(final Class<E> type) {
		this(type, 10);
	}
	
	private Array(final Class<E> type, final int size) {
		if(size < 0) {
			throw new IllegalArgumentException("Size cannot be negative!");
		}
		
		setArray(createNewArray(type, size));
	}
	
	@Override
	public boolean add(final E object) {
		if(size == array.length) {
			array = resize(Math.max(array.length >> 1, 1));
		}
		
		array[size++] = object;
        return true;
	}
	
    @SuppressWarnings("unchecked")
	private E[] resize(int added) {
        Object[] newArray = (Object[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), array.length + added);

        E[] copy = (E[]) newArray;

        System.arraycopy(array, 0, copy, 0, Math.min(array.length, copy.length));

        return copy;
    }

    @SuppressWarnings("unchecked")
	private E[] createNewArray(final Class<E> type, final int size) {
    	Object[] newArray = (Object[]) java.lang.reflect.Array.newInstance(type, size);
		return (E[]) newArray;
    }
    
	@Override
	public boolean addAll(Collection<? extends E> objects) {
        if (objects.isEmpty()) {
            return false;
        }

        int current = array.length;
        int selfSize = size();
        int targetSize = objects.size();
        int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = resize(Math.max(current >> 1, diff));
        }

        for (E element : objects) {
        	array[size++] = element;
        }

        return true;
	}

	@Override
	public void clear() {
		if (!isEmpty()) {
			for (int i = 0, length = array.length; i < length; i++) {
				array[i] = null;
			}
			setSize(0);
		}
	}

	@Override
	public boolean contains(final Object object) {
        for (int i = 0; i < size; i++) {
            if (array[i] == null) {
                break;
            } else if (array[i].equals(object)) {
                return true;
            }
        }
        return false;
	}

	@Override
	public boolean containsAll(final Collection<?> objects) {
        if (isEmpty()) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (array[i] == null) {
                break;
            } else if (!contains(array[i])) {
                return false;
            }
        }

        return true;
	}

	@Override
	public boolean isEmpty() {
		return size < 1;
	}

	@Override
	public Iterator<E> iterator() {
		return new ArrayIterator<>(this);
	}

	@Override
	public boolean remove(Object object) {
        int index = indexOf(object);

        if (index >= 0) {
            remove(index);
        }
        
        return index >= 0;
	}
	
    /**
     * Finds the index of the provided object in the <code>Array</code>.
     *
     * @param object The object to find the index.
     * @return 		 The index of the object or -1.
     */
    public int indexOf(final Object object) {
        int index = 0;

        for (int i = 0; i < size; i++) {
            if (array[i] == null) {
                break;
            } else if (Objects.equals(object, array[i])) {
                return index;
            }
            index++;
        }

        return -1;
    }
	
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new NoSuchElementException();
        }

        int numMoved = size - index - 1;
        E old = array[index];

        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        size -= 1;
        array[size] = null;

        return old;
    }

	@Override
	public boolean removeAll(Collection<?> objects) {
        if (objects.isEmpty()) {
            return false;
        }

        int count = 0;

        for (Object element : objects) {
            if (element == null) {
                break;
            } else if (remove(element)) {
                count++;
            }
        }

        return count == objects.size();
	}

	@Override
	public boolean retainAll(Collection<?> objects) {
        E[] array = array();
        for (int i = 0, length = size(); i < length; i++) {
            if (!objects.contains(array[i])) {
                remove(i--);
                length--;
            }
        }
        return true;
	}

	@Override
	public Object[] toArray() {
		E[] array = array();
		return Arrays.copyOf(array, size(), array.getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] newArray) {
		E[] array = array();

		if (newArray.length >= size()) {
			for (int i = 0, j = 0, length = array.length, newLength = newArray.length; i < length && j < newLength; i++) {
				if (array[i] == null) continue;
				newArray[j++] = (T) array[i];
			}
			return newArray;
		}
		Class<T[]> arrayClass = (Class<T[]>) newArray.getClass();
		Class<T> componentType = (Class<T>) arrayClass.getComponentType();

     	newArray = (T[]) createNewArray((Class<E>) componentType, size());
        array = array();

        System.arraycopy(array, 0, newArray, 0, size());
        
        return newArray;
	}
	
	/**
	 * @return The wrapped array.
	 */
    public final E[] array() {
        return array;
    }
	
	/**
	 * Sets the wrapped array.
	 * 
	 * @param array The wrapped array.
	 */
	protected void setArray(final E[] array) {
		this.array = array;
	}
	
	/**
	 * Sets the size of the <code>Array</code>.
	 * 
	 * @param size The size of the array.
	 */
	protected void setSize(final int size) {
		this.size = size;
	}
	
	/**
	 * @return The size of the <code>Array</code>.
	 */
	@Override
	public final int size() {
		return size;
	}
	
	/**
	 * Applies the modifying action to each element of the <code>Array</code>.
	 * 
	 * @param action The modifier action.
	 */
	public void modify(final ModifierAction<E> action) {
		final E[] array = array();

		for (int i = 0, length = size(); i < length; i++) {
			array[i] = action.modify(array[i]);
		}
	}
	
	/**
	 * Applies the action to each element of the <code>Array</code>.
	 * 
	 * @param action The action to execute.
	 */
	public void perform(final VoidAction<? super E> action) {
		final E[] array = array();

		for (int i = 0, length = size(); i < length; i++) {
			
			if(array[i] == null) {
				break;
			}
			
			action.perform(array[i]);
		}
	}
	
	@Override
	public void forEach(final Consumer<? super E> consumer) {
		final E[] array = array();
		
		for (int i = 0, length = size(); i < length; i++) {
			
			if(array[i] == null) {
				break;
			}
			
			consumer.accept(array[i]);
		}
	}
}
