package fr.alchemy.utilities.collections.array;

import java.util.Iterator;
import java.util.Optional;

/**
 * <code>ArrayIterator</code> is an implementation of {@link Iterator} to iterate over an {@link Array}.
 * 
 * @param <E> The type of elements contained in the array.
 * 
 * @version 0.2.0
 * @since 0.1.0
 * 
 * @author GnosticOccultist
 */
public interface ArrayIterator<E> extends Iterator<E> {

	/**
     * Return an {@link Optional} value of the next element of the <code>DefaultArrayIterator</code>.
     * 
     * @return An optional value containing the next element in the iteration, or empty if none.
     */
    default Optional<E> nextSafe() {
        return Optional.ofNullable(next());
    }
	
	/**
     * Removes the current element in the <code>ArrayIterator</code>.
     * The last element replaces the removed element in the array.
     */
    void fastRemove();

    /**
     * The current index of the <code>ArrayIterator</code> in the {@link Array}.
     *
     * @return The current index in the array (&ge;0, &lt;size).
     */
    int index();
}
