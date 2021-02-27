package fr.alchemy.utilities.collections.array;

import java.util.Collection;
import java.util.NoSuchElementException;

import fr.alchemy.utilities.Validator;

/**
 * <code>FastArray</code> is a basic and fast implementation of {@link AbstractArray}. The array is <b>NOT</b> thread safe.
 * 
 * @param <E> The type of elements contained in the array.
 * 
 * @version 0.1.1
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public class FastArray<E> extends AbstractArray<E> {
	
	private static final long serialVersionUID = -6711056773756527290L;

	/**
	 * The unsafe array.
	 */
	protected volatile E[] array;
	/**
	 * The current size of the array.
	 */
	protected int size;
	
	/**
	 * Instantiates a new empty <code>FastArray</code> of the provided type and with
	 * an initial capacity of 10.
	 * 
	 * @param type The type of elements to contain (not null).
	 */
	public FastArray(Class<? super E> type) {
		super(type);
	}
	
	/**
	 * Instantiates a new empty <code>FastArray</code> of the provided type and with
	 * the given initial capacity.
	 * 
	 * @param type	   The type of elements to contain (not null).
	 * @param capacity The initial capacity of the array (&ge;0).
	 */
	public FastArray(Class<? super E> type, int size) {
		super(type, size);
	}
	
	/**
	 * Instantiates a new <code>FastArray</code> using the provided array to use
	 * internally. The size is set accordingly to the array length.
	 * 
	 * @param array The internal array to use (not null).
	 */
    public FastArray(E[] array) {
        super(array);
    }
    
    /**
     * Adds the provided element at the end of the <code>FastArray</code>, resizing the internal 
     * array if need be.
     * 
     * @param element The element to add to the array (not null).
     * @return		  Whether the array was changed.
     */
    @Override
    public boolean add(E element) {
    	Validator.nonNull(element, "The element to add can't be null!");
    	
    	if(size == array.length) {
    		array = ArrayUtil.copyOf(array, Math.max(array.length >> 1, 1));
    	}
    	
    	array[size++] = element;
    	return true;
    }
    
    /**
     * Adds all the elements contained in the provided collection at the end of the <code>FastArray</code>, 
     * resizing the internal array if need be.
     * 
     * @param elements The collection of elements to add to the array (not null).
     * @return 		   Whether the array was changed.
     */
    @Override
    public boolean addAll(Collection<? extends E> elements) {
        if (elements.isEmpty()) {
            return false;
        }

        int current = array.length;
        int selfSize = size();
        int targetSize = elements.size();
        int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = ArrayUtil.copyOf(array, Math.max(current >> 1, diff));
        }

        for (E element : elements) {
        	array[size++] = element;
        }

        return true;
    }
    
    /**
     * Adds all the elements contained in the provided array at the end of the <code>FastArray</code>, 
     * resizing the internal array if need be.
     * 
     * @param elements The array of elements to add to the array (not null).
     * @return 		   Whether the array was changed.
     */
    @Override
    public boolean addAll(E[] elements) {
    	if (elements.length == 0) {
            return false;
        }

        int current = array.length;
        int selfSize = size();
        int targetSize = elements.length;
        int diff = selfSize + targetSize - current;

        if (diff > 0) {
            array = ArrayUtil.copyOf(array, Math.max(current >> 1, diff));
        }

        System.arraycopy(elements, 0, array, selfSize, targetSize);
        setSize(selfSize + targetSize);

        return true;
    }
    
    /**
     * Removes the element at the given index in the <code>FastArray</code>.
     * The last element replaces the removed element in the array.
     * 
     * @param index The index of the element to remove (&ge;0, &lt;size).
     * @return 		The removed element or null if none.
     * 
     * @throws NoSuchElementException Thrown if the provided index is out of range.
     */
	@Override
	public E fastRemove(int index) {
		Validator.inRange(index, 0, size() - 1);

        size -= 1;

        E old = array[index];

        array[index] = array[size];
        array[size] = null;
        
        return old;
	}

	/**
     * Remove the element at the given index in the <code>FastArray</code>.
     * The array is being entirely reordered.
     * 
     * @param index The index of the element to remove (&ge;0, &lt;size).
     * @return		The removed element or null if none.
     */
	@Override
	public E remove(int index) {
		Validator.inRange(index, 0, size() - 1);

        int numMoved = size - index - 1;
        E old = array[index];
        
        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        size -= 1;
        array[size] = null;

        return old;
	}
    
    /**
     * Return the element at the specified index from the <code>FastArray</code>.
     * 
     * @param index The index of the element to retrieve (&ge;0, &lt;size).
     * @return		The element at the index, or null.
     */
    @Override
    public final E get(int index) {
    	Validator.inRange(index, 0, size() - 1);
        return array[index];
    }
    
    /**
     * Sets the element at the given index in the <code>FastArray</code> to the provided one.
     * 
     * @param index   The index to set the element to.
     * @param element The element to set, or null to remove any previous element.
     */
    @Override
    public void set(int index, E element) {
    	Validator.inRange(index, 0, size() - 1);
    	
    	array[index] = element;
    }

    /**
     * Return the internal unsafe array of the <code>FastArray</code>.
     * 
     * @return The internal array (not null).
     */
	@Override
    public final E[] array() {
    	return array;
    }
    
	/**
     * Sets the new internal array of the <code>FastArray</code>.
     * 
     * @param array The new internal array (not null).
     */
    @Override
    protected final void setArray(E[] array) {
    	Validator.nonNull(array, "The internal array can't be null!");
    	this.array = array;
    }
    
    /**
     * Return the size of the <code>FastArray</code>.
     * 
     * @return The size of the array (&ge;0).
     */
    @Override
    public final int size() {
    	return size;
    }
    
    /**
     * Sets the new size of the <code>FastArray</code>.
     * 
     * @param size The new size of the array (&ge;0).
     */
    @Override
    protected final void setSize(int size) {
    	Validator.nonNegative(size, "The size can't be negative!");
    	this.size = size;
    }

	/**
	 * Returns an {@link ArrayIterator} to iterate over the elements of 
	 * the <code>FastArray</code>.
	 * 
	 * @return An iterator implementation to iterate over the array (not null).
	 */
	@Override
	public ArrayIterator<E> iterator() {
		return new DefaultArrayIterator<>(this);
	}
}
