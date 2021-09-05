package fr.alchemy.utilities.collections.array;

import fr.alchemy.utilities.Validator;

/**
 * <code>DefaultArrayIterator</code> is the default implementation of {@link ArrayIterator}.
 * 
 * @param <E> The type of elements contained in the array.
 * 
 * @version 0.2.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public class DefaultArrayIterator<E> implements ArrayIterator<E> {
	
    /**
     * The array for iteration.
     */
    private final Array<E> array;
    /**
     * The unsafe array for direct access.
     */
    private final E[] unsafeArray;
    /**
     * The current position in the array.
     */
    private int ordinal;

    /**
     * Instantiates a new <code>DefaultArrayIterator</code> to iterate over the provided {@link Array}.
     * 
     * @param array The array for iteration (not null).
     */
    public DefaultArrayIterator(Array<E> array) {
    	Validator.nonNull(array, "The array can't be null!");
        this.array = array;
        this.unsafeArray = array.array();
    }

    /**
     * Removes the current element in the <code>DefaultArrayIterator</code>.
     * The last element replaces the removed element in the array.
     * 
     * @see Array#fastRemove(int)
     */
    @Override
    public void fastRemove() {
        array.fastRemove(--ordinal);
    }

    /**
     * Return whether the <code>DefaultArrayIterator</code> has more elements
     * to iterate over.
     * 
     * @return Whether the iterator has more elements.
     */
    @Override
    public boolean hasNext() {
        return ordinal < array.size();
    }

    /**
     * The current index of the <code>DefaultArrayIterator</code> in the {@link Array}.
     *
     * @return The current index in the array (&ge;0, &lt;size).
     */
    @Override
    public int index() {
        return ordinal - 1;
    }

    /**
     * Return the next element of the <code>DefaultArrayIterator</code>.
     * 
     * @return The next element in the iteration, or null if none.
     */
    @Override
    public E next() {
        return ordinal >= unsafeArray.length ? null : unsafeArray[ordinal++];
    }

    /**
     * Removes from the {@link Array} the last element returned by the <code>DefaultArrayIterator</code>. 
     * The method can only be called once per {@link #next()} call.
     * 
     * @throws IllegalStateException Thrown if the next() method hasn't already been called.
     */
    @Override
    public void remove() {
    	if(ordinal < 0) {
    		throw new IllegalStateException();
    	}
    	
        array.remove(--ordinal);
    }

    @Override
    public String toString() {
    	return getClass().getSimpleName() + " [ array= " + array + ", index= " + ordinal + " ]";
    }
}
