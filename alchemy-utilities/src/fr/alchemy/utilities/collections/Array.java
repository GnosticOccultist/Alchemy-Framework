package fr.alchemy.utilities.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Array<E> implements Collection<E> {

	/**
	 * The simple array containing the values.
	 */
	protected E[] array;
	/**
	 * The size of the array.
	 */
	protected int size;
	
	public Array(final Class<E> type) {
		this(type, 10);
	}
	
	public Array(final Class<E> type, final int size) {
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
	
	protected void setArray(final E[] array) {
		this.array = array;
	}
	
	protected void setSize(final int size) {
		this.size = size;
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
		throw new UnsupportedOperationException("No iterator available for now!");
	}

	@Override
	public boolean remove(Object object) {
        int index = indexOf(object);

        if (index >= 0) {
            remove(index);
        }
        
        return index >= 0;
	}
	
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
	public final int size() {
		return size;
	}
	
    public final E[] array() {
        return array;
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

     	newArray = (T[]) java.lang.reflect.Array.newInstance(componentType, size());
        array = array();

        System.arraycopy(array, 0, newArray, 0, size());
        
        return newArray;
	}
}
