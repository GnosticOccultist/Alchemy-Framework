package fr.alchemy.utilities.collections;

import java.util.Iterator;

/**
 * <code>ArrayIterator</code> is the {@link Iterator} implementation for an {@link Array}. 
 * 
 * @author GnosticOccultist
 */
public class ArrayIterator<E> implements Iterator<E> {

	/**
	 * The array used for iteration.
	 */
	private final Array<E> array;
    /**
     * The unsafe array for directly access.
     */
    private final E[] unsafeArray;
    /**
     * The current position in the array.
     */
    private int ordinal;
	
    public ArrayIterator(final Array<E> array) {
        this.array = array;
        this.unsafeArray = array.array();
    }

	@Override
	public boolean hasNext() {
		return ordinal < array.size();
	}

	@Override
	public E next() {
		return ordinal >= unsafeArray.length ? null : unsafeArray[ordinal++];
	}
}
