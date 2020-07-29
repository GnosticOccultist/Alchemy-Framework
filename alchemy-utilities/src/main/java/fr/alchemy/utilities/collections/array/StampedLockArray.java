package fr.alchemy.utilities.collections.array;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;

import fr.alchemy.utilities.Validator;

/**
 * <code>StampedLockArray</code> is an implementation of {@link AbstractArray} with a thread-safe access
 * guaranteed using a {@link StampedLock}.
 * <p>
 * In order to perform read or write safe actions, methods requiring functional interfaces or lambdas expressions can be used, because
 * their execution occurs in a locked block.
 * For example, to remove an element in a thread-safe manner:
 * <pre>
 * var toRemove = ...
 * var result = applyInWriteLock(toRemove, ConcurrentArray::remove);
 * </pre>
 * 
 * @param <E> The type of element contained in the array.
 * 
 * @version 0.2.0
 * @since 0.2.0
 * 
 * @author GnosticOccultist
 */
public class StampedLockArray<E> extends AbstractArray<E> implements ConcurrentArray<E> {
	
	private static final long serialVersionUID = -7811030477153329829L;

	/**
	 * The unsafe array.
	 */
	protected volatile E[] array;
	/**
	 * The current size of this array.
	 */
	protected final AtomicInteger size;
	/**
	 * The stamped lock for thread-safety.
	 */
	protected final StampedLock lock;
	
	/**
	 * Instantiates a new empty <code>StampedLockArray</code> of the provided type and with
	 * an initial capacity of 10.
	 * 
	 * @param type The type of elements to contain (not null).
	 */
	public StampedLockArray(Class<? super E> type) {
		super(type);
		this.size = new AtomicInteger();
		this.lock = new StampedLock();
	}
	
	/**
	 * Instantiates a new empty <code>StampedLockArray</code> of the provided type and with
	 * the given initial capacity.
	 * 
	 * @param type	   The type of elements to contain (not null).
	 * @param capacity The initial capacity of the array (&ge;0).
	 */
	public StampedLockArray(Class<? super E> type, int size) {
		super(type, size);
		this.size = new AtomicInteger();
		this.lock = new StampedLock();
	}
	
	/**
	 * Instantiates a new <code>StampedLockArray</code> using the provided array to use
	 * internally. The size is set accordingly to the array length.
	 * 
	 * @param array The internal array to use (not null).
	 */
    public StampedLockArray(E[] array) {
        super(array);
        this.size = new AtomicInteger();
		this.lock = new StampedLock();
    }
    
    /**
     * Adds the provided element at the end of the <code>StampedLockArray</code>, 
     * resizing the internal array if need be.
     * 
     * @param element The element to add to the array (not null).
     * @return		  Whether the array was changed.
     */
    @Override
	public boolean add(E element) {
		if(size() == array.length) {
    		array = ArrayUtil.copyOf(array, Math.max(array.length >> 1, 1));
    	}
    	
		array[size.getAndIncrement()] = element;
        return true;
	}
	
    /**
     * Adds all the elements contained in the provided collection at the end of the 
     * <code>StampedLockArray</code>, resizing the internal array if need be.
     * 
     * @param elements The collection of elements to add to the array (not null).
     * @return 			 Whether the array was changed.
     */
	@Override
	public boolean addAll(Collection<? extends E> collection) {
		if (collection.isEmpty()) {
            return false;
        }

        int current = array.length;
        int diff = size() + collection.size() - current;

        if (diff > 0) {
            array = ArrayUtil.copyOf(array, Math.max(current >> 1, diff));
        }

        for (E element : collection) {
        	array[size.getAndIncrement()] = element;
        }

        return true;
	}

	/**
     * Removes the element at the given index in the <code>StampedLockArray</code>.
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

        var newSize = size.decrementAndGet();
        var old = array[index];

        array[index] = array[newSize];
        array[newSize] = null;

        return old;
	}

	/**
     * Remove the element at the given index in the <code>StampedLockArray</code>.
     * The array is being entirely reordered.
     * 
     * @param index The index of the element to remove (&ge;0, &lt;size).
     * @return		The removed element or null if none.
     */
	@Override
	public E remove(int index) {
		Validator.inRange(index, 0, size() - 1);

        int length = size();
        int numMoved = length - index - 1;

        E old = array[index];

        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }

        array[size.decrementAndGet()] = null;
        return old;
	}

	/**
     * Return the element at the specified index from the <code>StampedLockArray</code>.
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
	 * Acquires the {@link StampedLock} to perform a reading action with the <code>StampedLockArray</code>.
	 * 
	 * @return The stamp to use for unlocking the lock.
	 */
	@Override
	public long readLock() {
		return lock.readLock();
	}

	/**
	 * Release the previously acquired {@link StampedLock} for reading with the <code>StampedLockArray</code>.
	 * The lock will only be release if the provided stamp matches the current lock's state.
	 * 
	 * @param stamp The stamp to release the lock.
	 * 
	 * @throws IllegalMonitorStateException Thrown if the stamp doesn't match the current lock's state.
	 */
	@Override
	public void readUnlock(long stamp) {
		this.lock.unlockRead(stamp);
	}

	/**
	 * Acquires the {@link StampedLock} to perform a writing action with the <code>StampedLockArray</code>.
	 * 
	 * @return The stamp to use for unlocking the lock.
	 */
	@Override
	public long writeLock() {
		return lock.writeLock();
	}

	/**
	 * Release the previously acquired {@link StampedLock} for writing with the <code>StampedLockArray</code>.
	 * The lock will only be release if the provided stamp matches the current lock's state.
	 * 
	 * @param stamp The stamp to release the lock.
	 * 
	 * @throws IllegalMonitorStateException Thrown if the stamp doesn't match the current lock's state.
	 */
	@Override
	public void writeUnlock(long stamp) {
		this.lock.unlockWrite(stamp);
	}
	
	/**
     * Return the internal unsafe array of the <code>StampedLockArray</code>.
     * 
     * @return The internal array (not null).
     */
	@Override
	public E[] array() {
		return array;
	}
	
	/**
     * Sets the new internal array of the <code>StampedLockArray</code>.
     * 
     * @param array The new internal array (not null).
     */
	@Override
	protected void setArray(E[] array) {
		Validator.nonNull(array, "The internal array can't be null!");
		this.array = array;
	}

	/**
     * Return the size of the <code>StampedLockArray</code>.
     * 
     * @return The size of the array (&ge;0).
     */
	@Override
	public int size() {
		return size.get();
	}
	
	/**
     * Sets the new size of the <code>StampedLockArray</code>.
     * 
     * @param size The new size of the array (&ge;0).
     */
	@Override
	protected void setSize(int size) {
		Validator.nonNegative(size, "The size can't be negative!");
		this.size.set(size);
	}
	
	/**
	 * Returns an {@link ArrayIterator} to iterate over the elements of 
	 * the <code>StampedLockArray</code>.
	 * 
	 * @return An iterator implementation to iterate over the array (not null).
	 */
	@Override
	public Iterator<E> iterator() {
		return new DefaultArrayIterator<>(this);
	}
}
