package fr.alchemy.utilities.collections.array;

import java.util.Iterator;

public interface ArrayIterator<E> extends Iterator<E> {

    /**
     * Removes the current element using reordering.
     */
    void fastRemove();

    /**
     * Index int.
     *
     * @return the current position of this {@link Array}.
     */
    int index();
}
